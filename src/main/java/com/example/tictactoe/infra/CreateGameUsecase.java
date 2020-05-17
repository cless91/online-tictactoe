package com.example.tictactoe.infra;

import com.example.tictactoe.game.Game;
import com.example.tictactoe.game.GameFactory;
import com.example.tictactoe.game.Player;
import com.example.tictactoe.presentation.GamePresentation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CreateGameUsecase {
    private final GameFactory gameFactory;

    private final ListGamesSocketHandler socketHandler;

    GamePresentation createGame(String sessionId) throws IOException {
        Game newGame = gameFactory.createNewGame(new Player(sessionId));
        GamePresentation newGamePresentation = GamePresentation.fromGame(newGame);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameCreated");
        data.put("newGame", newGamePresentation);
        socketHandler.broadcast(data);
        return newGamePresentation;
    }
}