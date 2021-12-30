package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.util.ViewUtil;
import com.angrynerds.passdirect.dao.*;


import static spark.Spark.*;
import spark.*;
import java.util.*;



public class AccountController {
    private UserDao userDao;

    public AccountController(UserDao userDao) {
        this.userDao = userDao;
    }

    public Route serveActivationPrompt = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Aktivacija računa - Passdirect");
        return ViewUtil.render(req, model, Path.Template.ACTIVATE_PROMPT);
    };

    public Route handleActivationGet = (Request req, Response res) -> {
        String hash = req.queryParams("hash");
        if (hash == null) {
            res.redirect(Path.Web.INDEX);
            return halt();
        }

        User user = userDao.readByHash(hash);
        if (user == null) {
            res.redirect(Path.Web.INDEX);
            return halt();
        }

        userDao.deleteHash(user.getEmail());
        res.redirect(Path.Web.ACCOUNT_ACTIVATED);
        return halt();
    };

    public Route serveActivationSuccess = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Uspješna aktivacija - Passdirect");
        return ViewUtil.render(req, model, Path.Template.ACCOUNT_ACTIVATE);
    };


    public Route handleDeleteAccount = (Request req, Response res) -> {
        User tbd = req.session().attribute("user");
        userDao.delete(tbd);
        req.session().invalidate();
        res.redirect(Path.Web.INDEX);
        return halt();
    };


}