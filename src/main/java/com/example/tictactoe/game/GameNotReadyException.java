package com.example.tictactoe.game;

public class GameNotReadyException extends RuntimeException {
    public GameNotReadyException(String message) {
        super(message);
    }
}
