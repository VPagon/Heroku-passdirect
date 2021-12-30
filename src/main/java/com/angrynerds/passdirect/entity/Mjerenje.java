package com.angrynerds.passdirect.entity;

import lombok.*;

import java.util.*;

@AllArgsConstructor
public class Mjerenje {
    @Getter @Setter
    private String oznakaVlak;
    @Getter @Setter
    private int oznakaStanica;
    @Getter @Setter
    private int kgFront;
    @Getter @Setter
    private int kgBack;
    @Getter @Setter
    private int rbrVagon;
    @Getter @Setter
    private Date datVrijMjerenja;
}