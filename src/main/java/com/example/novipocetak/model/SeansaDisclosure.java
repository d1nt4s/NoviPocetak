package com.example.novipocetak.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class SeansaDisclosure {
    private final IntegerProperty seansaId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> datum = new SimpleObjectProperty<>();
    private final StringProperty vreme = new SimpleStringProperty();
    private final IntegerProperty trajanje = new SimpleIntegerProperty();
    private final StringProperty beleske = new SimpleStringProperty();

    private final ObjectProperty<LocalDate> datumObjave = new SimpleObjectProperty<>();
    private final StringProperty kome = new SimpleStringProperty();
    private final StringProperty razlog = new SimpleStringProperty();

    private final IntegerProperty klijentId = new SimpleIntegerProperty();

    public SeansaDisclosure(int seansaId, LocalDate datum, String vreme, int trajanje, String beleske,
                            LocalDate datumObjave, String kome, String razlog, int klijentId) {
        this.seansaId.set(seansaId);
        this.datum.set(datum);
        this.vreme.set(vreme);
        this.trajanje.set(trajanje);
        this.beleske.set(beleske);
        this.datumObjave.set(datumObjave);
        this.kome.set(kome);
        this.razlog.set(razlog);
        this.klijentId.set(klijentId);
    }

    public IntegerProperty seansaIdProperty() { return seansaId; }
    public ObjectProperty<LocalDate> datumProperty() { return datum; }
    public StringProperty vremeProperty() { return vreme; }
    public IntegerProperty trajanjeProperty() { return trajanje; }
    public StringProperty beleskeProperty() { return beleske; }
    public ObjectProperty<LocalDate> datumObjaveProperty() { return datumObjave; }
    public StringProperty komeProperty() { return kome; }
    public StringProperty razlogProperty() { return razlog; }
    public int getKlijentId() { return klijentId.get(); }
    public IntegerProperty klijentIdProperty() { return klijentId; }
}
