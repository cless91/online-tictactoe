package com.example.tictactoe.presentation;

import com.example.tictactoe.entity.GAME_STATE;
import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.Player;

import java.util.Optional;

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

    public static GamePresentation fromGame(Game game){
        GamePresentation gamePresentation = new GamePresentation();
        gamePresentation.id = game.getId();
        gamePresentation.title = "game-"+game.getId();
        gamePresentation.creator = game.getCreator().getId();
        gamePresentation.nbOfPlayers = game.listPlayers().size();
        gamePresentation.isJoinable = game.listPlayers().size() < 2 ;
        gamePresentation.gameState = game.getGameState();
        gamePresentation.otherPlayer = game.getOtherPlayer().map(Player::getId).orElse(null);
        gamePresentation.playerX = Optional.ofNullable(game.getPlayerX()).map(Player::getId).orElse(null);
        gamePresentation.playerO = Optional.ofNullable(game.getPlayerO()).map(Player::getId).orElse(null);
        gamePresentation.currentPlayer = Optional.ofNullable(game.getCurrentPlayer()).map(Player::getId).orElse(null);
        return gamePresentation;
    }
}
