package com.angrynerds.passdirect.entity;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
public class User {
        
    @Getter @Setter
    private String ime;
    @Getter @Setter
    private String prezime;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String lozinka;
    @Getter @Setter
    private String oznakaUloga;

}
