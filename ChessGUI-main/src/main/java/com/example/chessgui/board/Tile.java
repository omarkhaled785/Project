package com.example.chessgui.board;
import com.example.chessgui.pieces.Piece;

import java.util.Map;

/**
 * This class represents a single tile on the chess board.
 * */
public abstract class Tile {

    int tileCoordinate;

    Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    //An empty tile map with integer key.
    private static final Map<Integer, emptyTile> EMPTY_TILE_MAP = createPossibleEmptyTiles();
    private static Map<Integer, emptyTile> createPossibleEmptyTiles(){
        for (int i = 0; i < BoardStructure.NUM_OF_TILES; i++){
            EMPTY_TILE_MAP.put(i,new emptyTile(i));
        }
        return EMPTY_TILE_MAP;
    }

    //Create a tile
    public static Tile createTile(int tileCoordinate, Piece piece){
        return piece != null ? new occupiedTile(tileCoordinate, piece) : EMPTY_TILE_MAP.get(tileCoordinate);
    }

}
