module ro.uaic {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens ro.uaic to javafx.fxml;
    exports ro.uaic;
}
