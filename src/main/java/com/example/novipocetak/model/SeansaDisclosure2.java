package com.example.novipocetak.model;

import javafx.beans.property.*;

public class SeansaDisclosure2 {
    private final StringProperty datum, vreme, beleske, testNaziv, rezultat, objava;
    private final IntegerProperty trajanje;

    public SeansaDisclosure2(String datum, String vreme, int trajanje, String beleske,
                      String testNaziv, String rezultat, String objava) {
        this.datum = new SimpleStringProperty(datum);
        this.vreme = new SimpleStringProperty(vreme);
        this.trajanje = new SimpleIntegerProperty(trajanje);
        this.beleske = new SimpleStringProperty(beleske);
        this.testNaziv = new SimpleStringProperty(testNaziv);
        this.rezultat = new SimpleStringProperty(rezultat);
        this.objava = new SimpleStringProperty(objava);
    }

    public StringProperty datumProperty() { return datum; }
    public StringProperty vremeProperty() { return vreme; }
    public IntegerProperty trajanjeProperty() { return trajanje; }
    public StringProperty beleskeProperty() { return beleske; }
    public StringProperty testNazivProperty() { return testNaziv; }
    public StringProperty rezultatProperty() { return rezultat; }
    public StringProperty objavaProperty() { return objava; }
}
