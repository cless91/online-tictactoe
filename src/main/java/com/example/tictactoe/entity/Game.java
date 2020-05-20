package com.example.tictactoe.entity;

import java.util.*;

import static com.example.tictactoe.entity.GAME_STATE.*;
import static com.example.tictactoe.entity.GAME_STATE.CREATED;
import static com.example.tictactoe.entity.MARK.*;

public class Game {
    private String id;
    private Player creator;
    private Player otherPlayer;
    private Player playerX;
    private Player playerO;
    private Grid grid;
    private Player currentPlayer;
    private MARK currentmark;
    private GAME_STATE gameState;
    private Set<GAME_STATE> gameOverStates = new HashSet<>(Arrays.asList(X_WINS, O_WINS, DRAW));
    private Set<GAME_STATE> gameNotStartedStates = new HashSet<>(Arrays.asList(CREATED, READY));

    public Game(Player player) {
        creator = player;
        gameState = CREATED;
        id = UUID.randomUUID().toString();
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
        this.gameState = READY;
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
        if(otherPlayer == null){
            throw new GameNotReadyException(String.format("cannot start the game %s. Another player must join first.", id));
        }
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
        gameState = STARTED;
    }

    public void play(Player player, int xCoord, int yCoord) {
        if (gameNotStartedStates.contains(gameState)) {
            throw new GameNotStartedException(String.format("player %s tried to play but the game has not started yet.", player));
        }else if (gameOverStates.contains(gameState)) {
            throw new GameOverException(String.format("player %s tried to play but the game is over.", player));
        }else if (player != currentPlayer) {
            throw new WrongPlayerException(String.format("player %s is not allowed to play now", player));
        }
        grid.mark(xCoord, yCoord, currentmark);
        updateGameEndState();
        switchCurrentPlayer();
    }

    private void updateGameEndState() {
        checkWinner();
    }

    private void checkWinner() {
        MARK markDiagonalTopLeft = grid.markAt(0, 0);
        boolean winsDiagonalTopLeft = !markDiagonalTopLeft.equals(NONE);
        MARK markDiagonalTopRight = grid.markAt(0, 2);
        boolean winsDiagonalTopRight = !markDiagonalTopRight.equals(NONE);
        MARK markRow;
        boolean winsRow;
        MARK markColumn;
        boolean winsColumn;

        for (int i = 0; i < 3; i++) {
            winsDiagonalTopLeft = winsDiagonalTopLeft && markDiagonalTopLeft.equals(grid.markAt(i, i));
            winsDiagonalTopRight = winsDiagonalTopRight && markDiagonalTopRight.equals(grid.markAt(i, 2 - i));

            markRow = grid.markAt(i, 0);
            winsRow = !markRow.equals(NONE);
            markColumn = grid.markAt(0, i);
            winsColumn = !markColumn.equals(NONE);

            for (int j = 0; j < 3; j++) {
                winsRow = winsRow && markRow.equals(grid.markAt(i, j));
                winsColumn = winsColumn && markColumn.equals(grid.markAt(j, i));
            }
            checkWinnerOnRowOrColumn(markRow, winsRow, markColumn, winsColumn);
        }
        if ((markDiagonalTopLeft.equals(X) && winsDiagonalTopLeft) || (markDiagonalTopRight.equals(X) && winsDiagonalTopRight)) {
            gameState = X_WINS;
        } else if ((markDiagonalTopLeft.equals(O) && winsDiagonalTopLeft) || (markDiagonalTopRight.equals(O) && winsDiagonalTopRight)) {
            gameState = O_WINS;
        }

        checkDraw();
    }

    private void checkDraw() {
        if(Arrays.stream(grid.getMarks()).noneMatch(mark -> mark.equals(NONE))){
            gameState = DRAW;
        }
    }

    private void checkWinnerOnRowOrColumn(MARK markRow, boolean winsRow, MARK markColumn, boolean winsColumn) {
        if ((markRow.equals(X) && winsRow) || (markColumn.equals(X) && winsColumn)) {
            gameState = X_WINS;
        } else if ((markRow.equals(O) && winsRow) || (markColumn.equals(O) && winsColumn)) {
            gameState = O_WINS;
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

    public String getId() {
        return id;
    }

    public Optional<Player> getOtherPlayer() {
        return Optional.ofNullable(otherPlayer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
