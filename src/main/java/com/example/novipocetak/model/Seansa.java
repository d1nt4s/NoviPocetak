package com.example.novipocetak.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Seansa {
    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private StringProperty vreme = new SimpleStringProperty();
    private IntegerProperty trajanje = new SimpleIntegerProperty();
    private StringProperty beleske = new SimpleStringProperty();

    public Seansa(int id, LocalDate datum, String vreme, int trajanje, String beleske) {
        this.id.set(id);
        this.datum.set(datum);
        this.vreme.set(vreme);
        this.trajanje.set(trajanje);
        this.beleske.set(beleske);
    }

    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<LocalDate> datumProperty() { return datum; }
    public StringProperty vremeProperty() { return vreme; }
    public IntegerProperty trajanjeProperty() { return trajanje; }
    public StringProperty beleskeProperty() { return beleske; }
}
