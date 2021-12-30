package com.angrynerds.passdirect;

import com.angrynerds.passdirect.config.*;
import com.angrynerds.passdirect.controller.*;
import com.angrynerds.passdirect.dao.*;
import com.angrynerds.passdirect.util.AuthenticationFilter;
import com.angrynerds.passdirect.util.ViewUtil;

import java.util.*;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");
        //staticFiles.expireTime(1000L);

        Database database = new Database();
        database.connect();
        UserDao userDao = new UserDao(database);
        VoznjaDao voznjaDao = new VoznjaDao(database);
        KarticaDao karticaDao = new KarticaDao(database);
        KartaDao kartaDao = new KartaDao(database);
        MjerenjeDao mjerenjeDao = new MjerenjeDao(database);

        IndexController indexController = new IndexController(voznjaDao);
        LoginController loginController = new LoginController(userDao);
        RegisterController registerController = new RegisterController(userDao);
        SearchController searchController = new SearchController(voznjaDao, mjerenjeDao);
        PaymentController paymentController = new PaymentController(karticaDao, kartaDao, voznjaDao);
        AccountController accountController = new AccountController(userDao);
        AdminController adminController = new AdminController(userDao, voznjaDao, kartaDao);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userDao);


        get(Path.Web.INDEX, indexController.serveIndexPage);
        before(Path.Web.INDEX, authenticationFilter.prohibitLoggedIn);
        get(Path.Web.HOME, indexController.serveFunctionsPage);
        before(Path.Web.HOME, authenticationFilter.checkActivation, authenticationFilter.checkUserAuth);

        get(Path.Web.LOGIN, loginController.serveLoginPage);
        post(Path.Web.LOGIN, loginController.handleLoginPost);
        before(Path.Web.LOGIN, authenticationFilter.prohibitLoggedIn);
        get(Path.Web.LOGOUT, loginController.handleLogoutGet);

        get(Path.Web.REGISTER, registerController.serveRegisterPage);
        post(Path.Web.REGISTER, registerController.handleRegisterPost);
        before(Path.Web.REGISTER, authenticationFilter.prohibitLoggedIn);

        get(Path.Web.STATION, searchController.serverStationPage);
        before(Path.Web.STATION, authenticationFilter.checkActivation, authenticationFilter.checkUserAuth);
        get(Path.Web.TIME_TABLE_USER, searchController.serveTimeTablePage);
        before(Path.Web.TIME_TABLE_USER, authenticationFilter.checkActivation, authenticationFilter.checkUserAuth);

        get(Path.Web.ADMIN, adminController.serveAdminPage);
        before(Path.Web.ADMIN, authenticationFilter.checkActivation, authenticationFilter.checkAdminAuth);

        get(Path.Web.ADMIN_USERS, adminController.serveUsersPage);
        before(Path.Web.ADMIN_USERS, authenticationFilter.checkActivation, authenticationFilter.checkAdminAuth);
        get(Path.Web.ADMIN_DELETE_USER, adminController.handleDeleteUser);
        before(Path.Web.ADMIN_DELETE_USER, authenticationFilter.checkActivation, authenticationFilter.checkAdminAuth);
        get(Path.Web.ADMIN_TRANSACTIONS, adminController.serveTransactionsPage);
        before(Path.Web.ADMIN_TRANSACTIONS, authenticationFilter.checkActivation, authenticationFilter.checkAdminAuth);
        get(Path.Web.ADMIN_TIME_TABLE, adminController.serveTimeTablePage);
        before(Path.Web.ADMIN_TIME_TABLE, authenticationFilter.checkActivation, authenticationFilter.checkAdminAuth);

        get(Path.Web.PAYMENT_PROMPT, paymentController.servePaymentPage);
        post(Path.Web.PAYMENT_PROMPT, paymentController.handlePaymentPost);
        before(Path.Web.PAYMENT_PROMPT, authenticationFilter.checkActivation, authenticationFilter.checkUserAuth);

        get(Path.Web.ACTIVATE_PROMPT, accountController.serveActivationPrompt);
        before(Path.Web.ACTIVATE_PROMPT, authenticationFilter.checkUserAuth);
        get(Path.Web.ACTIVATE, accountController.handleActivationGet);
        before(Path.Web.ACTIVATE, authenticationFilter.checkUserAuth);
        get(Path.Web.ACCOUNT_ACTIVATED, accountController.serveActivationSuccess);
        before(Path.Web.ACCOUNT_ACTIVATED, authenticationFilter.checkUserAuth);
        get(Path.Web.DELETE_ACCOUNT, accountController.handleDeleteAccount);
        before(Path.Web.DELETE_ACCOUNT, authenticationFilter.checkActivation, authenticationFilter.checkUserAuth);

        startGotcha();
        startRasporedjivac();
    }

    private static void startGotcha() {
        final Database gotcha_db = new Database();
        gotcha_db.connect();
        final MjerenjeDao gotcha_mjerenjeDao = new MjerenjeDao(gotcha_db);
        Thread gotcha = new Thread() {
            @Override
            public void run() {
                GotchaSenzori gs = new GotchaSenzori(gotcha_mjerenjeDao);
                gs.provjeraNovihVlakova();
            }
        };
        gotcha.start();
    }

    private static void startRasporedjivac() {
        final Database rasp_db = new Database();
        rasp_db.connect();
        final VoznjaDao rasp_voznjaDao = new VoznjaDao(rasp_db);
        final MjerenjeDao rasp_mjerenjeDao = new MjerenjeDao(rasp_db);
        final KartaDao rasp_kartaDao = new KartaDao(rasp_db);
        Thread rasporedjivac = new Thread() {
            @Override
            public void run() {
                Rasporedjivac rasp = new Rasporedjivac(rasp_kartaDao, rasp_voznjaDao, rasp_mjerenjeDao);
                rasp.rasporedi();
            }
        };
        rasporedjivac.start();
    }
}