package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.halt;

public class AdminController {
    private UserDao userDao;
    private VoznjaDao voznjaDao;
    private KartaDao kartaDao;
    
    public AdminController(UserDao userDao, VoznjaDao voznjaDao, KartaDao kartaDao) {
        this.userDao = userDao;
        this.voznjaDao = voznjaDao;
        this.kartaDao = kartaDao;
    }

    public Route serveAdminPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();

        model.put("pageTitle", "Kontrolna ploča - Passdirect Admin");
        model.put("user", req.session().attribute("user"));

        return ViewUtil.render(req, model, Path.Template.ADMIN);
    };

    public Route serveUsersPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();

        List<User> users = userDao.readAll();

        model.put("pageTitle", "Korisnici - Passdirect Admin");
        model.put("user", req.session().attribute("user"));
        model.put("users", users);

        return ViewUtil.render(req, model, Path.Template.ADMIN_USERS);
    };

    public Route handleDeleteUser = (Request req, Response res) -> {
        String email = ((User)req.session().attribute("user")).getEmail();
        User user_delete = userDao.read(req.params("user"));

        // admin ne smije moci izbrisati samoga sebe!
        if (email.equals(req.params("user"))) {
            res.redirect(Path.Web.ADMIN_USERS);
            return halt();
        }

        if (user_delete != null) {
            userDao.delete(user_delete);
        }

        res.redirect(Path.Web.ADMIN_USERS);
        return halt();
    };

    public Route serveTransactionsPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();

        model.put("pageTitle", "Transakcije - Passdirect Admin");
        model.put("user", req.session().attribute("user"));
        model.put("transactions", kartaDao.readAllAdmin());

        return ViewUtil.render(req, model, Path.Template.ADMIN_TRANSACTIONS);
    };

    public Route serveTimeTablePage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();

        model.put("pageTitle", "Red vožnje - Passdirect Admin");
        model.put("user", req.session().attribute("user"));
        model.put("red-voznje", voznjaDao.readAll());

        return ViewUtil.render(req, model, Path.Template.ADMIN_TIME_TABLE);
    };

}
