package com.example.tictactoe.game;

public class Game {
    private Player creator;

    public Game(Player player) {

        creator = player;
    }

    public Player getCreator() {
        return creator;
    }
}
