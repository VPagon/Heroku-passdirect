package com.angrynerds.passdirect.dao;

import com.angrynerds.passdirect.entity.User;

import java.sql.*;
import java.util.*;

public class UserDao {
  private Database database;

  public UserDao(Database database) {
    this.database = database;
  }

  public void create(User user) {
    String sql = "INSERT INTO korisnik(ime, prezime, email, lozinka, oznakauloga) values(?, ?, ?, ?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, user.getIme());
      stmt.setString(2, user.getPrezime());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getLozinka());
      stmt.setString(5, user.getOznakaUloga());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public User read(String email) {
    User ret = null;
    String sql = "SELECT * FROM korisnik WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String nemail = rs.getString("email");
        String nlozinka = rs.getString("lozinka");
        String nime = rs.getString("ime");
        String nprezime = rs.getString("prezime");
        String nuloga = rs.getString("oznakauloga");
        ret = new User(nime, nprezime, nemail, nlozinka, nuloga);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void createHash(String email, String hash) {
    String sql = "INSERT INTO aktivacija(email, hash) values(?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, email);
      stmt.setString(2, hash);
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public User readByHash(String hash) {
    User ret = null;
    String sql = "SELECT * FROM korisnik NATURAL JOIN aktivacija WHERE hash = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, hash);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String nemail = rs.getString("email");
        String nlozinka = rs.getString("lozinka");
        String nime = rs.getString("ime");
        String nprezime = rs.getString("prezime");
        String nuloga = rs.getString("oznakauloga");
        ret = new User(nime, nprezime, nemail, nlozinka, nuloga);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public String readHash(String email) {
    String ret = null;
    String sql = "SELECT * FROM aktivacija WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        ret = rs.getString("hash");
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void deleteHash(String email) {
    String sql = "DELETE FROM aktivacija WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, email);
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<User> readAll() {
    List<User> ret = new ArrayList<>();
    String sql = "SELECT * FROM korisnik ORDER BY prezime, ime;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String nemail = rs.getString("email");
        String nlozinka = rs.getString("lozinka");
        String nime = rs.getString("ime");
        String nprezime = rs.getString("prezime");
        String nuloga = rs.getString("oznakauloga");
        ret.add(new User(nime, nprezime, nemail, nlozinka, nuloga));
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void update(User user) {
    String sql = "UPDATE korisnik SET ime = ?, prezime = ?, lozinka = ?, oznakauloga = ? WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, user.getIme());
      stmt.setString(2, user.getPrezime());
      stmt.setString(3, user.getLozinka());
      stmt.setString(4, user.getOznakaUloga());
      stmt.setString(5, user.getEmail());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(User user) {
    String sql = "DELETE FROM korisnik WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, user.getEmail());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
