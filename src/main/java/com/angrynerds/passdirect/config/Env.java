package com.angrynerds.passdirect.config;

import java.text.*;

public class Env {
  public static class Database {
    public static final String DB_NAME = "d9a56ft06o3ca9";
    public static final String URL = "jdbc:postgresql://ec2-34-253-116-145.eu-west-1.compute.amazonaws.com:5432/" + DB_NAME;
    public static final String USERNAME = "nwhnvftasscciy";
    public static final String PASSWORD = "f9676b6bf8dc2cbaa2e9cdaa44afb2b5ff5b39e7f508af4d311cc7a7c18a8101";
  } 

  public static class Email {
    public static final String USERNAME = "passdirectaplikacija@gmail.com";
    public static final String PASSWORD = "Passdirect2021#$&";
    public static final String MAIL_SMTP_AUTH = "true"; 
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "true";
    public static final String MAIL_SMTP_HOST = "smtp.gmail.com";
    public static final String MAIL_SMTP_PORT = "587";
    public static final String MAIL_SMTP_SSL_TRUST = "smtp.gmail.com";

    public static final String APPLICATION_PROTOCOL = "http";
    public static final String APPLICATION_HOST = "localhost:4567";
  }
}
