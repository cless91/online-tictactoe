package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.gameending.GameEnding;

import java.util.Optional;

public interface GameEndingRepository {
    Optional<GameEnding> getById(String gameId);
    void save(GameEnding gameEnding);
    void delete(GameEnding gameEnding);
}
