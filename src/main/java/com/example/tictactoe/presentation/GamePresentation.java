package com.example.tictactoe.presentation;

import com.example.tictactoe.game.GAME_STATE;
import com.example.tictactoe.game.Game;
import com.example.tictactoe.game.Player;

public class GamePresentation {
    public String id;
    public String title;
    public String creator;
    public int nbOfPlayers;
    public boolean isJoinable;
    public GAME_STATE gameState;
    public String otherPlayer;

    public static GamePresentation fromGame(Game game){
        GamePresentation gamePresentation = new GamePresentation();
        gamePresentation.id = game.getId();
        gamePresentation.title = "game-"+game.getId();
        gamePresentation.creator = game.getCreator().getId();
        gamePresentation.nbOfPlayers = game.listPlayers().size();
        gamePresentation.isJoinable = game.listPlayers().size() < 2 ;
        gamePresentation.gameState = game.getGameState();
        gamePresentation.otherPlayer = game.getOtherPlayer().map(Player::getId).orElse(null);
        return gamePresentation;
    }
}
