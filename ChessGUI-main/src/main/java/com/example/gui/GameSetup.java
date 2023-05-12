package com.example.gui;

import com.example.chessgui.PieceColor;
import com.example.chessgui.Player.Player;
import com.example.gui.Table.PlayerType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameSetup extends Stage {

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private Spinner<Integer> searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    public GameSetup(final Stage parent) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Game Setup");

        final VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        final Label whiteLabel = new Label("White:");
        final Label blackLabel = new Label("Black:");

        final ToggleGroup whiteGroup = new ToggleGroup();
        final ToggleGroup blackGroup = new ToggleGroup();

        final RadioButton whiteHumanButton = new RadioButton(HUMAN_TEXT);
        final RadioButton whiteComputerButton = new RadioButton(COMPUTER_TEXT);
        final RadioButton blackHumanButton = new RadioButton(HUMAN_TEXT);
        final RadioButton blackComputerButton = new RadioButton(COMPUTER_TEXT);

        whiteHumanButton.setToggleGroup(whiteGroup);
        whiteComputerButton.setToggleGroup(whiteGroup);
        blackHumanButton.setToggleGroup(blackGroup);
        blackComputerButton.setToggleGroup(blackGroup);

        whiteHumanButton.setSelected(true);
        blackHumanButton.setSelected(true);

        final HBox whiteBox = new HBox(10, whiteLabel, whiteHumanButton, whiteComputerButton);
        final HBox blackBox = new HBox(10, blackLabel, blackHumanButton, blackComputerButton);

        final Label searchLabel = new Label("Search Depth:");
        final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, Integer.MAX_VALUE, 6, 1);
        searchDepthSpinner = new Spinner<>(valueFactory);

        final Button cancelButton = new Button("Cancel");
        final Button okButton = new Button("OK");

        okButton.setOnAction(event -> {
            whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            close();
        });

        cancelButton.setOnAction(event -> {
            System.out.println("Cancel");
            close();
        });

        final HBox buttonBox = new HBox(10, cancelButton, okButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        final GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(whiteBox, 0, 0);
        grid.add(blackBox, 0, 1);
        grid.add(searchLabel, 0, 2);
        grid.add(searchDepthSpinner, 1, 2);
        grid.add(buttonBox, 1, 3);

        root.getChildren().add(grid);

        final Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
        initOwner(parent);
        centerOnScreen();
    }

    public void promptUser() {
        showAndWait();
    }

    public boolean isAIPlayer(final Player player) {
        if (player.getPlayerColor() == PieceColor.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    public PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    public PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    private static Spinner<Integer> addLabeledSpinner(final VBox container,
            final String label,
            final int min,
            final int max,
            final int initialValue) {
        final Label l = new Label(label);
        final Spinner<Integer> spinner = new Spinner<>(min, max, initialValue);
        l.setLabelFor(spinner);
        container.getChildren().addAll(l, spinner);
        return spinner;
    }

    public int getSearchDepth() {
        return this.searchDepthSpinner.getValue();
    }
}
