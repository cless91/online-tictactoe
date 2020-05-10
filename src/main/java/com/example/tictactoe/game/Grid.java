package com.example.tictactoe.game;

import java.util.Arrays;

import static com.example.tictactoe.game.TILE.*;

public class Grid {
    private TILE[] tiles;

    private Grid() {
        tiles = new TILE[]{
                EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY
        };
    }

    public Grid(TILE[] tiles) {
        this.tiles =tiles;
    }

    public static Grid newEmptyGrid() {
        return new Grid();
    }

    public static Grid newGrid(TILE[] tiles) {
        return new Grid(tiles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        return Arrays.equals(tiles, grid.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }
}
