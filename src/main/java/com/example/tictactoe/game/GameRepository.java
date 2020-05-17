package com.example.tictactoe.game;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    List<Game> listGames();

    Optional<Game> getGameById(String gameId);

    Game save(Game game);
}
