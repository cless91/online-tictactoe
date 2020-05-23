package com.example.tictactoe.presentation;

import com.example.tictactoe.entity.Game;
import com.example.tictactoe.entity.Grid;
import com.example.tictactoe.entity.Player;
import com.example.tictactoe.entity.gameending.GameEnding;
import com.example.tictactoe.usecase.GameEndingRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class GamePresentationFactory {
    private final GameEndingRepository gameEndingRepository;

    public GamePresentation fromGame(Game game) {
        GamePresentation gamePresentation = new GamePresentation();
        gamePresentation.id = game.getId();
        gamePresentation.title = "game-" + game.getId();
        gamePresentation.creator = game.getCreator().getId();
        gamePresentation.nbOfPlayers = game.listPlayers().size();
        gamePresentation.isJoinable = game.listPlayers().size() < 2;
        gamePresentation.gameState = game.getGameState();
        gamePresentation.otherPlayer = game.getOtherPlayer().map(Player::getId).orElse(null);
        gamePresentation.playerX = Optional.ofNullable(game.getPlayerX()).map(Player::getId).orElse(null);
        gamePresentation.playerO = Optional.ofNullable(game.getPlayerO()).map(Player::getId).orElse(null);
        gamePresentation.currentPlayer = Optional.ofNullable(game.getCurrentPlayer()).map(Player::getId).orElse(null);
        gamePresentation.grid = Optional.ofNullable(game.getGrid()).map(Grid::getMarks).orElse(null);

        gameEndingRepository.getById(game.getId()).ifPresent(gameEnding -> gamePresentation.playerAcks.addAll(gameEnding.getAcksPlayerIds()));
        return gamePresentation;
    }
}
