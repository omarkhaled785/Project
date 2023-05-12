package com.example.chessgui.pieces;

import com.example.chessgui.PieceColor;
import com.example.chessgui.board.Board;
import com.example.chessgui.board.Move;

import java.util.List;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final PieceColor pieceColor;
    protected static boolean isFirstMove;

    Piece(final int piecePosition, final PieceColor pieceColor, PieceType pieceType, final boolean isFirstMove) {
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceColor getPieceColor() {
        return this.pieceColor;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    // Legal moves list method
    public abstract List<Move> calcLegalMoves(final Board board);

    public enum PieceType {
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;

        PieceType(String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
    }

}
