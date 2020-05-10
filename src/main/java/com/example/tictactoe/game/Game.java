package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.tictactoe.game.TILE.*;

public class Game {
    private Player creator;
    private Player otherPlayer;
    private Player playerX;
    private Player playerO;
    private Grid grid;
    private Player currentPlayer;

    public Game(Player player) {
        creator = player;
    }

    public Player getCreator() {
        return creator;
    }

    public void join(Player otherPlayer) {
        if(this.otherPlayer != null){
            throw new GameJoinException(String.format("a tictactoe game can only contain 2 players. A third one tried to join: %s", otherPlayer));
        } else if(otherPlayer.equals(this.creator)){
            throw new GameJoinException(String.format("the creator of the game cannot join its own game. Player: %s",otherPlayer));
        }
        this.otherPlayer = otherPlayer;
    }

    public List<Player> listPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        if(creator != null){
            players.add(creator);
        }
        if(otherPlayer != null){
            players.add(otherPlayer);
        }
        return players;
    }

    public void start() {
        if(new Random().nextDouble() <0.5){
            playerX = creator;
            playerO = otherPlayer;
        } else {
            playerO = creator;
            playerX = otherPlayer;
        }
        grid = Grid.newEmptyGrid();
        currentPlayer = playerX;
    }

    public void play(Player player, int xCoord, int yCoord) {
        if(player != currentPlayer){
            throw new WrongPlayerException(String.format("player %s is not allowed to play now",player));
        }
        grid.mark(xCoord,yCoord, X);
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
