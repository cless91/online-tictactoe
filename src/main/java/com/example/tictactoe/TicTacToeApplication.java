package com.example.tictactoe;

import com.example.tictactoe.game.GameFactory;
import com.example.tictactoe.game.GameRepository;
import com.example.tictactoe.game.InMemoryGameRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicTacToeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeApplication.class, args);
    }

    @Bean
    public GameFactory gameFactory(GameRepository gameRepository){
        return new GameFactory(gameRepository);
    }

    @Bean
    public GameRepository gameRepository(){
        return new InMemoryGameRepository();
    }
}
