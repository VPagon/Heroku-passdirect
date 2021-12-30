package com.angrynerds.passdirect.entity;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.util.*;

import lombok.*;
import java.util.*;

@AllArgsConstructor
public class SearchInfo {
    @Getter
    private String oznakaVlak;
    @Getter
    private String mjestoPolaska;
    @Getter
    private Date datVrijPolaska;
    @Getter
    private String mjestoDolaska;
    @Getter
    private Date datVrijDolaska;
    @Getter
    private int kolosijekDolaska;
    @Getter
    private int kasnjenjeMin;
    @Getter 
    private float cijenaVoznja;

    public String getDatVrijPolaskaFormatted() {
        return DateFormatter.format(datVrijPolaska);
    }

    public String getDatVrijDolaskaFormatted() {
        return DateFormatter.format(datVrijDolaska);
    }

}