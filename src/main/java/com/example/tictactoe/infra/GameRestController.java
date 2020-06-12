package com.example.tictactoe.infra;

import com.example.tictactoe.presentation.GamePresentation;
import com.example.tictactoe.usecase.AckEndGameUsecase;
import com.example.tictactoe.usecase.CreateGameUsecase;
import com.example.tictactoe.usecase.JoinGameUsecase;
import com.example.tictactoe.usecase.PlayUsecase;
import com.example.tictactoe.usecase.StartGameUsecase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
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

    @GetMapping("gameData/{gameId}")
    public GamePresentation getGameData(@PathVariable String gameId) throws IOException {
        GamePresentation gamePresentation = listGamesSocketHandler.getGameData(gameId).orElseThrow(
                () -> new GameNotFoundException(String.format("game not found : %s", gameId))
        );
        return gamePresentation;
    }

    @GetMapping("listGames")
    public List<GamePresentation> getGameDatas() throws IOException {
        return listGamesSocketHandler.listGames();
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
