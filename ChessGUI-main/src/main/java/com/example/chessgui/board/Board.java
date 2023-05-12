package com.example.chessgui.board;

import com.example.chessgui.PieceColor;
import com.example.chessgui.Player.Player;
import com.example.chessgui.Player.BlackPlayer;
import com.example.chessgui.Player.WhitePlayer;
import com.example.chessgui.pieces.Piece;
import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, PieceColor.WHITE);
        this.blackPieces= calculateActivePieces(this.gameBoard, PieceColor.BLACK);

        List<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        List<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = null;  //for now TODO
    }

    public List<Piece> getBlackPieces (){
        return this.blackPieces;
    }

    public List<Piece> getWhitePieces (){
        return this.whitePieces;
    }

    private List<Move> calculateLegalMoves(List<Piece> pieces) {
        List<Move> legalMoves = new ArrayList<>();
        for (Piece piece : pieces){
            legalMoves.addAll(piece.calcLegalMoves(this));
        }
        return legalMoves;
    }

    private List<Piece> calculateActivePieces(List<Tile> gameBoard, PieceColor pieceColor){
        final List<Piece> activePieces = new ArrayList<>();
        for (Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceColor() == pieceColor) {
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardStructure.NUM_OF_TILES];
        for (int i = 0; i < BoardStructure.NUM_OF_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return Arrays.asList(tiles);
    }


    public static Board createStandardBoard() {
        /*TODO*/
        return null;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    //using the builder design pattern.
    public static class Builder{
        Map<Integer, Piece> boardConfig;
        PieceColor nextMoveMaker;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(final PieceColor nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
