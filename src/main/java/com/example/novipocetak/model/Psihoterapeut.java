package com.example.novipocetak.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Psihoterapeut {
    private int id;
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private int univerzitetId;
}
