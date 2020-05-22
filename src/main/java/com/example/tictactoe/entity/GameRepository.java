package com.example.tictactoe.entity;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    List<Game> listGames();
    Optional<Game> getGameById(String gameId);
    void save(Game game);
    void delete(Game game);
}
