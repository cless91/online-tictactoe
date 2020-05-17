package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameRepository;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.example.tictactoe.infra.SingleGameSocketHandler;
import com.example.tictactoe.presentation.GamePresentation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JoinGameUsecase {
    private final GameRepository gameRepository;
    private final ListGamesSocketHandler listGamesSocketHandler;
    private final SingleGameSocketHandler singleGameSocketHandler;

    public void joinGame(String gameId, String playerId) throws IOException {
        Game game = gameRepository.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException("unknown game id"));
        game.join(new Player(playerId));
        GamePresentation gamePresentation = GamePresentation.fromGame(game);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameUpdated");
        data.put("game", gamePresentation);
        listGamesSocketHandler.broadcast(data);
        singleGameSocketHandler.broadcast(gameId,data);
    }
}