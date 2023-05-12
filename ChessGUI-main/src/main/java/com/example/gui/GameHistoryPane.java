package com.example.gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

import com.example.chessgui.board.Board;
import com.example.chessgui.board.Move;
import com.example.gui.Table.MoveLog;

public class GameHistoryPane extends BorderPane {
    private final DataModel model;
    private final ScrollPane scrollPane;
    private static final int HISTORY_PANEL_WIDTH = 100;
    private static final int HISTORY_PANEL_HEIGHT = 40;
}

    public GameHistoryPane() {
        this.model = new DataModel();
        final TableView<Row> table = new TableView<>();
        table.setPrefHeight(15);
        table.setPrefWidth(HISTORY_PANEL_WIDTH);
        table.setPrefHeight(HISTORY_PANEL_HEIGHT);
        TableColumn<Row, String> whiteColumn = new TableColumn<>("White");
        whiteColumn.setCellValueFactory(new PropertyValueFactory<>("whiteMove"));
        TableColumn<Row, String> blackColumn = new TableColumn<>("Black");
        blackColumn.setCellValueFactory(new PropertyValueFactory<>("blackMove"));
        table.getColumns().addAll(whiteColumn, blackColumn);
        this.scrollPane = new ScrollPane(table);
        this.setCenter(scrollPane);
        this.setVisible(true);
    }

    public void redo(final Board chessBoard, final MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();
        for (final Move move : moveHistory.getMoves()) {
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceColor().isWhite()) {
                this.model.setValueAt(moveText, currentRow, 0);
            } else if (move.getMovedPiece().getPieceColor().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        if (moveHistory.getMoves().size() > 0) {
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getMovedPiece().getPieceColor().isWhite()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(chessBoard), currentRow, 0);
            } else if (lastMove.getMovedPiece().getPieceColor().isBlack()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(chessBoard), currentRow - 1, 1);
            }
        }

        final ScrollPane vertical = scrollPane;
        vertical.setVvalue(vertical.getVmax());
    }

    private static String calculateCheckAndCheckMateHash(final Board board) {
        if (board.currentPlayer().isInCheckMate()) {
            return "#";
        } else if (board.currentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

    private static class Row {

        private String whiteMove;
        private String blackMove;

        Row() {
        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final String move) {
            this.whiteMove = move;
        }

        public void setBlackMove(final String move) {
            this.blackMove = move;
        }
    }

    private static class DataModel {
        private final List<Row> values;

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
        }

        public void setValueAt(final Object aValue, final int row, final int col) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if (col == 0) {
                currentRow.setWhiteMove((String) aValue);
            } else if (col == 1) {
                currentRow.setBlackMove((String) aValue);
            }
        }

        public Class<?> getColumnClass(final int col) {
            return String.class;
        }

        public String getColumnName(final int col) {
            return col == 0 ? "White" : "Black";
        }
    }
}
