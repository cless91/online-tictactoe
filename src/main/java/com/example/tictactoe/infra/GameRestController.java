package com.example.tictactoe.infra;

import com.example.tictactoe.presentation.GamePresentation;
import com.example.tictactoe.usecase.CreateGameUsecase;
import com.example.tictactoe.usecase.JoinGameUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
public class GameRestController {

    @Autowired
    private ListGamesSocketHandler listGamesSocketHandler;

    @Autowired
    private CreateGameUsecase createGameUsecase;

    @Autowired
    private JoinGameUsecase joinGameUsecase;

    @PostMapping("createGame/{playerId}")
    public GamePresentation createGame(@PathVariable String playerId) throws IOException {
        return createGameUsecase.createGame(playerId);
    }

    @PostMapping("joinGame/{gameId}/{playerId}")
    public void joinGame(@PathVariable String gameId, @PathVariable String playerId) throws IOException {
        joinGameUsecase.joinGame(gameId, playerId);
    }

    @PostMapping("gameData/{gameId}")
    public GamePresentation getGameData(@PathVariable String gameId) throws IOException {
        GamePresentation gamePresentation = listGamesSocketHandler.getGameData(gameId).orElseThrow(
                () -> new GameNotFoundException(String.format("game not found : %s", gameId))
        );
        return gamePresentation;
    }
}
