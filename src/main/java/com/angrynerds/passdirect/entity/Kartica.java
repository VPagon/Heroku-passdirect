package com.angrynerds.passdirect.entity;

import lombok.*;

@AllArgsConstructor
public class Kartica {
    
    @Getter @Setter
    private String brojKartice;
    @Getter @Setter
    private String imePrezVlasnik;
    @Getter @Setter
    private String email;

}