package com.angrynerds.passdirect;

import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.util.*;

import java.util.*;

public class Rasporedjivac {
    private int SLEEP_SECONDS = 60;
    private KartaDao kartaDao;
    private VoznjaDao voznjaDao;
    private MjerenjeDao mjerenjeDao;

    public Rasporedjivac(KartaDao kartaDao, VoznjaDao voznjaDao, MjerenjeDao mjerenjeDao) {
        this.kartaDao = kartaDao;
        this.voznjaDao = voznjaDao;
        this.mjerenjeDao = mjerenjeDao;
    }

    public void rasporedi() {
        while (true) {
            try {
                List<Karta> nesmjestene = kartaDao.readNesmjestene();
                for (Karta karta : nesmjestene) {
                    obradiKartu(karta);
                }
                Thread.sleep(SLEEP_SECONDS * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void obradiKartu(Karta karta) {
        List<Voznja> voznjeKarte = voznjaDao.readVoznjeByKarta(karta.getOznakaKarta());
        if (voznjeKarte.size() == 0)
            return;

        int mjerenjeOznSt = -1;
        Voznja prvaVoznja = voznjeKarte.get(0);
        List<Voznja> sveVoznje = voznjaDao.read(prvaVoznja.getOznakaVlak());
        for (int i = 0; i < sveVoznje.size(); i++) {
            if (sveVoznje.get(i).getOdredisteStanica().getOznakaStanica() == prvaVoznja.getStanica().getOznakaStanica()) {
                mjerenjeOznSt = sveVoznje.get(i).getStanica().getOznakaStanica();
                break;
            }
        }

        if (mjerenjeOznSt == -1) 
            return;

        List<Mjerenje> mjerenjaVagona = mjerenjeDao.read(prvaVoznja.getOznakaVlak(), mjerenjeOznSt);
        if (mjerenjaVagona.size() == 0)
            return;

        System.out.println("[RASPOREDJIVAC] Rasporedjujem kartu " + karta.getOznakaKarta());
        
        
        // "prazniji dio najmanje okupiranog vagona"
        int rbrVagonMinIdx = 0;
        int min_sum = mjerenjaVagona.get(rbrVagonMinIdx).getKgFront() + mjerenjaVagona.get(rbrVagonMinIdx).getKgBack();
        for (int i = 0; i < mjerenjaVagona.size(); i++) {
            int sum = mjerenjaVagona.get(i).getKgFront()  + mjerenjaVagona.get(i).getKgBack();
            if (sum < min_sum) {
                rbrVagonMinIdx = i;
                min_sum = sum;
            }
        }
        String pozicija = (mjerenjaVagona.get(rbrVagonMinIdx).getKgFront() <
                           mjerenjaVagona.get(rbrVagonMinIdx).getKgBack()) ? "F" : "B";
        
        
        // smjesti kartu
        karta.setPozicija(pozicija);
        karta.setRbrVagon(rbrVagonMinIdx + 1);
        kartaDao.update(karta);

        // osvjezi mjerenje
        Mjerenje mjerenje = mjerenjaVagona.get(rbrVagonMinIdx);
        if (pozicija.equals("F")) {
            mjerenje.setKgFront(mjerenje.getKgFront() + 75);
        }  else if (pozicija.equals("B")) {
            mjerenje.setKgBack(mjerenje.getKgBack() + 75);
        }
        mjerenjeDao.update(mjerenje);

        // postalji mail
        final String email = karta.getEmail();
        final String start = prvaVoznja.getStanica().getNazivStanica();
        final String dest = voznjeKarte.get(voznjeKarte.size() - 1).getOdredisteStanica().getNazivStanica();
        final String datVrijPolaska = DateFormatter.format(prvaVoznja.getDatVrijPolaska());
        final int rbrVagon = rbrVagonMinIdx + 1;
        final String poz = (pozicija.equals("F")) ? "prednji" : "stražnji";
        final String oznakaVlak = prvaVoznja.getOznakaVlak();

        SendEmail.sendTicketPosition(email, start, dest, datVrijPolaska, rbrVagon, poz, oznakaVlak);        
    }

}