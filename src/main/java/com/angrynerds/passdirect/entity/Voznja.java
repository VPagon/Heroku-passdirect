package com.angrynerds.passdirect.entity;

import java.util.*;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
public class Voznja {
  
  @Getter @Setter
  private float cijenaVoznja;
  @Getter @Setter
  private Date datVrijPolaska;
  @Getter @Setter
  private Date datVrijDolaska;
  @Getter @Setter
  private Stanica stanica;
  @Getter @Setter
  private Stanica odredisteStanica;
  @Getter @Setter
  private String oznakaVlak;

}
