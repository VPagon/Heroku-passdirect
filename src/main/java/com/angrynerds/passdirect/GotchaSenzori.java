package com.angrynerds.passdirect;

import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.entity.*;

import java.util.*;
import java.text.*;

public class GotchaSenzori {
    private int SLEEP_SECONDS = 30;
    private MjerenjeDao mjerenjeDao;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
    ArrayList<String> mjerenja = new ArrayList<>();

    public GotchaSenzori(MjerenjeDao mjerenjeDao) {
        this.mjerenjeDao = mjerenjeDao;
        mjerenja.add("0 151 23.12.2021. 20:13 8.9 100 15 222 499");
        mjerenja.add("1 151 23.12.2021. 20:25 8.9 890 890 100 100");
        mjerenja.add("2 151 23.12.2021. 20:55 8.9 465 970 222 499");
    
        
        mjerenja.add("0 128 29.12.2021. 15:34 8.9 100 15 222 499 233 123");
        mjerenja.add("1 128 29.12.2021. 16:46 8.9 890 890 100 100 222 15");
        mjerenja.add("2 128 29.12.2021. 19:43 8.9 465 970 222 499 23 120");
    }

    public void provjeraNovihVlakova() {
        while(true) {
            try {
                ArrayList<String> delete = new ArrayList<String>();
                for (String mjer : mjerenja) {
                    String[] mjer_split = mjer.split(" ");
                    Date date = format.parse(mjer_split[2] + " " + mjer_split[3]);
                    if (date.before(new Date())) {
                        System.out.println("------------------------------------[GOTCHA " + format.format(new Date()) + "] Novo mjerenje: " + mjer);
                        delete.add(mjer);
                        int oznakaStanica = Integer.parseInt(mjer_split[0]);
                        String oznakaVlak = mjer_split[1];
                        for (int i = 5; i < mjer_split.length; i+= 2) {
                            int rbrVagon = (i - 5) / 2 + 1;
                            Mjerenje mjerenje = mjerenjeDao.read(oznakaVlak, oznakaStanica, rbrVagon);
                            if (mjerenje != null) {
                                mjerenjeDao.delete(mjerenje);
                            }
                            int kgFront = Integer.parseInt(mjer_split[i]);
                            int kgBack = Integer.parseInt(mjer_split[i + 1]);
                            mjerenjeDao.create(new Mjerenje(oznakaVlak, oznakaStanica, kgFront, kgBack, rbrVagon, date));
                        }
                    }
                }
                mjerenja.removeAll(delete);
                Thread.sleep(SLEEP_SECONDS * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}