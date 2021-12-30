package com.angrynerds.passdirect.entity;

import lombok.*;

@AllArgsConstructor
public class Karta {

    @Getter @Setter
    private int oznakaKarta;
    @Getter @Setter
    private String pozicija;
    @Getter @Setter
    private int rbrVagon;
    @Getter @Setter
    private String email;

}