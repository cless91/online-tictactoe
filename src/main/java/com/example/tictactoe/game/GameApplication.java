package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameApplication {
    private ArrayList<Game> games = new ArrayList<>();

    public List<Game> listGames() {
        return games;
    }

    public Game createNewGame(Player player) {
        Game game = new Game(player);
        games.add(game);
        return game;
    }

    public Optional<Game> getGameById(String gameId) {
        return games.stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst();
    }
}
