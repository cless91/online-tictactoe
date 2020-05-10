package com.example.tictactoe.game;

public class GameOverException extends RuntimeException {
    public GameOverException(String message) {
        super(message);
    }
}
