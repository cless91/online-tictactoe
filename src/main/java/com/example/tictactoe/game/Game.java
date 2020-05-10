package com.example.tictactoe.game;

import java.util.*;
import java.util.stream.IntStream;

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
    private Set<GAME_STATE> gameOverStates = new HashSet<>(Arrays.asList(X_WINS,O_WINS, DRAW));

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
        if(gameOverStates.contains(gameState)){
            throw new GameOverException(String.format("player %s tried to play but the game is over.",player));
        }
        if (player != currentPlayer) {
            throw new WrongPlayerException(String.format("player %s is not allowed to play now", player));
        }
        grid.mark(xCoord, yCoord, currentmark);
        checkGameEndState();
        switchCurrentPlayer();
    }

    private void checkGameEndState() {
        for (int i = 0; i < 3; i++) {
            MARK mark_row = grid.markAt(i,0);
            boolean wins_row = !mark_row.equals(NONE);
            MARK mark_column = grid.markAt(0,i);
            boolean wins_column = !mark_column.equals(NONE);
            for (int j = 0; j < 3; j++) {
                wins_row = wins_row && mark_row.equals(grid.markAt(i,j));
                wins_column = wins_column && mark_column.equals(grid.markAt(j,i));
            }

            if((mark_row.equals(X) || mark_column.equals(X)) && (wins_row || wins_column)){
                gameState = X_WINS;
            } else if((mark_row.equals(O) || mark_column.equals(O)) && (wins_row || wins_column)){
                gameState = O_WINS;
            }
        }
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

    public GAME_STATE getGameState() {
        return gameState;
    }
}
