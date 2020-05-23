package com.example.tictactoe.entity.gameending;

import com.example.tictactoe.entity.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameEnding {
    private String gameId;
    private String creatorId;
    private String otherPlayerId;
    private boolean creatorAck = false;
    private boolean otherPlayerAck = false;

    public GameEnding() {
    }

    public static GameEnding from(Game game){
        GameEnding gameEnding = new GameEnding();
        gameEnding.gameId = game.getId();
        gameEnding.creatorId = game.getCreator().getId();
        gameEnding.otherPlayerId = game.getOtherPlayer()
                .orElseThrow(() -> new IllegalStateException("creating a new gameEnding while \"otherPlayer\" is not defined"))
                .getId();
        return gameEnding;
    }

    public void ack(String playerId){
        if(!Objects.equals(creatorId, playerId) && !Objects.equals(otherPlayerId, playerId)){
            throw new IllegalArgumentException(playerId+" is not a player of the game "+gameId);
        }else if(Objects.equals(creatorId, playerId)){
            creatorAck = true;
        }else {
            otherPlayerAck = true;
        }
    }

    public List<String> getAcksPlayerIds(){
        List<String> acksPlayerIds = new ArrayList<>();
        if(creatorAck) acksPlayerIds.add(creatorId);
        if(otherPlayerAck) acksPlayerIds.add(otherPlayerId);
        return acksPlayerIds;
    }

    public boolean allPlayersHaveAck(){
        return creatorAck && otherPlayerAck;
    }

    public String getGameId() {
        return gameId;
    }
}
