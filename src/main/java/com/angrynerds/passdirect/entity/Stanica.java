package com.angrynerds.passdirect.entity;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
public class Stanica {

  @Getter @Setter
  private int oznakaStanica;
  @Getter @Setter
  private String nazivStanica;

}
