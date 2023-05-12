package com.example.chessgui.Player;

import com.example.chessgui.PieceColor;
import com.example.chessgui.board.Board;
import com.example.chessgui.board.Move;
import com.example.chessgui.pieces.Piece;
import com.example.chessgui.PieceColor;

import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board,
                       List<Move> whiteStandardLegalMoves,
                       List<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public PieceColor getPlayerColor() {
        return PieceColor.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
