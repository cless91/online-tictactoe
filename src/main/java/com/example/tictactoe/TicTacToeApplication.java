package com.example.tictactoe;

import com.example.tictactoe.game.GameFactory;
import com.example.tictactoe.game.GameRepository;
import com.example.tictactoe.infra.CreateGameUsecase;
import com.example.tictactoe.infra.InMemoryGameRepository;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicTacToeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApplication.class, args);
    }

    @Bean
    public GameFactory gameFactory(GameRepository gameRepository) {
        return new GameFactory(gameRepository);
    }

    @Bean
    public GameRepository gameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public CreateGameUsecase createGameUsecase(GameFactory gameFactory, ListGamesSocketHandler socketHandler) {
        return new CreateGameUsecase(gameFactory, socketHandler);
    }

    @Bean
    public ListGamesSocketHandler listGamesSocketHandler(GameRepository gameRepository,
                                                         ObjectMapper objectMapper) {
        return new ListGamesSocketHandler(gameRepository, objectMapper);
    }

}
