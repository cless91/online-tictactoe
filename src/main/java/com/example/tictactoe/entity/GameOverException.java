package com.example.tictactoe.entity;

public class GameOverException extends RuntimeException {
    public GameOverException(String message) {
        super(message);
    }
}
