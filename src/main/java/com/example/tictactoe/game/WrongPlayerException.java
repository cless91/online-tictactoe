package com.example.tictactoe.game;

public class WrongPlayerException extends RuntimeException {
    public WrongPlayerException(String message) {
        super(message);
    }
}
