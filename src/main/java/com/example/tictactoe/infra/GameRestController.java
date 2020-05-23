package com.example.tictactoe.infra;

import com.example.tictactoe.presentation.GamePresentation;
import com.example.tictactoe.usecase.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    private PlayUsecase playUsecase;

    @Autowired
    private AckEndGameUsecase ackEndGameUsecase;

    @RequestMapping("/")
    public ModelAndView homePage(@CookieValue(value = "playerId", required = false) String playerId,
                                 HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(playerId)) {
            playerId = String.format("anon-player-%s", UUID.randomUUID().toString());
            response.addCookie(new Cookie("playerId", playerId));
        }
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/ackEndGame/{gameId}/{playerId}", method = RequestMethod.POST)
    public void ackEndGame(@PathVariable String gameId, @PathVariable String playerId) throws IOException {
        ackEndGameUsecase.ackEndGame(gameId, playerId);
    }

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

    @PostMapping("startGame/{gameId}")
    public void startGame(@PathVariable String gameId) throws IOException {
        startGameUsecase.startGame(gameId);
    }

    @PostMapping("play/{gameId}/{playerId}")
    public void play(@PathVariable String gameId, @PathVariable String playerId, @RequestParam int x, @RequestParam int y) throws IOException {
        playUsecase.play(gameId, playerId, x, y);
    }
}
