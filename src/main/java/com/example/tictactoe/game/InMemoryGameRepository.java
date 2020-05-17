package com.example.tictactoe.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryGameRepository implements GameRepository {
    private ArrayList<Game> games = new ArrayList<>();

    public InMemoryGameRepository() {
    }

    @Override
    public List<Game> listGames() {
        return games;
    }



    @Override
    public Optional<Game> getGameById(String gameId) {
        return games.stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst();
    }

    @Override
    public Game save(Game game) {
        games.add(game);
        return game;
    }
}
