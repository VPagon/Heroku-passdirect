package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.util.ViewUtil;
import com.angrynerds.passdirect.entity.User;
import com.angrynerds.passdirect.config.Path;
import com.angrynerds.passdirect.dao.UserDao;

import static com.angrynerds.passdirect.util.RequestUtil.*;
import spark.*;
import java.util.HashMap;

import static spark.Spark.*;

public class LoginController {
    private UserDao userDao;

    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    // GET
    public Route serveLoginPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Prijava - Passdirect");
        return ViewUtil.render(req, model, Path.Template.LOGIN);
    };

    // POST
    public Route handleLoginPost = (Request req, Response res) -> {
        // provjeri da li postoji korisnik registriran s tim email-om i lozinkom
        String email = getQueryEmail(req);

        if (email == null) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("pageTitle", "Prijava - Passdirect");
            model.put("loginFailed", true);
            return ViewUtil.render(req, model, Path.Template.LOGIN);
        }

        User user = userDao.read(email);
        if (user == null || !user.getLozinka().equals(getQueryPassword(req))) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("pageTitle", "Prijava - Passdirect");
            model.put("loginFailed", true);
            return ViewUtil.render(req, model, Path.Template.LOGIN);
        }

        // stavi atribut currentUser u session
        req.session().attribute("user", user);

        // redirect
        res.redirect(Path.Web.INDEX);
        return halt();
    };

    public Route handleLogoutGet = (Request req, Response res) -> {
        req.session().invalidate();
        res.redirect(Path.Web.INDEX);
        return halt();
    };

}
