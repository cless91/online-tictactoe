package com.example.tictactoe.infra;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.gameending.GameEnding;
import com.example.tictactoe.usecase.GameEndingRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameEndingRepositoryInMemory implements GameEndingRepository {

    private Map<String, GameEnding> gameEndings = new HashMap<>();

    @Override
    public Optional<GameEnding> getById(String gameId) {
        return Optional.ofNullable(gameEndings.get(gameId));
    }

    @Override
    public void save(GameEnding gameEnding) {
        gameEndings.put(gameEnding.getGameId(), gameEnding);
    }

    @Override
    public void delete(GameEnding gameEnding) {
        gameEndings.remove(gameEnding.getGameId());
    }
}
