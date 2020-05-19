package com.example.tictactoe.infra;

import com.example.tictactoe.presentation.GamePresentation;
import com.example.tictactoe.usecase.CreateGameUsecase;
import com.example.tictactoe.usecase.JoinGameUsecase;
import com.example.tictactoe.usecase.StartGameUsecase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private StartGameUsecase startGameUsecase;

    @RequestMapping("/")
    public ModelAndView homePage(@CookieValue(value = "playerId", required = false) String playerId,
                                 HttpServletResponse response) throws IOException {
        System.out.println();
        if(StringUtils.isBlank(playerId)) {
            playerId = String.format("anon-player-%s", UUID.randomUUID().toString());
            response.addCookie(new Cookie("playerId", playerId));
        }
        return new ModelAndView("index");
    }

    @PostMapping("createGame/{playerId}")
    public GamePresentation createGame(@PathVariable String playerId) throws IOException {
        return createGameUsecase.createGame(playerId);
    }

    @PostMapping("joinGame/{gameId}/{playerId}")
    public void joinGame(@PathVariable String gameId, @PathVariable String playerId) throws IOException {
        joinGameUsecase.joinGame(gameId, playerId);
    }

    @PostMapping("startGame/{gameId}")
    public void startGame(@PathVariable String gameId) throws IOException {
        startGameUsecase.startGame(gameId);
    }

    @PostMapping("gameData/{gameId}")
    public GamePresentation getGameData(@PathVariable String gameId) throws IOException {
        GamePresentation gamePresentation = listGamesSocketHandler.getGameData(gameId).orElseThrow(
                () -> new GameNotFoundException(String.format("game not found : %s", gameId))
        );
        return gamePresentation;
    }
}
