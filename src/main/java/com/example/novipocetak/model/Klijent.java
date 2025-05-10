package com.example.novipocetak.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Klijent {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty ime = new SimpleStringProperty();
    private final StringProperty prezime = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dob = new SimpleObjectProperty<>();
    private final StringProperty pol = new SimpleStringProperty();
    private final StringProperty imejl = new SimpleStringProperty();
    private final StringProperty telefon = new SimpleStringProperty();
    private final BooleanProperty prosloIskustvo = new SimpleBooleanProperty();
    private final StringProperty problem = new SimpleStringProperty();

    public Klijent(int id, String ime, String prezime, LocalDate dob, String pol,
                   String imejl, String telefon, boolean prosloIskustvo, String problem) {
        this.id.set(id);
        this.ime.set(ime);
        this.prezime.set(prezime);
        this.dob.set(dob);
        this.pol.set(pol);
        this.imejl.set(imejl);
        this.telefon.set(telefon);
        this.prosloIskustvo.set(prosloIskustvo);
        this.problem.set(problem);
    }

    // Геттеры для TableView
    public StringProperty imeProperty() { return ime; }
    public StringProperty prezimeProperty() { return prezime; }
    public StringProperty polProperty() { return pol; }
    public StringProperty telefonProperty() { return telefon; }

    // Можно добавить и обычные геттеры/сеттеры при необходимости
}
