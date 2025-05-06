module com.example.novipocetak {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens com.example.novipocetak to javafx.fxml;
    exports com.example.novipocetak;
}