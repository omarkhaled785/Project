package com.example.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.example.chessgui.board.Move;
import com.example.chessgui.pieces.Piece;
//import com.google.common.primitives.Ints;
import com.example.gui.Table.MoveLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class TakenPiecesPane extends BorderPane {

    private final VBox northPanel;
    private final VBox southPanel;

    private static final Color PANEL_COLOR = Color.web("0xFDF5E6");
    private static final int TAKEN_PIECES_PANEL_WIDTH = 40;
    private static final int TAKEN_PIECES_PANEL_HEIGHT = 80;

    public TakenPiecesPane() {
        setStyle("-fx-background-color: #FDF5E6; -fx-border-color: black;");
        this.northPanel = new VBox();
        this.southPanel = new VBox();
        this.northPanel.setAlignment(Pos.CENTER);
        this.southPanel.setAlignment(Pos.CENTER);
        setTop(this.northPanel);
        setBottom(this.southPanel);
        setPrefSize(TAKEN_PIECES_PANEL_WIDTH, TAKEN_PIECES_PANEL_HEIGHT);
    }

    public void redo(final MoveLog moveLog) {
        this.northPanel.getChildren().clear();
        this.southPanel.getChildren().clear();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceType().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if (takenPiece.getPieceType().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        Collections.sort(whiteTakenPieces, Comparator.comparingInt(Piece::getPieceValue));
        Collections.sort(blackTakenPieces, Comparator.comparingInt(Piece::getPieceValue));

        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                final Image image = new Image(new FileInputStream("art/holywarriors/"
                        + takenPiece.getPieceType().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".gif"));
                final ImageView imageView = new ImageView(image);
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                this.southPanel.getChildren().add(imageView);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final Image image = new Image(new FileInputStream("art/holywarriors/"
                        + takenPiece.getPieceType().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".gif"));
                final ImageView imageView = new ImageView(image);
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                this.northPanel.getChildren().add(imageView);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
