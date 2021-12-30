package com.angrynerds.passdirect.controller;

import com.angrynerds.passdirect.util.*;
import com.angrynerds.passdirect.entity.*;
import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.dao.*;

import spark.*;
import static com.angrynerds.passdirect.util.RequestUtil.*;
import static spark.Spark.*;

import java.util.*;

public class RegisterController {
    private UserDao userDao;

    public RegisterController(UserDao userDao) {
        this.userDao = userDao;
    }

    public Route serveRegisterPage = (Request req, Response res) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("pageTitle", "Registracija - Passdirect");
        return ViewUtil.render(req, model, Path.Template.REGISTER);
    };

    public Route handleRegisterPost = (Request req, Response res) -> {
        // provjeri ispravnost unesenih podataka
        final String email = getQueryEmail(req);
        if (email == null) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("pageTitle", "Registracija - Passdirect");
            model.put("registrationFailed", true);
            return ViewUtil.render(req, model, Path.Template.REGISTER);
        }

        User user = userDao.read(email);
        if (!checkFormData(req) || !(user == null)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("pageTitle", "Registracija - Passdirect");
            model.put("registrationFailed", true);
            return ViewUtil.render(req, model, Path.Template.REGISTER);
        }

        user = new User(getQueryFirstname(req), getQueryLastname(req), getQueryEmail(req), getQueryPassword(req), "K");
        userDao.create(user);

        // provjeri je li user uspjesno kreiran
        User user_created = userDao.read(email);
        if (user_created == null) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("pageTitle", "Registracija - Passdirect");
            model.put("registrationFailed", true);
            return ViewUtil.render(req, model, Path.Template.REGISTER);
        }

        final String uuid = UUID.randomUUID().toString();
        userDao.createHash(user.getEmail(), uuid);

        // pohrani podatke u session (atribut currentUser)
        req.session().attribute("user", user);

        // Slanje maila na novoj dretvi
        (new Thread() {
            @Override
            public void run() {
                SendEmail.sendActivationMail(email, uuid);
            }
        }).start();

        // redirect na naslovnu stranicu
        res.redirect(Path.Web.INDEX);
        return halt();
    };

    // provjera ispravnosti unesenih podataka u formu
    public static boolean checkFormData(Request req) {
        // ako je neko polje ostavljeno prazno
        if (getQueryFirstname(req) == null || getQueryLastname(req) == null || getQueryEmail(req) == null
                || getQueryPassword(req) == null || getQueryPasswordRepeat(req) == null) {
            return false;
        }
        if (getQueryFirstname(req).isEmpty() || getQueryLastname(req).isEmpty() || getQueryEmail(req).isEmpty()
                || getQueryPassword(req).isEmpty() || getQueryPasswordRepeat(req).isEmpty()) {
            return false;
        }
        // provjera emaila - mora sadržavati znak @
        if (getQueryEmail(req).indexOf('@') == -1) {
            return false;
        }
        // da li se lozinke podudaraju
        if (!getQueryPassword(req).equals(getQueryPasswordRepeat(req))) {
            return false;
        }
        // lozinka mora imati najmanje 8 znakova
        if (getQueryPassword(req).length() < 8) {
            return false;
        }

        return true;
    }
}
