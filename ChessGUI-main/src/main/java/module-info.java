module com.example.chessgui {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.chessgui.board;
    opens com.example.chessgui.board to javafx.fxml;
    exports com.example.chessgui.pieces;
    opens com.example.chessgui.pieces to javafx.fxml;
}