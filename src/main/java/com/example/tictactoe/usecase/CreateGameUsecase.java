package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameFactory;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.example.tictactoe.presentation.GamePresentation;
import com.example.tictactoe.presentation.GamePresentationFactory;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CreateGameUsecase {
    private final GameFactory gameFactory;
    private final ListGamesSocketHandler listGamesSocketHandler;
    private final GamePresentationFactory gamePresentationFactory;

    public GamePresentation createGame(String sessionId) throws IOException {
        Game game = gameFactory.createNewGame(new Player(sessionId));
        GamePresentation newGamePresentation = gamePresentationFactory.fromGame(game);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameCreated");
        data.put("newGame", newGamePresentation);
        listGamesSocketHandler.broadcast(data);
        return newGamePresentation;
    }
}