package com.example.tictactoe.game;

import com.example.tictactoe.infra.InMemoryGameRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameMenuTest {
    @Test
    void listGames_inNewlyLaunchedServer_shouldBeEmpty() {
        InMemoryGameRepository gameMenu = new InMemoryGameRepository();
        assertThat(gameMenu.listGames()).isEmpty();
    }

    @Test
    void givenNoGames_whenCreateNewGame_thenThereIsOneGame_andItsCreatorIsPlayer1() {
        GameRepository gameRepository = new InMemoryGameRepository();
        GameFactory gameMenu = new GameFactory(gameRepository);
        Player player1 = new Player("player1");
        gameMenu.createNewGame(player1);
        List<Game> games = gameRepository.listGames();
        assertThat(games).hasSize(1);
        Game game = games.get(0);
        assertThat(game).extracting(Game::getCreator).isEqualTo(player1);
    }

    @Test
    void givenNewlyCreatedGameByPlayer_whenPlayer2JoinsTheGame_thenThereIsStillOnlyOneGame_andItsCreatorIsPlayer1_andItHasTwoPlayers() {
        //GIVEN
        GameRepository gameRepository = new InMemoryGameRepository();
        GameFactory gameFactory = new GameFactory(gameRepository);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game newGame = gameFactory.createNewGame(player1);

        //WHEN
        newGame.join(player2);

        //THEN
        List<Game> games = gameRepository.listGames();
        assertThat(games).hasSize(1);
        Game game = games.get(0);
        assertThat(game).extracting(Game::getCreator).isEqualTo(player1);
        assertThat(game.listPlayers()).usingElementComparatorOnFields("id").containsExactlyInAnyOrder(player1, player2);
    }

    @Test
    void givenAGameWithTwoPlayer_noNewPlayerCanJoin() {
        //GIVEN
        GameRepository gameRepository = new InMemoryGameRepository();
        GameFactory gameFactory = new GameFactory(gameRepository);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game newGame = gameFactory.createNewGame(player1);
        newGame.join(player2);

        //WHEN
        Player player3 = new Player("player3");
        Assertions.assertThatThrownBy(() -> newGame.join(player3))
                .isInstanceOf(GameJoinException.class)
                .hasMessageContaining("a tictactoe game can only contain 2 players");
    }

    @Test
    void givenAGameCreatedByPlayer1_thenPlayer1CannotJoinItsOwnGame() {
        //GIVEN
        GameRepository gameRepository = new InMemoryGameRepository();
        GameFactory gameFactory = new GameFactory(gameRepository);
        Player player1 = new Player("player1");
        Game newGame = gameFactory.createNewGame(player1);

        //WHEN
        Assertions.assertThatThrownBy(() -> newGame.join(player1))
                .isInstanceOf(GameJoinException.class)
                .hasMessageContaining("the creator of the game cannot join its own game");
    }
}