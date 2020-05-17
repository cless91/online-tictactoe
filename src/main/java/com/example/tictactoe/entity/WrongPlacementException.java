package com.example.tictactoe.entity;

public class WrongPlacementException extends RuntimeException {
    public WrongPlacementException(String message) {
        super(message);
    }
}
