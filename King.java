package com.example.chessgui.pieces;

import com.example.chessgui.PieceColor;
import com.example.chessgui.board.Board;
import com.example.chessgui.board.BoardStructure;
import com.example.chessgui.board.Move;
import com.example.chessgui.board.Move.MajorAttackMove;
import com.example.chessgui.board.Move.MajorMove;

import java.util.*;

public final class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

    private final static Map<Integer, int[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final PieceColor PieceColor,
            final int piecePosition,
            final boolean kingSideCastleCapable,
            final boolean queenSideCastleCapable) {
        super(PieceType.KING, PieceColor, piecePosition, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final PieceColor PieceColor,
            final int piecePosition,
            final boolean isFirstMove,
            final boolean isCastled,
            final boolean kingSideCastleCapable,
            final boolean queenSideCastleCapable) {
        super(PieceType.KING, PieceColor, piecePosition, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    private static Map<Integer, int[]> computeCandidates() {
        final Map<Integer, int[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardStructure.NUM_TILES; position++) {
            int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];
            int numLegalOffsets = 0;
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                if (isFirstColumnExclusion(position, offset) ||
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }
                int destination = position + offset;
                if (BoardStructure.isValidTileCoordinate(destination)) {
                    legalOffsets[numLegalOffsets++] = offset;
                }
            }
            if (numLegalOffsets > 0) {
                candidates.put(position, Arrays.copyOf(legalOffsets, numLegalOffsets));
            }
        }
        return Collections.unmodifiableMap(candidates);
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
            if (pieceAtDestination == null) {
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate, isCastled));
            } else {
                final PieceColor pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                    legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                            pieceAtDestination));
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.kingBonus(this.piecePosition);
    }

    @Override
    public King movePiece(final Move move) {
        return new King(this.pieceAlliance, move.getDestinationCoordinate(), false, move.isCastlingMove(), false,
                false);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof King)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final King king = (King) other;
        return isCastled == king.isCastled;
    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + (isCastled ? 1 : 0);
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
            final int candidateDestinationCoordinate) {
        return BoardStructure.INSTANCE.FIRST_COLUMN.get(currentCandidate)
                && ((candidateDestinationCoordinate == -9) || (candidateDestinationCoordinate == -1) ||
                        (candidateDestinationCoordinate == 7));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
            final int candidateDestinationCoordinate) {
        return BoardStructure.INSTANCE.EIGHTH_COLUMN.get(currentCandidate)
                && ((candidateDestinationCoordinate == -7) || (candidateDestinationCoordinate == 1) ||
                        (candidateDestinationCoordinate == 9));
    }

    @Override
    public List<Move> calcLegalMoves(com.example.gui.Table.Board board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcLegalMoves'");
    }
}
