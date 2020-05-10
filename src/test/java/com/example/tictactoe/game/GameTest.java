package com.example.tictactoe.game;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static com.example.tictactoe.game.TILE.*;

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
        Grid expectedGrid = Grid.newGrid(new TILE[]{
                EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY
        });

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1);
        game.join(player2);
        game.start();

        Assertions.assertThat(game.getGrid()).isEqualTo(expectedGrid);
    }
}