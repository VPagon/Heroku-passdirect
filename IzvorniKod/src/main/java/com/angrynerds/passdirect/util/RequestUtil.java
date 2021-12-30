package com.angrynerds.passdirect.util;

import com.angrynerds.passdirect.entity.User;

import spark.Request;

public class RequestUtil {

    // korištene
    public static String getQueryFirstname(Request req) {
        return req.queryParams("firstname");
    }

    public static String getQueryLastname(Request req) {
        return req.queryParams("lastname");
    }

    public static String getQueryEmail(Request req) {
        return req.queryParams("email");
    }

    public static String getQueryPassword(Request req) {
        return req.queryParams("password");
    }

    public static String getQueryPasswordRepeat(Request req) {
        return req.queryParams("password-repeat");
    }
}
