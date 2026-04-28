module ro.uaic {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens ro.uaic to javafx.fxml;
    opens ro.uaic.entities to javafx.graphics, javafx.fxml;
    exports ro.uaic;
}
