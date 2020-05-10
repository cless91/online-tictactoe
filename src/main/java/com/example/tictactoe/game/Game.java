package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.tictactoe.game.GAME_STATE.*;
import static com.example.tictactoe.game.GAME_STATE.CREATED;
import static com.example.tictactoe.game.MARK.*;

public class Game {
    private Player creator;
    private Player otherPlayer;
    private Player playerX;
    private Player playerO;
    private Grid grid;
    private Player currentPlayer;
    private MARK currentmark;
    private GAME_STATE gameState;

    public Game(Player player) {
        creator = player;
        gameState = CREATED;
    }

    public Player getCreator() {
        return creator;
    }

    public void join(Player otherPlayer) {
        if (this.otherPlayer != null) {
            throw new GameJoinException(String.format("a tictactoe game can only contain 2 players. A third one tried to join: %s", otherPlayer));
        } else if (otherPlayer.equals(this.creator)) {
            throw new GameJoinException(String.format("the creator of the game cannot join its own game. Player: %s", otherPlayer));
        }
        this.otherPlayer = otherPlayer;
        this.gameState = STARTING;
    }

    public List<Player> listPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        if (creator != null) {
            players.add(creator);
        }
        if (otherPlayer != null) {
            players.add(otherPlayer);
        }
        return players;
    }

    public void start() {
        gameState = ONGOING;
        if (new Random().nextDouble() < 0.5) {
            playerX = creator;
            playerO = otherPlayer;
        } else {
            playerO = creator;
            playerX = otherPlayer;
        }
        grid = Grid.newEmptyGrid();
        currentPlayer = playerX;
        currentmark = X;
    }

    public void play(Player player, int xCoord, int yCoord) {
        if (player != currentPlayer) {
            throw new WrongPlayerException(String.format("player %s is not allowed to play now", player));
        }
        grid.mark(xCoord, yCoord, currentmark);
        switchCurrentPlayer();
    }

    private void switchCurrentPlayer() {
        if (currentPlayer == playerX) {
            currentPlayer = playerO;
            currentmark = O;
        } else {
            currentPlayer = playerX;
            currentmark = X;
        }
    }

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public Grid getGrid() {
        return grid;
    }
}
