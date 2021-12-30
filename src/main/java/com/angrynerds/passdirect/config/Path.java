package com.angrynerds.passdirect.config;

public class Path {
  public static class Web {
    public static final String INDEX = "/";
    public static final String HOME = "/home";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String STATION = "/station";
    public static final String ADMIN = "/admin";
    public static final String ADMIN_USERS = "/admin/users";
    public static final String ADMIN_DELETE_USER = "/admin/users/delete/:user";
    public static final String ADMIN_TRANSACTIONS = "/admin/transactions";
    public static final String ADMIN_TIME_TABLE = "/admin/time-table";
    public static final String TIME_TABLE_USER = "/time-table-user";
    public static final String PAYMENT_PROMPT = "/payment/:oznVlak/:start/:dest";
    public static final String ACTIVATE = "/activate";
    public static final String ACTIVATE_PROMPT = "/not-activated";
    public static final String ACCOUNT_ACTIVATED = "/activated";
    public static final String DELETE_ACCOUNT = "/delete-account";
  }

  public static class Template {
    public static final String INDEX = "/template/index/index.vm";
    public static final String REGISTER = "/template/register/register.vm";
    public static final String LOGIN = "/template/login/login.vm";
    public static final String HOME = "/template/home/home.vm";
    public static final String STATION = "/template/station/station.vm";
    public static final String ADMIN = "/template/admin/admin.vm";
    public static final String ADMIN_USERS = "/template/users/users.vm";
    public static final String ADMIN_TRANSACTIONS = "/template/transactions/transactions.vm";
    public static final String ADMIN_TIME_TABLE = "/template/time-table/time-table.vm";
    public static final String TIME_TABLE_USER = "/template/time-table-user/time-table-user.vm";
    public static final String PAYMENT_PROMPT = "/template/payment/payment.vm";
    public static final String PAYMENT_END = "/template/payment-end/payment-end.vm";
    public static final String ACTIVATE_PROMPT = "/template/confirmation/confirmation.vm";
    public static final String ACCOUNT_ACTIVATE = "/template/confirmation-end/confirmation-end.vm";
  }
}
