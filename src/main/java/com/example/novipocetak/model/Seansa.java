package com.example.novipocetak.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seansa {
    private int id;
    private int psihoterapeutId;
    private int klijentId;
    private String datumVreme;
}
