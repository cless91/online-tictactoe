package com.example.tictactoe.game;

import java.util.Arrays;

import static com.example.tictactoe.game.MARK.*;

public class Grid {
    private MARK[] marks;

    private Grid() {
        marks = new MARK[]{
                NONE, NONE, NONE,
                NONE, NONE, NONE,
                NONE, NONE, NONE
        };
    }

    public Grid(MARK[] marks) {
        this.marks = marks;
    }

    public static Grid newEmptyGrid() {
        return new Grid();
    }

    public static Grid newGrid(MARK[] tiles) {
        return new Grid(tiles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        return Arrays.equals(marks, grid.marks);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(marks);
    }

    public void mark(int xCoord, int yCoord, MARK tile) {
        marks[xCoord*3+yCoord] = tile;
    }
}
