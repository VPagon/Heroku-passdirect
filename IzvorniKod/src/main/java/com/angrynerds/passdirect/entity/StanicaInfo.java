package com.angrynerds.passdirect.entity;


import com.angrynerds.passdirect.util.*;
import com.angrynerds.passdirect.config.*;

import lombok.*;
import java.util.Date;

@AllArgsConstructor
public class StanicaInfo {
    @Getter
    private String oznakaVlak;
    @Getter
    private String krajnjeOdrediste;
    @Getter
    private int kolosijek;
    @Getter
    private Date datVrijDolaska;
    @Getter
    private int kasnjenjeMin;

    public String getDatVrijDolaskaFormatted() {
        return DateFormatter.format(datVrijDolaska);
    }

}