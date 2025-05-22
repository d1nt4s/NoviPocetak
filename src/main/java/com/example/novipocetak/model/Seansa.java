package com.example.novipocetak.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Seansa {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private final StringProperty vreme = new SimpleStringProperty();
    private final IntegerProperty trajanje = new SimpleIntegerProperty();
    private final StringProperty beleske = new SimpleStringProperty();
    private final StringProperty klijent = new SimpleStringProperty();

    public Seansa(int id, LocalDate datum, String vreme, int trajanje, String beleske, String klijent) {
        this.id.set(id);
        this.datum.set(datum);
        this.vreme.set(vreme);
        this.trajanje.set(trajanje);
        this.beleske.set(beleske);
        this.klijent.set(klijent);
    }

    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<LocalDate> datumProperty() { return datum; }
    public StringProperty vremeProperty() { return vreme; }
    public IntegerProperty trajanjeProperty() { return trajanje; }
    public StringProperty beleskeProperty() { return beleske; }
    public StringProperty klijentProperty() { return klijent; }
}
