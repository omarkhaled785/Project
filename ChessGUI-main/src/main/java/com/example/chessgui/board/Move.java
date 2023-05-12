package com.example.chessgui.board;

import com.example.chessgui.pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board excute();

    public static final class attackMove extends Move {
        final Piece attackedPiece;

        public attackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board excute() {
            // TODO
            return null;
        }
    }

    public static final class pieceMove extends Move {
        public pieceMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board excute() {
            // TODO
            return null;
        }
    }
}
