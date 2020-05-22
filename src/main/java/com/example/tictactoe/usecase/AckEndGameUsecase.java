package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameRepository;
import com.example.tictactoe.entity.gameending.GameEnding;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AckEndGameUsecase {
    private final ListGamesSocketHandler listGamesSocketHandler;
    private final GameRepository gameRepository;
    private final GameEndingRepository gameEndingRepository;
    private String gameNotFoundErrorMessage;

    public AckEndGameUsecase(ListGamesSocketHandler listGamesSocketHandler, GameRepository gameRepository, GameEndingRepository gameEndingRepository) {
        this.listGamesSocketHandler = listGamesSocketHandler;
        this.gameRepository = gameRepository;
        this.gameEndingRepository = gameEndingRepository;
    }

    public void ackEndGame(String gameId, String playerId) throws IOException {
        gameNotFoundErrorMessage = String.format("%s game does not exist", gameId);
        GameEnding gameEnding = getGameEnding(gameId);
        gameEnding.ack(playerId);
        if(gameEnding.allPlayersHaveAck()){
            deleteGameAndNotify(gameId, gameNotFoundErrorMessage, gameEnding);
        }else{
            gameEndingRepository.save(gameEnding);
        }
    }

    private GameEnding getGameEnding(String gameId) {
        GameEnding gameEnding;
        Optional<GameEnding> gameEndingOpt = gameEndingRepository.getById(gameId);
        if(!gameEndingOpt.isPresent()){
            Game game = gameRepository.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException(gameNotFoundErrorMessage));
            gameEnding = GameEnding.from(game);
        }else {
            gameEnding = gameEndingOpt.get();
        }
        return gameEnding;
    }

    private void deleteGameAndNotify(String gameId, String gameNotFoundErrorMessage, GameEnding gameEnding) throws IOException {
        gameEndingRepository.delete(gameEnding);
        Game game = gameRepository.getGameById(gameId).orElseThrow(() -> new IllegalArgumentException(gameNotFoundErrorMessage));
        gameRepository.delete(game);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("opCode", "gameDeleted");
        data.put("game", game);
        listGamesSocketHandler.broadcast(data);
    }

}