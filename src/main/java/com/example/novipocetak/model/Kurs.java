package com.example.novipocetak.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kurs {
    private int id;
    private String naziv;
    private String opis;
    private int psihoterapeutId;
}
