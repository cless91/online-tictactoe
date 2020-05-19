package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameRepository;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.infra.SingleGameSocketHandler;
import com.example.tictactoe.presentation.GamePresentation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PlayUsecase {
    private final GameRepository gameRepository;
    private final SingleGameSocketHandler singleGameSocketHandler;

    public void play(String gameId, String playerId, int x, int y) throws IOException {
        Game game = gameRepository.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException("unknown game id"));
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            throw new IllegalArgumentException(playerId + " is not the current player");
        }

        game.play(currentPlayer, x, y);

        GamePresentation gamePresentation = GamePresentation.fromGame(game);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameUpdated");
        data.put("game", gamePresentation);
        singleGameSocketHandler.broadcast(gameId, data);
    }
}
