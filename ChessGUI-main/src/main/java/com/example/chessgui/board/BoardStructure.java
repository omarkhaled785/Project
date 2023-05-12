package com.example.chessgui.board;

public class BoardStructure {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;

    public static final int NUM_OF_TILES = 64;
    public static final int TILES_PER_ROW = 8;
    public static final int NUM_TILES = 0;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] columns = new boolean[NUM_OF_TILES];
        do {
            columns[columnNumber] = true;
            columnNumber += TILES_PER_ROW;
        } while (columnNumber < NUM_OF_TILES);
        return columns;
    }

    public static boolean isValidCoordinate(int tileCoordinate) {
        return tileCoordinate >= 0 && tileCoordinate < NUM_OF_TILES;
    }
}
