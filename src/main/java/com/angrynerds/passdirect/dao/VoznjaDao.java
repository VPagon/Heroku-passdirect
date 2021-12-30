package com.angrynerds.passdirect.dao;

import com.angrynerds.passdirect.entity.*;

import java.sql.*;
import java.util.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class VoznjaDao {
     private Database database;

    public VoznjaDao(Database database) {
        this.database = database;
    }
 
    public List<Voznja> read(String oznakaVlak) {
        List<Voznja> ret = new ArrayList<Voznja>();
        String sql = "SELECT voznja.*, p.nazivstanica polNaz, o.nazivstanica odrNaz" +
                        " FROM voznja JOIN stanica p ON p.oznakastanica = voznja.oznakastanica" +
                        " JOIN stanica o ON o.oznakastanica = voznja.odredisteoznakastanica" +
                        " WHERE oznakaVlak = ? ORDER BY datVrijPolaska ASC;";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, oznakaVlak);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float cijVoz = rs.getFloat("cijenavoznja");
                java.util.Date datVrijPol = rs.getTimestamp("datvrijpolaska");
                java.util.Date datVrijDol = rs.getTimestamp("datvrijdolaska");
                int oznSt = rs.getInt("oznakastanica");
                String nazSt = rs.getString("polnaz");
                Stanica pol = new Stanica(oznSt, nazSt);
                int oznOdrSt = rs.getInt("odredisteoznakastanica");
                String nazOdrSt = rs.getString("odrnaz");
                Stanica odr = new Stanica(oznOdrSt, nazOdrSt);
                String oznVlak = rs.getString("oznakaVlak");
                Voznja voz = new Voznja(cijVoz, datVrijPol, datVrijDol, pol, odr, oznVlak);
                ret.add(voz);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    } 


    /* 
    public Voznja read(String oznakaVlak, int oznakaStanica) {
        Voznja ret = null;
        String sql = "SELECT voznja.*, p.nazivstanica polNaz, o.nazivstanica odrNaz" +
                        " FROM voznja JOIN stanica p ON p.oznakastanica = voznja.oznakastanica" +
                        " JOIN stanica o ON o.oznakastanica = voznja.odredisteoznakastanica" +
                        " WHERE oznakaVlak = ? AND voznja.oznakaStanica = ? ORDER BY datVrijPolaska ASC;";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, oznakaVlak);
            stmt.setInt(2, oznakaStanica);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                float cijVoz = rs.getFloat("cijenavoznja");
                java.util.Date datVrijPol = rs.getTimestamp("datvrijpolaska");
                java.util.Date datVrijDol = rs.getTimestamp("datvrijdolaska");
                int oznSt = rs.getInt("oznakastanica");
                String nazSt = rs.getString("polnaz");
                Stanica pol = new Stanica(oznSt, nazSt);
                int oznOdrSt = rs.getInt("odredisteoznakastanica");
                String nazOdrSt = rs.getString("odrnaz");
                Stanica odr = new Stanica(oznOdrSt, nazOdrSt);
                String oznVlak = rs.getString("oznakaVlak");
                ret = new Voznja(cijVoz, datVrijPol, datVrijDol, pol, odr, oznVlak);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }*/

    public List<Voznja> readAll() {
        List<Voznja> ret = new ArrayList<Voznja>();
        String sql = "SELECT voznja.*, p.nazivstanica polNaz, o.nazivstanica odrNaz" +
                        " FROM voznja JOIN stanica p ON p.oznakastanica = voznja.oznakastanica" +
                        " JOIN stanica o ON o.oznakastanica = voznja.odredisteoznakastanica" +
                        " ORDER BY datVrijPolaska ASC;";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float cijVoz = rs.getFloat("cijenavoznja");
                java.util.Date datVrijPol = rs.getTimestamp("datvrijpolaska");
                java.util.Date datVrijDol = rs.getTimestamp("datvrijdolaska");
                int oznSt = rs.getInt("oznakastanica");
                String nazSt = rs.getString("polnaz");
                Stanica pol = new Stanica(oznSt, nazSt);
                int oznOdrSt = rs.getInt("odredisteoznakastanica");
                String nazOdrSt = rs.getString("odrnaz");
                Stanica odr = new Stanica(oznOdrSt, nazOdrSt);
                String oznVlak = rs.getString("oznakaVlak");
                Voznja voz = new Voznja(cijVoz, datVrijPol, datVrijDol, pol, odr, oznVlak);
                ret.add(voz);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<Voznja> readVoznjeByKarta(int oznakaKarta) {
        List<Voznja> ret = new ArrayList<Voznja>();
        String sql = "SELECT voznja.*, p.nazivstanica polNaz, o.nazivstanica odrNaz" +
                        " FROM voznja JOIN stanica p ON p.oznakastanica = voznja.oznakastanica" +
                        " JOIN stanica o ON o.oznakastanica = voznja.odredisteoznakastanica" +
                        " JOIN zaVoznju ON voznja.oznakaVlak = zaVoznju.oznakaVlak AND voznja.oznakaStanica = zaVoznju.oznakaStanica" +
                        " WHERE oznakaKarta = ? ORDER BY datVrijPolaska ASC;";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, oznakaKarta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float cijVoz = rs.getFloat("cijenavoznja");
                java.util.Date datVrijPol = rs.getTimestamp("datvrijpolaska");
                java.util.Date datVrijDol = rs.getTimestamp("datvrijdolaska");
                int oznSt = rs.getInt("oznakastanica");
                String nazSt = rs.getString("polnaz");
                Stanica pol = new Stanica(oznSt, nazSt);
                int oznOdrSt = rs.getInt("odredisteoznakastanica");
                String nazOdrSt = rs.getString("odrnaz");
                Stanica odr = new Stanica(oznOdrSt, nazOdrSt);
                String oznVlak = rs.getString("oznakaVlak");
                Voznja voz = new Voznja(cijVoz, datVrijPol, datVrijDol, pol, odr, oznVlak);
                ret.add(voz);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int readDelay(String oznakaVlak) {
        int ret = -1;
        String sql = "SELECT DISTINCT EXTRACT(EPOCH FROM (datVrijMjerenja - datVrijPolaska)) " +
                    " / 60 AS razlika FROM voznja NATURAL JOIN mjerenje WHERE oznakaVlak = ? " +
                    " AND oznakastanica = (SELECT MAX(oznakaStanica) FROM mjerenje WHERE oznakaVlak = voznja.oznakavlak);";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, oznakaVlak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("razlika");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public List<Stanica> readAllStations() {
        List<Stanica> ret = new ArrayList<>();
        String sql = "SELECT * FROM stanica WHERE oznakaStanica <> 0 ORDER BY oznakaStanica ASC";
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int oznSt = rs.getInt("oznakastanica");
                String nazSt = rs.getString("nazivstanica");
                ret.add(new Stanica(oznSt, nazSt));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}