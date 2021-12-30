package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.config.Path;
import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.util.ViewUtil;
import spark.*;
import java.util.*;

public class IndexController {
    private VoznjaDao voznjaDao;

    public IndexController(VoznjaDao voznjaDao) {
        this.voznjaDao = voznjaDao;
    }

    public Route serveIndexPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Početna stranica - Passdirect");
        return ViewUtil.render(req, model, Path.Template.INDEX);
    };

    public Route serveFunctionsPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        List<Stanica> stanice = voznjaDao.readAllStations();

        model.put("pageTitle", "Početna stranica - Passdirect");
        model.put("stanice", stanice);
        model.put("user", req.session().attribute("user"));
        return ViewUtil.render(req, model, Path.Template.HOME);
    };
}