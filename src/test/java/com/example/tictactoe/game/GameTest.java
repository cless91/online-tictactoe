package com.example.tictactoe.game;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.tictactoe.game.GAME_STATE.O_WINS;
import static com.example.tictactoe.game.GAME_STATE.X_WINS;
import static com.example.tictactoe.game.MARK.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameTest {

    private Player player1 = new Player("player1");
    private Player player2 = new Player("player2");
    private Game game;
    private Player playerX;
    private Player playerO;

    @BeforeEach
    void setUp() {
        game = new Game(player1);
        game.join(player2);
        game.start();
        playerX = game.getPlayerX();
        playerO = game.getPlayerO();
    }

    @Test
    void givenGameWithTwoPlayersHavingJoined_whenStartTheGame_thenPlayersCrossAndCirclesAreAssigned_andTheGridIsInitialized() {
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
        assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }

    @Test
    void givenNewGame_onlyPlayerXCanStart() {
        Grid expectedGrid = Grid.newGrid(new MARK[]{
                NONE, X, NONE,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        });

        game.play(playerX, 0, 1);
        assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }

    @Test
    void givenNewGame_whenPlayerOPlays_thenError() {
        Player playerO = game.getPlayerO();
        assertThatThrownBy(() -> game.play(playerO, 0, 1)).isInstanceOf(WrongPlayerException.class);
    }

    @Test
    void givenNewGame_whenPlayerXThenPlayerO_thenOK() {
        Grid expectedGrid = Grid.newGrid(new MARK[]{
                NONE, X, O,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        });
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 2);
        assertThat(game.getGrid()).usingRecursiveComparison().isEqualTo(expectedGrid);
    }

    @Test
    void cannotPlayTwiceInSameTile() {
        game.play(playerX, 0, 1);
        assertThatThrownBy(() -> game.play(playerO, 0, 1)).isInstanceOf(WrongPlacementException.class);
    }

    @Test
    void xWinsVertical_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 2);
        game.play(playerX, 1, 1);
        game.play(playerO, 1, 2);
        game.play(playerX, 2, 1);

        //       |     |
        //    -  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    -  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    -  |  X  |  -
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(X_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void oWinsVertical_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 2);
        game.play(playerX, 1, 1);
        game.play(playerO, 1, 2);
        game.play(playerX, 1, 0);
        game.play(playerO, 2, 2);

        //       |     |
        //    -  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    X  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    -  |  -  |  O
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(O_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void xWinsDiagonalTopLeft_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 0);
        game.play(playerO, 0, 1);
        game.play(playerX, 1, 1);
        game.play(playerO, 1, 2);
        game.play(playerX, 2, 2);

        //       |     |
        //    X  |  O  |  -
        //  _____|_____|_____
        //       |     |
        //    -  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    -  |  -  |  X
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(X_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void xWinsDiagonalTopRight_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 2);
        game.play(playerO, 0, 1);
        game.play(playerX, 1, 1);
        game.play(playerO, 1, 2);
        game.play(playerX, 2, 0);

        //       |     |
        //    -  |  O  |  X
        //  _____|_____|_____
        //       |     |
        //    -  |  X  |  O
        //  _____|_____|_____
        //       |     |
        //    X  |  -  |  -
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(X_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void oWinsDiagonalTopLeft_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 0);
        game.play(playerX, 0, 2);
        game.play(playerO, 1, 1);
        game.play(playerX, 1, 2);
        game.play(playerO, 2, 2);

        //       |     |
        //    O  |  X  |  X
        //  _____|_____|_____
        //       |     |
        //    -  |  O  |  X
        //  _____|_____|_____
        //       |     |
        //    -  |  -  |  O
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(O_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void oWinsDiagonalTopRight_andNobodyElseCanPlayAfter() {
        game.play(playerX, 0, 1);
        game.play(playerO, 0, 2);
        game.play(playerX, 0, 0);
        game.play(playerO, 1, 1);
        game.play(playerX, 1, 0);
        game.play(playerO, 2, 0);

        //       |     |
        //    X  |  X  |  -
        //  _____|_____|_____
        //       |     |
        //    X  |  O  |  -
        //  _____|_____|_____
        //       |     |
        //    O  |  -  |  -
        //       |     |

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(game.getGameState()).isEqualTo(O_WINS);
            softAssertions.assertThatThrownBy(() -> game.play(playerO, 0, 0)).isInstanceOf(GameOverException.class);
            softAssertions.assertThatThrownBy(() -> game.play(playerX, 0, 0)).isInstanceOf(GameOverException.class);
        });
    }

    @Test
    void cannotPlayOnAGameThatIsNotStarted() {
        Player p1 = new Player("p1");
        Game game = new Game(p1);
        assertThatThrownBy(() -> game.play(p1,0,0)).isInstanceOf(GameNotStartedException.class);
    }

    @Test
    void cannotStartAGameIfNoOtherPlayerHasJoined() {
        Player p1 = new Player("p1");
        Game game = new Game(p1);
        assertThatThrownBy(game::start).isInstanceOf(GameNotReadyException.class);
    }
}