package com.example.tictactoe.infra;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void save(Game game) {
        games.add(game);
    }

    @Override
    public void delete(Game game) {
        games.remove(game);
    }
}
