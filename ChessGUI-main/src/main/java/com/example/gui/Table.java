package com.example.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Pane.*;
import javafx.scene.layout.Region.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.example.chessgui.board.BoardStructure;
import com.example.chessgui.board.Move;
import com.example.chessgui.pieces.Piece;
import com.example.gui.Table.PlayerType;
import com.example.chessgui.Player.MoveTransition;
import com.example.chessgui.Player.Player;
import com.example.chessgui.board.Board;

public final class Table {

    private final BorderPane mainPane;
    private final Stage primaryStage;
    private final GameHistoryPane gameHistoryPane;
    private final TakenPiecesPane takenPiecesPane;
    private final DebugPane debugPane;
    private final Board Board;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;
    private Board chessBoard;
    private Move computerMove;
    private Piece sourceTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private String pieceIconPath;
    private boolean highlightLegalMoves;
    private boolean useBook;
    private Color lightTileColor = javafx.scene.paint.Color.rgb(255, 250, 205);
    private Color darkTileColor = javafx.scene.paint.Color.rgb(89, 62, 26);

    private static final javafx.geometry.Dimension2D OUTER_FRAME_DIMENSION = new javafx.geometry.Dimension2D(600, 600);
    private static final javafx.geometry.Dimension2D BOARD_Pane_DIMENSION = new javafx.geometry.Dimension2D(400, 350);
    private static final javafx.geometry.Dimension2D TILE_Pane_DIMENSION = new javafx.geometry.Dimension2D(10, 10);

    private static final Table INSTANCE = new Table();

    private Table() {
        this.mainPane = new BorderPane();
        this.primaryStage = new Stage();
        final MenuBar tableMenuBar = new MenuBar();
        populateMenuBar(tableMenuBar);
        this.mainPane.setTop(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.useBook = false;
        this.pieceIconPath = "art/holywarriors/";
        this.gameHistoryPane = new GameHistoryPane();
        this.debugPane = new DebugPane();
        this.takenPiecesPane = new TakenPiecesPane();
        this.Board = Board;
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.primaryStage);
        this.mainPane.setLeft(this.takenPiecesPane);
        this.mainPane.setCenter(this.Board);
        this.mainPane.setRight(this.gameHistoryPane);
        this.mainPane.setBottom(this.debugPane);
        this.primaryStage.setScene(new Scene(this.mainPane));
        this.primaryStage.setTitle("BlackWidow");
        this.primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        this.primaryStage.show();
    }

    public static Table get() {
        return INSTANCE;
    }

    private Stage getGameFrame() {
        return this.primaryStage;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private Board getBoard() {
        return this.Board;
    }

    private GameHistoryPane getGameHistoryPane() {
        return this.gameHistoryPane;
    }

    private TakenPiecesPane getTakenPiecesPane() {
        return this.takenPiecesPane;
    }

    private DebugPane getDebugPane() {
        return this.debugPane;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private boolean getHighlightLegalMoves() {
        return this.highlightLegalMoves;
    }

    private boolean getUseBook() {
        return this.useBook;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPane().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPane().redo(Table.get().getMoveLog());
        Table.get().getBoard().drawBoard(Table.get().getGameBoard());
        Table.get().getDebugPane();
    }

    private void populateMenuBar(final MenuBar tableMenuBar) {
        tableMenuBar.getMenus().addAll(createOptionsMenu(), createPreferencesMenu(), createOptionsMenu());
    }

    private static void center(final Stage stage) {
        final Screen screen = Screen.getPrimary();
        final Rectangle2D bounds = screen.getVisualBounds();
        final double w = stage.getWidth();
        final double h = stage.getHeight();
        final double x = (bounds.getWidth() - w) / 2;
        final double y = (bounds.getHeight() - h) / 2;
        stage.setX(x);
        stage.setY(y);
    }

    /*
     * private Menu createFileMenu() {
     * final Menu filesMenu = new Menu("File");
     * filesMenu.setMnemonicParsing(true);
     * final MenuItem openPGN = new MenuItem("Load PGN File");
     * openPGN.setMnemonicParsing(true);
     * openPGN.setOnAction(e -> {
     * FileChooser chooser = new FileChooser();
     * FileChooser.ExtensionFilter extFilter = new
     * FileChooser.ExtensionFilter("PGN files (*.pgn)", "*.pgn");
     * chooser.getExtensionFilters().add(extFilter);
     * File file = chooser.showOpenDialog(Table.get().getGameFrame());
     * if (file != null) {
     * loadPGNFile(file);
     * }
     * });
     * filesMenu.getItems().add(openPGN);
     * 
     * final MenuItem openFEN = new MenuItem("Load FEN File");
     * openFEN.setMnemonicParsing(true);
     * openFEN.setOnAction(e -> {
     * TextInputDialog dialog = new TextInputDialog("");
     * dialog.setTitle("Load FEN File");
     * dialog.setHeaderText(null);
     * dialog.setContentText("Input FEN:");
     * Optional<String> result = dialog.showAndWait();
     * result.ifPresent(fenString -> {
     * undoAllMoves();
     * chessBoard = FenUtilities.createGameFromFEN(fenString);
     * Table.get().getBoard().drawBoard(chessBoard);
     * });
     * });
     * filesMenu.getItems().add(openFEN);
     * 
     * final MenuItem saveToPGN = new MenuItem("Save Game");
     * saveToPGN.setMnemonicParsing(true);
     * saveToPGN.setOnAction(e -> {
     * FileChooser chooser = new FileChooser();
     * FileChooser.ExtensionFilter extFilter = new
     * FileChooser.ExtensionFilter("PGN files (*.pgn)", "*.pgn");
     * chooser.getExtensionFilters().add(extFilter);
     * File file = chooser.showSaveDialog(Table.get().getGameFrame());
     * if (file != null) {
     * savePGNFile(file);
     * }
     * });
     * filesMenu.getItems().add(saveToPGN);
     * 
     * final MenuItem exitMenuItem = new MenuItem("Exit");
     * exitMenuItem.setMnemonicParsing(true);
     * exitMenuItem.setOnAction(e -> {
     * Table.get().getGameFrame().close();
     * });
     * filesMenu.getItems().add(exitMenuItem);
     * 
     * return filesMenu;
     * }
     */

    private Menu createOptionsMenu() {

        Menu optionsMenu = new Menu("Options");
        optionsMenu.setMnemonicParsing(true);
        optionsMenu.setMnemonic(KeyCode.O);

        MenuItem resetMenuItem = new MenuItem("New Game");
        resetMenuItem.setMnemonicParsing(true);
        resetMenuItem.setMnemonic(KeyCode.P);
        resetMenuItem.setOnAction(e -> undoAllMoves());
        optionsMenu.getItems().add(resetMenuItem);

        MenuItem evaluateBoardMenuItem = new MenuItem("Evaluate Board");
        evaluateBoardMenuItem.setMnemonicParsing(true);
        evaluateBoardMenuItem.setMnemonic(KeyCode.E);

        optionsMenu.getItems().add(evaluateBoardMenuItem);

        MenuItem escapeAnalysis = new MenuItem("Escape Analysis Score");
        escapeAnalysis.setMnemonicParsing(true);
        escapeAnalysis.setMnemonic(KeyCode.S);
        escapeAnalysis.setOnAction(e -> {
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
            if (lastMove != null) {
                System.out.println(MoveUtils.exchangeScore(lastMove));
            }
        });
        optionsMenu.getItems().add(escapeAnalysis);

        MenuItem legalMovesMenuItem = new MenuItem("Current State");
        legalMovesMenuItem.setMnemonicParsing(true);
        legalMovesMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        legalMovesMenuItem.setOnAction(e -> {
            System.out.println(chessBoard.getWhitePieces());
            System.out.println(chessBoard.getBlackPieces());
            System.out.println(playerInfo(chessBoard.currentPlayer()));
            System.out.println(playerInfo(chessBoard.currentPlayer().getOpponent()));
        });
        optionsMenu.getItems().add(legalMovesMenuItem);

        MenuItem undoMoveMenuItem = new MenuItem("Undo last move");
        undoMoveMenuItem.setMnemonicParsing(true);
        undoMoveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN));
        undoMoveMenuItem.setOnAction(e -> {
            if (Table.get().getMoveLog().size() > 0) {
                undoLastMove();
            }
        });
        optionsMenu.getItems().add(undoMoveMenuItem);

        final MenuItem setupGameMenuItem = new MenuItem("Setup Game", KeyEvent.VK_S);
        MenuItem setupGameMenuItem = new MenuItem("Setup Game");
        setupGameMenuItem.setOnAction(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.getItems().add(setupGameMenuItem);

        return optionsMenu;
    }

    private Menu createPreferencesMenu() {

        final Menu preferencesMenu = new Menu("Preferences");

        final Menu colorChooserSubMenu = new Menu("Choose Colors");
        colorChooserSubMenu.setMnemonic(KeyEvent.VK_S);

        final MenuItem chooseDarkMenuItem = new MenuItem("Choose Dark Tile Color");
        colorChooserSubMenu.add(chooseDarkMenuItem);

        final MenuItem chooseLightMenuItem = new MenuItem("Choose Light Tile Color");
        colorChooserSubMenu.add(chooseLightMenuItem);

        final MenuItem chooseLegalHighlightMenuItem = new MenuItem(
                "Choose Legal Move Highlight Color");
        colorChooserSubMenu.add(chooseLegalHighlightMenuItem);

        preferencesMenu.add(colorChooserSubMenu);

        chooseDarkMenuItem.setOnAction(e -> {
            final ColorPicker colorPicker = new ColorPicker();
            final Color colorChoice = colorPicker.showAndWait().orElse(null);
            if (colorChoice != null) {
                Table.get().getBoard().setTileDarkColor(chessBoard, colorChoice);
            }
        });

        chooseLightMenuItem.setOnAction(e -> {
            final ColorPicker colorPicker = new ColorPicker();
            final Color colorChoice = colorPicker.showAndWait().orElse(null);
            if (colorChoice != null) {
                Table.get().getBoard().setTileLightColor(chessBoard, colorChoice);
            }
        });

        final Menu chessMenChoiceSubMenu = new Menu("Choose Chess Men Image Set");

        Menu chessMenChoiceMenu = new Menu("Chess Piece Set");

        MenuItem holyWarriorsMenuItem = new MenuItem("Holy Warriors");
        holyWarriorsMenuItem.setOnAction(e -> {
            pieceIconPath = "art/holywarriors/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        MenuItem rockMenMenuItem = new MenuItem("Rock Men");
        rockMenMenuItem.setOnAction(e -> {
            // Implement me
        });

        MenuItem abstractMenMenuItem = new MenuItem("Abstract Men");
        abstractMenMenuItem.setOnAction(e -> {
            pieceIconPath = "art/simple/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        MenuItem woodMenMenuItem = new MenuItem("Wood Men");
        woodMenMenuItem.setOnAction(e -> {
            System.out.println("Implement me");
            Table.get().getGameFrame().repaint();
        });

        MenuItem fancyMenMenuItem = new MenuItem("Fancy Men");
        fancyMenMenuItem.setOnAction(e -> {
            pieceIconPath = "art/fancy/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        MenuItem fancyMenMenuItem2 = new MenuItem("Fancy Men 2");
        fancyMenMenuItem2.setOnAction(e -> {
            pieceIconPath = "art/fancy2/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        chessMenChoiceMenu.getItems().addAll(
                holyWarriorsMenuItem,
                rockMenMenuItem,
                abstractMenMenuItem,
                woodMenMenuItem,
                fancyMenMenuItem,
                fancyMenMenuItem2);

        woodMenMenuItem.setOnAction(e -> {
            System.out.println("implement me");
            Table.get().getGameFrame().repaint();
        });

        holyWarriorsMenuItem.setOnAction(e -> {
            pieceIconPath = "art/holywarriors/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        rockMenMenuItem.setOnAction(e -> {
            // implementation
        });

        abstractMenMenuItem.setOnAction(e -> {
            pieceIconPath = "art/simple/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        fancyMenMenuItem2.setOnAction(e -> {
            pieceIconPath = "art/fancy2/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        fancyMenMenuItem.setOnAction(e -> {
            pieceIconPath = "art/fancy/";
            Table.get().getBoard().drawBoard(chessBoard);
        });

        preferencesMenu.getItems().add(chessMenChoiceSubMenu);

        chooseLegalHighlightMenuItem.setOnAction(e -> {
            System.out.println("implement me");
            Table.get().getGameFrame().repaint();
        });

        MenuItem flipBoardMenuItem = new MenuItem("Flip board");
        flipBoardMenuItem.setOnAction(e -> {
            boardDirection = boardDirection.opposite();
            Board.drawBoard(chessBoard);
        });
        preferencesMenu.getItems().add(flipBoardMenuItem);
        preferencesMenu.getItems().add(new MenuItem());

        CheckMenuItem cbLegalMoveHighlighter = new CheckMenuItem("Highlight Legal Moves");
        cbLegalMoveHighlighter.setSelected(false);
        cbLegalMoveHighlighter.setOnAction(e -> highlightLegalMoves = cbLegalMoveHighlighter.isSelected());
        preferencesMenu.getItems().add(cbLegalMoveHighlighter);

        CheckMenuItem cbUseBookMoves = new CheckMenuItem("Use Book Moves");
        cbUseBookMoves.setSelected(false);
        preferencesMenu.getItems().add(cbUseBookMoves);

        cbUseBookMoves.addActionListener(e -> useBook = cbUseBookMoves.isSelected());

        preferencesMenu.add(cbUseBookMoves);

        return preferencesMenu;

    }

    private static String playerInfo(final Player player) {
        return ("Player is: " + player.getPlayerColor() + "\nlegal moves (" + player.getLegalMoves().size() + ") = "
                + player.getLegalMoves() + "\ninCheck = " +
                player.isInCheck() + "\nisInCheckMate = " + player.isInCheckMate() +
                "\nisCastled = " + player.hasCastled()) + "\n";
    }

    private void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private void undoAllMoves() {
        for (int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) {
            final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
            this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        }
        this.computerMove = null;
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPane().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPane().redo(Table.get().getMoveLog());
        Table.get().getBoard().drawBoard(chessBoard);
        Table.get().getDebugPane();
    }

    /*
     * private static void loadPGNFile(final File pgnFile) {
     * try {
     * persistPGNFile(pgnFile);
     * } catch (final IOException e) {
     * e.printStackTrace();
     * }
     * }
     */

    private void undoLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        this.computerMove = null;
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPane().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPane().redo(Table.get().getMoveLog());
        Table.get().getBoard().drawBoard(chessBoard);
        Table.get().getDebugPane();
    }

    /*
     * private void moveMadeUpdate(final PlayerType playerType) {
     * setChanged();
     * notifyObservers(playerType);
     * }
     * 
     * private void setupUpdate(final GameSetup gameSetup) {
     * setChanged();
     * notifyObservers(gameSetup);
     * }
     */

    private static class TableGameAIWatcher
            implements Observer {

        @Override
        public void update(final Observable o,
                final Object arg) {

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
                OptionPane.showMessageDialog(Table.get().getBoard(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in checkmate!",
                        "Game Over",
                        OptionPane.INFORMATION_MESSAGE);
            }

            if (Table.get().getGameBoard().currentPlayer().isInStalemate()) {
                OptionPane.showMessageDialog(Table.get().getBoard(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in stalemate!",
                        "Game Over",
                        OptionPane.INFORMATION_MESSAGE);
            }

        }

    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }

    public class Board extends Pane {

        final List<TilePane> boardTiles;

        Board() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardStructure.NUM_TILES; i++) {
                final TilePane tilePane = new TilePane(this, i);
                this.boardTiles.add(tilePane);
                add(tilePane);
            }
            setPreferredSize(BOARD_Pane_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.rgb(139, 71, 38));
            validate();
        }

        private void setPreferredSize(Dimension2D boardPaneDimension) {
        }

        void drawBoard(final Board board) {
            removeAll();
            for (final TilePane boardTile : boardDirection.traverse(boardTiles)) {
                boardTile.drawTile(board);
                add(boardTile);
            }
            validate();
            repaint();
        }

        void setTileDarkColor(final Board board,
                final Color darkColor) {
            for (final TilePane boardTile : boardTiles) {
                boardTile.setDarkTileColor(darkColor);
            }
            drawBoard(board);
        }

        void setTileLightColor(final Board board,
                final Color lightColor) {
            for (final TilePane boardTile : boardTiles) {
                boardTile.setLightTileColor(lightColor);
            }
            drawBoard(board);
        }

        public Player currentPlayer() {
            return null;
        }

    }

    enum BoardDirection {
        NORMAL {
            @Override
            List<TilePane> traverse(final List<TilePane> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePane> traverse(final List<TilePane> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePane> traverse(final List<TilePane> boardTiles);

        abstract BoardDirection opposite();

    }

    public static class MoveLog {

        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        void clear() {
            this.moves.clear();
        }

        Move removeMove(final int index) {
            return this.moves.remove(index);
        }

        boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

    }

    private class TilePane extends Pane {

        private final int tileId;

        TilePane(final Board Board,
                final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_Pane_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            highlightTileBorder(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {

                    if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) ||
                            BoardStructure.isEndGame(Table.get().getGameBoard())) {
                        return;
                    }

                    if (isRightMouseButton(event)) {
                        sourceTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(event)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getPiece(tileId);
                            humanMovedPiece = sourceTile;
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            final Move move = MoveFactory.createMove(chessBoard, sourceTile.getPiecePosition(),
                                    tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getToBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            humanMovedPiece = null;
                        }
                    }
                    invokeLater(() -> {
                        gameHistoryPane.redo(chessBoard, moveLog);
                        takenPiecesPane.redo(moveLog);
                        // if (gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
                        Table.get().moveMadeUpdate(PlayerType.HUMAN);
                        // }
                        Board.drawBoard(chessBoard);
                        debugPane.redo();
                    });
                }

                /*
                 * @Override
                 * public void mouseExited(final MouseEvent e) {
                 * }
                 * 
                 * @Override
                 * public void mouseEntered(final MouseEvent e) {
                 * }
                 * 
                 * @Override
                 * public void mouseReleased(final MouseEvent e) {
                 * }
                 * 
                 * @Override
                 * public void mousePressed(final MouseEvent e) {
                 * }
                 */
            });
            validate();
        }

        private void setPreferredSize(Dimension2D tilePaneDimension) {
        }

        void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightTileBorder(board);
            highlightLegals(board);
            highlightAIMove();
            validate();
            repaint();
        }

        void setLightTileColor(final Color color) {
            setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.rgb((int) color.getRed(), (int) color.getGreen(), (int) color.getBlue()),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
        }

        void setDarkTileColor(final Color color) {
            setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.rgb((int) color.getRed(), (int) color.getGreen(), (int) color.getBlue()),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
        }

        private void highlightTileBorder(final Board board) {
            if (humanMovedPiece != null &&
                    humanMovedPiece.getPieceType() == board.currentPlayer().getPlayerColor() &&
                    humanMovedPiece.getPiecePosition() == this.tileId) {
                setBorder(BorderFactory.createLineBorder(Color.CYAN));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }

        private void highlightAIMove() {
            if (computerMove != null) {
                if (this.tileId == computerMove.getDestinationCoordinate()) {
                    setBackground(Color.PINK);
                } else if (this.tileId == computerMove.getDestinationCoordinate()) {
                    setBackground(Color.RED);
                }
            }
        }

        private void highlightLegals(final Board board) {
            if (Table.get().getHighlightLegalMoves()) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new Label(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null
                    && humanMovedPiece.getPieceType() == board.currentPlayer().getPlayerColor()) {
                return humanMovedPiece.calcLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getPiece(this.tileId) != null) {
                try {
                    final BufferedImage image = Image.read(new File(pieceIconPath +
                            board.getPiece(this.tileId).getPieceAllegiance().toString().substring(0, 1) + "" +
                            board.getPiece(this.tileId).toString() +
                            ".gif"));
                    add(new Label(new ImageIcon(image)));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardStructure.INSTANCE.FIRST_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.THIRD_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.FIFTH_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.SEVENTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardStructure.INSTANCE.SECOND_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.FOURTH_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.SIXTH_ROW.get(this.tileId) ||
                    BoardStructure.INSTANCE.EIGHTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }

}
