package com.example.tictactoe.infra;

import com.example.tictactoe.presentation.GamePresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GameRestController {

    @Autowired
    ListGamesSocketHandler listGamesSocketHandler;

    @Autowired
    CreateGameUsecase createGameUsecase;

    @PostMapping("createGame/{sessionId}")
    public GamePresentation createGame(@PathVariable String sessionId) throws IOException {
        return createGameUsecase.createGame(sessionId);
    }

    @PostMapping("joinGame/{gameId}/{sessionId}")
    public void joinGame(@PathVariable String gameId, @PathVariable String sessionId) throws IOException {
        listGamesSocketHandler.joinGame(gameId, sessionId);
    }

    @PostMapping("gameData/{gameId}")
    public GamePresentation getGameData(@PathVariable String gameId) throws IOException {
        GamePresentation gamePresentation = listGamesSocketHandler.getGameData(gameId).orElseThrow(
                () -> new GameNotFoundException(String.format("game not found : %s", gameId))
        );
        return gamePresentation;
    }
}
