package com.example.tictactoe;

import com.example.tictactoe.game.Game;
import com.example.tictactoe.game.GameApplication;
import com.example.tictactoe.game.Player;
import com.example.tictactoe.presentation.GamePresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebSocketHandler extends AbstractWebSocketHandler {

    List<WebSocketSession> sessions = new ArrayList<>();
    GameApplication gameApplication = new GameApplication();
    ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketHandler() {
        Game joinableGame = gameApplication.createNewGame(new Player("player test"));
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game unjoinableGame = gameApplication.createNewGame(player1);
        unjoinableGame.join(player2);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        List<GamePresentation> gamePresentations = gameApplication.listGames().stream()
                .map(this::toGamePresentation)
                .collect(Collectors.toList());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gamePresentations)));
        super.afterConnectionEstablished(session);
    }

    private GamePresentation toGamePresentation(Game game) {
        GamePresentation gamePresentation = new GamePresentation();
        gamePresentation.id = game.getId();
        gamePresentation.title = "game-"+game.getId();
        gamePresentation.creator = game.getCreator().getId();
        gamePresentation.nbOfPlayers = game.listPlayers().size();
        gamePresentation.isJoinable = game.listPlayers().size() < 2 ;
        return gamePresentation;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.printf("txt msg received: %s\n", message.getPayload());
        TextMessage textMessage = new TextMessage(String.format("%s: %s", session.getId(), message.getPayload()));
        for (WebSocketSession webSocketSession : sessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(textMessage);
            }
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        System.out.println("binary msg received");
        for (WebSocketSession webSocketSession : sessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
    }
}
