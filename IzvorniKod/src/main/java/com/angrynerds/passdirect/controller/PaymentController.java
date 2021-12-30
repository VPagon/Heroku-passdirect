package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.util.*;

import spark.*;
import java.util.*;
import static spark.Spark.*;

public class PaymentController {
    private KarticaDao karticaDao;
    private KartaDao kartaDao;
    private VoznjaDao voznjaDao;

    public PaymentController(KarticaDao karticaDao, KartaDao kartaDao, VoznjaDao voznjaDao) {
        this.karticaDao = karticaDao;
        this.kartaDao = kartaDao;
        this.voznjaDao = voznjaDao;
    }

    public Route servePaymentPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Plaćanje karte");

        User user = req.session().attribute("user");
        Kartica kartica = karticaDao.read(user.getEmail());
        model.put("kartica", kartica);

        List<Voznja> voznje = getVoznje(req.params("oznVlak"), req.params("start"), req.params("dest"));
        if (voznje.size() == 0) {
            res.redirect(Path.Web.HOME);
            return halt();
        }

        
        model.put("price", getVoznjePrice(voznje));
        model.put("oznVlak", req.params("oznVlak"));
        model.put("start", req.params("start"));
        model.put("dest", req.params("dest"));
        return ViewUtil.render(req, model, Path.Template.PAYMENT_PROMPT);
    };

    public Route handlePaymentPost = (Request req, Response res) -> {
        User user = req.session().attribute("user");
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Kupnja karte - Passdirect");
        model.put("user", user);
        
        String brojKartice = req.queryParams("cardnumber");
        String datumIsteka = req.queryParams("carddate");
        String CVV = req.queryParams("cardcvv");
        String imePrezVlasnik = req.queryParams("cardowner");
        
        if (!checkCardInfo(brojKartice, datumIsteka, CVV, imePrezVlasnik)) {
            model.put("paymentSuccess", false);
            return ViewUtil.render(req, model, Path.Template.PAYMENT_END);
        }

        Kartica kartica = new Kartica(brojKartice, imePrezVlasnik, user.getEmail());
        Kartica postojeca = karticaDao.read(user.getEmail());
        if (postojeca == null) {
            karticaDao.create(kartica);
        } else {
            karticaDao.update(kartica);
        }

        int maxId = kartaDao.maxOznakaKarta();
        Karta karta = new Karta(maxId + 1, null, -1, user.getEmail());
        kartaDao.create(karta);
        List<Voznja> voznje = getVoznje(req.params("oznVlak"), req.params("start"), req.params("dest"));
        kartaDao.insertVoznje(karta, voznje);


        final String email = user.getEmail();
        final String start = voznje.get(0).getStanica().getNazivStanica();
        final String dest = voznje.get(voznje.size() - 1).getOdredisteStanica().getNazivStanica();
        final String datVrijPolaska = DateFormatter.format(voznje.get(0).getDatVrijPolaska());
        float price = getVoznjePrice(voznje);
        (new Thread() {
            @Override
            public void run() {
                SendEmail.sendTicketBoughtMail(email, start, dest, datVrijPolaska, price);
            }
        }).start();

        model.put("paymentSuccess", true);
        return ViewUtil.render(req, model, Path.Template.PAYMENT_END);
    };

    private boolean checkCardInfo(String brojKartice, String datumIsteka, String CVV, String imePrezVlasnik) {
        if (brojKartice.length() < 16) return false;
        if (datumIsteka.length() < 5 || datumIsteka.indexOf("/") != 2) return false;
        try {
          Integer.parseInt(datumIsteka.substring(0, 2));
          Integer.parseInt(datumIsteka.substring(3, 5));  
        } catch (Exception e) {
            return false;
        }
        if (CVV.length() < 3) return false;
        try {
            Integer.parseInt(CVV);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private List<Voznja> getVoznje(String oznakaVlak, String start, String dest) {
        List<Voznja> ret = new ArrayList<>();
        List<Voznja> voznje = voznjaDao.read(oznakaVlak);
        
        boolean add = false;
        for (Voznja v : voznje) {
            if (v.getStanica().getNazivStanica().toLowerCase().equals(start.toLowerCase())) add = true;
            if (add) ret.add(v);
            if (v.getOdredisteStanica().getNazivStanica().toLowerCase().equals(dest.toLowerCase())) break;
        }

        return ret;
    }

    private float getVoznjePrice(List<Voznja> voznje) {
        float price = 0;
        for (Voznja v : voznje) {
            price += v.getCijenaVoznja();
        }
        return price;
    }

}