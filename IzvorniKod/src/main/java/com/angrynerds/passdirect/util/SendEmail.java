package com.angrynerds.passdirect.util;

import com.angrynerds.passdirect.config.*;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail {

    public static void sendMail(String userEmail, String subject, String text) {
        //PODACI O POŠILJATELJU, mail s kojeg apk šalje user-u verifikacijski link, namjesteno na gmail
        String email = Env.Email.USERNAME;
        String password = Env.Email.PASSWORD;

        Properties properties = new Properties();

        // povezano s gmail-om
        properties.put("mail.smtp.auth", Env.Email.MAIL_SMTP_AUTH);
        properties.put("mail.smtp.starttls.enable", Env.Email.MAIL_SMTP_STARTTLS_ENABLE);
        properties.put("mail.smtp.host", Env.Email.MAIL_SMTP_HOST);
        properties.put("mail.smtp.port", Env.Email.MAIL_SMTP_PORT);
        properties.put("mail.smtp.ssl.trust", Env.Email.MAIL_SMTP_SSL_TRUST);

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        //PODACI O PRIMATELJU I ŠTO SE PRIMA
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("passdirect@passdirect.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Sendmail Exception\n" + e);
        }          

    }

    public static void sendActivationMail(String userEmail, String userHash) {
        sendMail(userEmail, "PASSDIRECT - Aktivacija računa", "Kliknite na ovaj link da potvrdite račun :: " 
                                    + Env.Email.APPLICATION_PROTOCOL + "://" + Env.Email.APPLICATION_HOST + Path.Web.ACTIVATE 
                                    + "?hash=" + userHash);        
    }

    public static void sendTicketBoughtMail(String userEmail, String start, String destination, String datVrijPolaska, float price) {
        String text = "Zahvaljujemo Vam na kupnji karte.\n" + 
                            "Vaš vlak na relaciji " + start + " - " + destination + " kreće " + datVrijPolaska + "\n" +
                            "Cijena karte: " + price + "kn\n" +
                            "Želimo Vam ugodno putovanje!";
        sendMail(userEmail, "PASSDIRECT - Karta", text);            
    }

    public static void sendTicketPosition(String userEmail, String start, String destination, String datVrijPolaska, int rbrVagon, String pozicija, String oznakaVlak) {
        String text = "Vaš vlak na relaciji " + start + " - " + destination + " koji kreće " 
                        + datVrijPolaska + " je krenuo s prethodnog stajališta!\nMolimo Vas da " 
                        + "sjednete u " + pozicija + " dio " + rbrVagon + ". vagona.\nHvala!";
        sendMail(userEmail, "PASSDIRECT - Vlak " + oznakaVlak, text);          
    }
}