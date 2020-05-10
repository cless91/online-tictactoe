package com.example.tictactoe.game;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static com.example.tictactoe.game.MARK.*;
import static com.example.tictactoe.game.MARK.NONE;

class GameTest {
    @Test
    void givenGameWithTwoPlayersHavingJoined_whenStartTheGame_thenPlayersCrossAndCirclesAreAssigned_andTheGridIsInitialized() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Player playerX = game.getPlayerX();
        Player playerO = game.getPlayerO();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(playerX).isIn(player1, player2);
            softAssertions.assertThat(playerO).isIn(player1, player2);
            softAssertions.assertThat(playerX).isNotEqualTo(playerO);
        });
    }

    @Test
    void givenNewGame_whenStart_thenTheGridIsInitialized() {
        Grid expectedGrid = Grid.newGrid(new MARK[]{
                NONE, NONE, NONE,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        });

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Assertions.assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }

    @Test
    void givenNewGame_onlyPlayerXCanStart() {
        Grid expectedGrid = Grid.newGrid(new MARK[]{
                NONE, X, NONE,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        });

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Player playerX = game.getPlayerX();
        game.play(playerX, 0, 1);
        Assertions.assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }

    @Test
    void givenNewGame_whenPlayer0Plays_thenError() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Player playerO = game.getPlayerO();
        Assertions.assertThatThrownBy(() -> game.play(playerO, 0, 1)).isInstanceOf(WrongPlayerException.class);
    }

    @Test
    void givenNewGame_whenPlayerXThenPlayer0_thenOK() {
        Grid expectedGrid = Grid.newGrid(new MARK[]{
                NONE, X, O,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        });

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Player playerX = game.getPlayerX();
        Player playerO = game.getPlayerO();
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 2);
        Assertions.assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }
}