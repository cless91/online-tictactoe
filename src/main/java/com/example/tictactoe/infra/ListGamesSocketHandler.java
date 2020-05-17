package com.example.tictactoe.infra;

import com.example.tictactoe.game.*;
import com.example.tictactoe.presentation.GamePresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ListGamesSocketHandler extends AbstractWebSocketHandler {

    static List<WebSocketSession> ListGamesSessions = new ArrayList<>();
    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ListGamesSessions.add(session);
        List<GamePresentation> gamePresentations = gameRepository.listGames().stream()
                .map(GamePresentation::fromGame)
                .collect(Collectors.toList());
        Map<String,Object> data = new HashMap<>();
        data.put("opCode","mainPage");
        data.put("sessionId",session.getId());
        data.put("currentGames",gamePresentations);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        ListGamesSessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.printf("txt msg received: %s\n", message.getPayload());
        TextMessage textMessage = new TextMessage(String.format("%s: %s", session.getId(), message.getPayload()));
        for (WebSocketSession webSocketSession : ListGamesSessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(textMessage);
            }
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        System.out.println("binary msg received");
        for (WebSocketSession webSocketSession : ListGamesSessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    public void broadcast(Map<String, Object> data) throws IOException {
        for (WebSocketSession webSocketSession : ListGamesSessions) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
        }
    }

    public void joinGame(String gameId, String sessionId) throws IOException {
        Game game = gameRepository.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException("unknown game id"));
        game.join(new Player(sessionId));
        GamePresentation gamePresentation = GamePresentation.fromGame(game);
        Map<String,Object> data = new HashMap<>();
        data.put("opCode","gameUpdated");
        data.put("game", gamePresentation);
        broadcast(data);
    }

    public Optional<GamePresentation> getGameData(String gameId){
        return gameRepository.getGameById(gameId)
                .map(GamePresentation::fromGame);
    }
}
