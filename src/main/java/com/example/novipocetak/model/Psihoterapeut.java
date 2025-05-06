package com.example.novipocetak.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Psihoterapeut {
    private int id;
    private String ime;
    private String prezime;
    private String jmbg;
    private String imejl;
    private String telefon;

    public Psihoterapeut(int id, String ime, String prezime, String jmbg, String imejl, String telefon) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.imejl = imejl;
        this.telefon = telefon;
    }
}
