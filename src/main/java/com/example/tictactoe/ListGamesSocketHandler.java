package com.example.tictactoe;

import com.example.tictactoe.game.Game;
import com.example.tictactoe.game.GameApplication;
import com.example.tictactoe.game.Player;
import com.example.tictactoe.presentation.GamePresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ListGamesSocketHandler extends AbstractWebSocketHandler {

    static List<WebSocketSession> ListGamesSessions = new ArrayList<>();
    static GameApplication gameApplication = new GameApplication();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ListGamesSessions.add(session);
        List<GamePresentation> gamePresentations = gameApplication.listGames().stream()
                .map(this::toGamePresentation)
                .collect(Collectors.toList());
        Map<String,Object> data = new HashMap<>();
        data.put("opCode","mainPage");
        data.put("sessionId",session.getId());
        data.put("currentGames",gamePresentations);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
        super.afterConnectionEstablished(session);
    }

    private GamePresentation toGamePresentation(Game game) {
        GamePresentation gamePresentation = new GamePresentation();
        gamePresentation.id = game.getId();
        gamePresentation.title = "game-"+game.getId();
        gamePresentation.creator = game.getCreator().getId();
        gamePresentation.nbOfPlayers = game.listPlayers().size();
        gamePresentation.isJoinable = game.listPlayers().size() < 2 ;
        gamePresentation.gameState = game.getGameState();
        gamePresentation.otherPlayer = game.getOtherPlayer().map(Player::getId).orElse(null);
        return gamePresentation;
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

    GamePresentation createGame(String sessionId) throws IOException {
        Game newGame = gameApplication.createNewGame(new Player(sessionId));
        GamePresentation newGamePresentation = toGamePresentation(newGame);
        Map<String,Object> data = new HashMap<>();
        data.put("opCode","gameCreated");
        data.put("newGame", newGamePresentation);
        broadcast(data);
        return newGamePresentation;
    }

    private void broadcast(Map<String, Object> data) throws IOException {
        for (WebSocketSession webSocketSession : ListGamesSessions) {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
        }
    }

    public void joinGame(String gameId, String sessionId) throws IOException {
        Game game = gameApplication.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException("unknown game id"));
        game.join(new Player(sessionId));
        GamePresentation gamePresentation = toGamePresentation(game);
        Map<String,Object> data = new HashMap<>();
        data.put("opCode","gameUpdated");
        data.put("game", gamePresentation);
        broadcast(data);
    }

    public Optional<GamePresentation> getGameData(String gameId){
        return gameApplication.getGameById(gameId)
                .map(this::toGamePresentation);
    }
}
