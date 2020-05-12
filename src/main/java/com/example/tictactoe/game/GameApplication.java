package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;

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
}
