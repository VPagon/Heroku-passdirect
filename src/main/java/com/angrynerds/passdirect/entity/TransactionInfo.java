package com.angrynerds.passdirect.entity;

import lombok.*;

@AllArgsConstructor
public class TransactionInfo {
    @Getter
    private String ime;
    @Getter
    private String prezime;
    @Getter
    private String email;
    @Getter
    private int oznakaKarta;
    @Getter
    private float cijenaKarta;
}