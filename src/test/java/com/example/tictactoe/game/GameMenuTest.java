package com.example.tictactoe.game;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameMenuTest {
    @Test
    void listGames_inNewlyLaunchedServer_shouldBeEmpty() {
        GameMenu gameMenu = new GameMenu();
        assertThat(gameMenu.listGames()).isEmpty();
    }

    @Test
    void givenNoGames_whenCreateNewGame_thenThereIsOneGame_andItsCreatorIsPlayer1() {
        GameMenu gameMenu = new GameMenu();
        Player player1 = new Player("player1");
        gameMenu.createNewGame(player1);
        List<Game> games = gameMenu.listGames();
        assertThat(games).hasSize(1);
        Game game = games.get(0);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game).extracting(Game::getCreator).isEqualTo(player1);
        });
    }
}