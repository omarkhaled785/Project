package com.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class DebugPane extends BorderPane {

    private static final int CHAT_PANEL_WIDTH = 600;
    private static final int CHAT_PANEL_HEIGHT = 150;
    private final TextArea textArea;

    public DebugPane() {
        this.textArea = new TextArea();
        this.textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(CHAT_PANEL_WIDTH);
        scrollPane.setPrefHeight(CHAT_PANEL_HEIGHT);

        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setPadding(new Insets(5));
        stackPane.setAlignment(Pos.TOP_LEFT);

        setCenter(stackPane);
    }

    /*
     * @Override
     * public void update(Observable o, Object arg) {
     * String message = arg.toString().trim();
     * this.textArea.setText(message);
     * }
     * 
     * @Override
     * public void update(Observable o, Object arg) {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'update'");
     * }
     */
}
