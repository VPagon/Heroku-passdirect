package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.util.*;
import com.angrynerds.passdirect.entity.*;

import spark.*;
import static spark.Spark.*;
import java.util.*;
import java.text.*;


public class SearchController {
  private VoznjaDao voznjaDao;
  private MjerenjeDao mjerenjeDao;

  public SearchController(VoznjaDao voznjaDao, MjerenjeDao MjerenjeDao) {
    this.voznjaDao = voznjaDao;
    this.mjerenjeDao = mjerenjeDao;
  }

  public Route serverStationPage = (Request req, Response res) -> {
    HashMap<String, Object> model = new HashMap<>();
    String name = req.queryParams("name");
    if (name == null) {
      res.redirect(Path.Web.HOME);
      return halt();
    }

    List<Voznja> voznje = voznjaDao.readAll();
    List<StanicaInfo> info = getIncoming(voznje, name);


    model.put("pageTitle", "Stajalište " + name + " - Passdirect");
    model.put("user", req.session().attribute("user"));
    model.put("voznje", info);
    model.put("stanica", name.substring(0, 1).toUpperCase() + name.substring(1));
    return ViewUtil.render(req, model, Path.Template.STATION);
  };

  public Route serveTimeTablePage = (Request req, Response res) -> {
    HashMap<String, Object> model = new HashMap<>();
    

    if (req.queryParams("start") == null || req.queryParams("destination") == null || req.queryParams("date") == null) {
      res.redirect(Path.Web.HOME);
      return halt();
    }

    List<Voznja> voznje = voznjaDao.readAll();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(req.queryParams("date"));
    List<SearchInfo> info = getSearch(voznje, req.queryParams("start"), req.queryParams("destination"), date);
    
    model.put("pageTitle", "Klasično pretrazivanje - Passdirect");
    model.put("user", req.session().attribute("user"));
    model.put("voznje", info);
    return ViewUtil.render(req, model, Path.Template.TIME_TABLE_USER);
  };

  private List<StanicaInfo> getIncoming(List<Voznja> sveVoznje, String stanica) {
    ArrayList<StanicaInfo> ret = new ArrayList<>();
    HashMap<String, List<Voznja>> voznje = new HashMap<>();
    
    sveVoznje.stream().forEach(voznja -> {
      if (!voznje.containsKey(voznja.getOznakaVlak())) {
        voznje.put(voznja.getOznakaVlak(), new ArrayList<Voznja>());
      }
      voznje.get(voznja.getOznakaVlak()).add(voznja);
    });

    for (String oznVoz : voznje.keySet()) {
      List<Voznja> v = voznje.get(oznVoz);
      for (Voznja jedna : v) {
        if (jedna.getOdredisteStanica().getNazivStanica().toLowerCase().equals(stanica.toLowerCase()) && (new Date()).before(jedna.getDatVrijDolaska())) {
          int kasnjenje = voznjaDao.readDelay(jedna.getOznakaVlak());
          ret.add(new StanicaInfo(jedna.getOznakaVlak(), v.get(v.size() - 1).getOdredisteStanica().getNazivStanica(), 1, jedna.getDatVrijDolaska(), kasnjenje));
          continue;
        }
      }
    }
    Collections.sort(ret, (v1, v2) -> v1.getDatVrijDolaska().compareTo(v2.getDatVrijDolaska()));
    return ret;
  }

  private List<SearchInfo> getSearch(List<Voznja> sveVoznje, String start, String dest, Date date) {
    ArrayList<SearchInfo> ret = new ArrayList<>();
    HashMap<String, List<Voznja>> voznje = new HashMap<>();
    sveVoznje.stream().forEach(voznja -> {
      if (!voznje.containsKey(voznja.getOznakaVlak())) {
        voznje.put(voznja.getOznakaVlak(), new ArrayList<Voznja>());
      }
      voznje.get(voznja.getOznakaVlak()).add(voznja);
    });

    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    for (List<Voznja> voznja : voznje.values()) {
      
      int idxPol = -1, idxOdr = -1;
      float cijena = 0;

      for (int i = 0; i < voznja.size(); i++) {
        if (idxPol == -1 && 
            voznja.get(i).getStanica().getNazivStanica().toLowerCase().equals(start.toLowerCase()) && 
            fmt.format(voznja.get(i).getDatVrijPolaska()).equals(fmt.format(date))) {
          idxPol = i;
            }
        if (idxOdr == -1 && voznja.get(i).getOdredisteStanica().getNazivStanica().toLowerCase().equals(dest.toLowerCase())) {
          idxOdr = i;
          break;
        }
      }
      if (idxPol != -1 && idxOdr != -1 && voznja.get(idxPol).getDatVrijPolaska().after(new Date())) {
        for (int i = idxPol; i <= idxOdr; i++) {
          cijena += voznja.get(i).getCijenaVoznja();
        }
        Voznja vp = voznja.get(idxPol);
        Voznja vd = voznja.get(idxOdr);
        int kasnjenje = voznjaDao.readDelay(vp.getOznakaVlak());
        ret.add(new SearchInfo(vp.getOznakaVlak(), vp.getStanica().getNazivStanica(), vp.getDatVrijPolaska(), 
                               vd.getOdredisteStanica().getNazivStanica(), vd.getDatVrijDolaska(), 1, kasnjenje, cijena));
      }
    }
    Collections.sort(ret, (v1, v2) -> v1.getDatVrijPolaska().compareTo(v2.getDatVrijPolaska()));
    return ret;
  }

}