package com.example.tictactoe.entity;

public class WrongPlayerException extends RuntimeException {
    public WrongPlayerException(String message) {
        super(message);
    }
}
