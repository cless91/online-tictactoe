package com.example.tictactoe.game;

public class WrongPlacementException extends RuntimeException {
    public WrongPlacementException(String message) {
        super(message);
    }
}
