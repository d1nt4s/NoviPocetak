package com.example.novipocetak.model;

import javafx.beans.property.*;

public class SeansaDisclosure {
    private final IntegerProperty seansaId;
    private final StringProperty datum;
    private final StringProperty vreme;
    private final IntegerProperty trajanje;
    private final StringProperty beleske;
    private final StringProperty testNaziv;
    private final StringProperty rezultat;
    private final StringProperty objava;
    private final StringProperty klijent;

    public SeansaDisclosure(int seansaId, String datum, String vreme, int trajanje, String beleske,
                            String testNaziv, String rezultat, String objava, String klijent) {
        this.seansaId = new SimpleIntegerProperty(seansaId);
        this.datum = new SimpleStringProperty(datum);
        this.vreme = new SimpleStringProperty(vreme);
        this.trajanje = new SimpleIntegerProperty(trajanje);
        this.beleske = new SimpleStringProperty(beleske);
        this.testNaziv = new SimpleStringProperty(testNaziv);
        this.rezultat = new SimpleStringProperty(rezultat);
        this.objava = new SimpleStringProperty(objava);
        this.klijent = new SimpleStringProperty(klijent);
    }

    public IntegerProperty seansaIdProperty() { return seansaId; }
    public StringProperty datumProperty() { return datum; }
    public StringProperty vremeProperty() { return vreme; }
    public IntegerProperty trajanjeProperty() { return trajanje; }
    public StringProperty beleskeProperty() { return beleske; }
    public StringProperty testNazivProperty() { return testNaziv; }
    public StringProperty rezultatProperty() { return rezultat; }
    public StringProperty objavaProperty() { return objava; }
    public StringProperty klijentProperty() { return klijent; }
}
