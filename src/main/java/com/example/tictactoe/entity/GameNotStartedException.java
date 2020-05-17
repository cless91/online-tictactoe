package com.example.tictactoe.entity;

public class GameNotStartedException extends RuntimeException {
    public GameNotStartedException(String message) {
        super(message);
    }
}
