package com.example.tictactoe.entity;

public class GameNotReadyException extends RuntimeException {
    public GameNotReadyException(String message) {
        super(message);
    }
}
