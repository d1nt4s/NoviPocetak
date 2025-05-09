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
    opens com.example.novipocetak.controllers to javafx.fxml;
    opens com.example.novipocetak.model to javafx.base;

    exports com.example.novipocetak;
    exports com.example.novipocetak.controllers;
}
