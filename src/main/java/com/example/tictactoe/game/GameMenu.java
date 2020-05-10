package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;

public class GameMenu {

    private ArrayList<Game> games = new ArrayList<>();

    public List<Game> listGames() {
        return games;
    }

    public void createNewGame(Player player) {
        games.add(new Game(player));
    }
}
