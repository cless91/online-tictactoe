package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameFactory;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.example.tictactoe.presentation.GamePresentation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CreateGameUsecase {
    private final GameFactory gameFactory;

    private final ListGamesSocketHandler socketHandler;
    public GamePresentation createGame(String sessionId) throws IOException {
        Game newGame = gameFactory.createNewGame(new Player(sessionId));
        GamePresentation newGamePresentation = GamePresentation.fromGame(newGame);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameCreated");
        data.put("newGame", newGamePresentation);
        socketHandler.broadcast(data);
        return newGamePresentation;
    }
}