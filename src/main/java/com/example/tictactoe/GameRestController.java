package com.example.tictactoe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GameRestController {

    @Autowired
    WebSocketHandler webSocketHandler;

    @PostMapping("createGame/{sessionId}")
    public void createGame(@PathVariable String sessionId) throws IOException {
        webSocketHandler.createGame(sessionId);
    }

    @PostMapping("joinGame/{gameId}/{sessionId}")
    public void joinGame(@PathVariable String gameId, @PathVariable String sessionId) throws IOException {
        webSocketHandler.joinGame(gameId, sessionId);
    }
}
