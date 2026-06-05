module game.theory.lab {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.lab.view to javafx.fxml, javafx.graphics;
    opens org.lab to javafx.fxml, javafx.graphics;
}