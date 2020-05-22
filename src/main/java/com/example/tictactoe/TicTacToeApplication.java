package com.example.tictactoe;

import com.example.tictactoe.entity.GameFactory;
import com.example.tictactoe.entity.GameRepository;
import com.example.tictactoe.infra.GameEndingRepositoryInMemory;
import com.example.tictactoe.infra.SingleGameSocketHandler;
import com.example.tictactoe.usecase.*;
import com.example.tictactoe.infra.InMemoryGameRepository;
import com.example.tictactoe.infra.ListGamesSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
    public JoinGameUsecase joinGameUsecase(GameRepository gameRepository,
                                           ListGamesSocketHandler listGamesSocketHandler,
                                           SingleGameSocketHandler singleGameSocketHandler) {
        return new JoinGameUsecase(gameRepository,
                listGamesSocketHandler,
                singleGameSocketHandler);
    }

    @Bean
    public StartGameUsecase startGameUsecase(GameRepository gameRepository, SingleGameSocketHandler singleGameSocketHandler) {
        return new StartGameUsecase(gameRepository, singleGameSocketHandler);
    }

    @Bean
    public PlayUsecase playUsecase(GameRepository gameRepository, SingleGameSocketHandler singleGameSocketHandler) {
        return new PlayUsecase(gameRepository, singleGameSocketHandler);
    }

    @Bean
    public GameEndingRepository gameEndingRepository(){
        return new GameEndingRepositoryInMemory();
    }

    @Bean
    public AckEndGameUsecase ackEndGameUsecase(ListGamesSocketHandler listGamesSocketHandler,
                                               GameRepository gameRepository,
                                               GameEndingRepository gameEndingRepository) {
        return new AckEndGameUsecase(listGamesSocketHandler, gameRepository, gameEndingRepository);
    }

}
