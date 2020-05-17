package com.example.tictactoe.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameApplication {
//    private static GameApplication instance;
    private ArrayList<Game> games = new ArrayList<>();

    public GameApplication() {
    }

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

//    public static GameApplication getInstance(){
//        if(instance == null){
//            instance = new GameApplication();
//        }
//
//        return new GameApplication();
//    }
}
