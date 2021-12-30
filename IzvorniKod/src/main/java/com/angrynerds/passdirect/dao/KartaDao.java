package com.angrynerds.passdirect.dao;

import com.angrynerds.passdirect.entity.*;

import java.sql.*;
import java.util.*;

public class KartaDao {
  private Database database;

  public KartaDao(Database database) {
    this.database = database;
  }

  public void create(Karta karta) {
    String sql = "INSERT INTO karta(oznakakarta, pozicija, rbrVagon, email) values(?, ?, ?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, karta.getOznakaKarta());
      stmt.setString(2, karta.getPozicija());
      stmt.setInt(3, karta.getRbrVagon());
      stmt.setString(4, karta.getEmail());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Karta read(int oznakaKarta) {
    Karta ret = null;
    String sql = "SELECT * FROM karta WHERE oznakaKarta = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, oznakaKarta);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String email = rs.getString("email");
        String pozicija = (String) rs.getObject("pozicija");
        int rbrVagon = rs.getInt("rbrvagon");
        ret = new Karta(oznakaKarta, pozicija, rbrVagon, email);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

   public int maxOznakaKarta() {
    int ret = 0;
    String sql = "SELECT MAX(oznakaKarta) cnt FROM karta;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        ret = rs.getInt("cnt");
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void update(Karta karta) {
    String sql = "UPDATE karta SET pozicija = ?, rbrVagon = ?, email = ? WHERE oznakakarta = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, karta.getPozicija());
      stmt.setInt(2, karta.getRbrVagon());
      stmt.setString(3, karta.getEmail());
      stmt.setInt(4, karta.getOznakaKarta());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public List<Karta> readAll() {
    List<Karta> ret = new ArrayList<Karta>();
    String sql = "SELECT * FROM karta;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String email = rs.getString("email");
        int oznakaKarta = rs.getInt("oznakakarta");
        String pozicija = (String) rs.getObject("pozicija");
        int rbrVagon = rs.getInt("rbrvagon");
        ret.add(new Karta(oznakaKarta, pozicija, rbrVagon, email));
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public List<Karta> readNesmjestene() {
    List<Karta> ret = new ArrayList<Karta>();
    String sql = "SELECT * FROM karta WHERE pozicija IS NULL;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String email = rs.getString("email");
        int oznakaKarta = rs.getInt("oznakakarta");
        String pozicija = (String) rs.getObject("pozicija");
        int rbrVagon = rs.getInt("rbrvagon");
        ret.add(new Karta(oznakaKarta, pozicija, rbrVagon, email));
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void insertVoznje(Karta karta, List<Voznja> voznje) {
    String sqlPrep = "DELETE FROM zaVoznju WHERE oznakaKarta = ?";
    String sql = "INSERT INTO zaVoznju(oznakakarta, oznakaVlak, oznakaStanica) values(?, ?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sqlPrep);
      stmt.setInt(1, karta.getOznakaKarta());
      stmt.executeUpdate();
      stmt.close();
      for (Voznja v : voznje) {
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, karta.getOznakaKarta());
        stmt.setString(2, v.getOznakaVlak());
        stmt.setInt(3, v.getStanica().getOznakaStanica());
        stmt.executeUpdate();
        stmt.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public List<TransactionInfo> readAllAdmin() {
    List<TransactionInfo> ret = new ArrayList<>();
    String sql = "SELECT email, oznakaKarta, SUM(cijenaVoznja) cijena, ime, prezime FROM korisnik NATURAL JOIN karta NATURAL JOIN zavoznju NATURAL JOIN voznja GROUP BY oznakaKarta, email;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String email = rs.getString("email");
        int oznakaKarta = rs.getInt("oznakakarta");
        String ime = rs.getString("ime");
        String prezime = rs.getString("prezime");
        float cijena = rs.getFloat("cijena");
        ret.add(new TransactionInfo(ime, prezime, email, oznakaKarta, cijena));
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }
}
