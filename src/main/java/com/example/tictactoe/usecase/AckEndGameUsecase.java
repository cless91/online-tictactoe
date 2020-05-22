package com.example.tictactoe.usecase;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.GameRepository;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.example.tictactoe.infra.SingleGameSocketHandler;
import com.example.tictactoe.presentation.GamePresentation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class AckEndGameUsecase {
    private final ListGamesSocketHandler listGamesSocketHandler;

    public void ackEndGame(String gameId, String playerId) throws IOException {
        System.out.println("coucou AckEndGameUsecase");
    }
}