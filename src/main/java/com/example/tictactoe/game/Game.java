package com.example.tictactoe.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player creator;
    private Player otherPlayer;

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
}
