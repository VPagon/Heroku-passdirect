package com.angrynerds.passdirect.util;

import com.angrynerds.passdirect.config.Path;
import com.angrynerds.passdirect.dao.UserDao;
import com.angrynerds.passdirect.entity.User;

import spark.*;
import static spark.Spark.*;

public class AuthenticationFilter {
  private UserDao userDao;

  public AuthenticationFilter(UserDao userDao) {
    this.userDao = userDao;
  }

  public Filter checkUserAuth = (Request req, Response res) -> {
    User user = req.session().attribute("user");
    if (user == null || userDao.read(user.getEmail()) == null) {
      req.session().invalidate();
      res.redirect(Path.Web.LOGIN);
      halt();
    }
  };

  public Filter checkAdminAuth = (Request req, Response res) -> {
    User user = req.session().attribute("user");
    if (user == null || userDao.read(user.getEmail()) == null) {
      req.session().invalidate();
      res.redirect(Path.Web.LOGIN);
      halt();
    }

    if (!user.getOznakaUloga().equals("A")) {
      res.redirect(Path.Web.LOGIN);
      halt();
    }
  };

  public Filter prohibitLoggedIn = (Request req, Response res) -> {
    User user = req.session().attribute("user");
    if (user != null && userDao.read(user.getEmail()) != null) {
      res.redirect(Path.Web.HOME);
      halt();
    }
  };

  public Filter checkActivation = (Request req, Response res) -> {
    User user = req.session().attribute("user");

    if (user == null || userDao.read(user.getEmail()) == null) {
      req.session().invalidate();
      res.redirect(Path.Web.LOGIN);
      halt();
    }
  
    if (userDao.readHash(user.getEmail()) != null) {
      res.redirect(Path.Web.ACTIVATE_PROMPT);
      halt();
    }
  };
}
