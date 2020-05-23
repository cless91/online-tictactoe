package com.example.tictactoe.presentation;

import com.example.tictactoe.entity.GAME_STATE;
import com.example.tictactoe.entity.MARK;

import java.util.ArrayList;
import java.util.List;

public class GamePresentation {
    public String id;
    public String title;
    public String creator;
    public int nbOfPlayers;
    public boolean isJoinable;
    public GAME_STATE gameState;
    public String otherPlayer;
    public String playerX;
    public String playerO;
    public String currentPlayer;
    public MARK[] grid;
    public List<String> playerAcks = new ArrayList<>();

}
