module game.theory.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;

    opens org.lab to javafx.graphics;
    opens org.lab.view to javafx.fxml, javafx.graphics;
    opens org.lab.controller to javafx.fxml;
}