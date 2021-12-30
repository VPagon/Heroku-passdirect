package com.angrynerds.passdirect.dao;

import com.angrynerds.passdirect.entity.*;

import java.sql.*;
import java.util.*;

public class KarticaDao {
  private Database database;

  public KarticaDao(Database database) {
    this.database = database;
  }

  public void create(Kartica kartica) {
    String sql = "INSERT INTO kartica(brojkartice, imeprezvlasnik, email) values(?, ?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, kartica.getBrojKartice());
      stmt.setString(2, kartica.getImePrezVlasnik());
      stmt.setString(3, kartica.getEmail());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Kartica read(String email) {
    Kartica ret = null;
    String sql = "SELECT * FROM kartica WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String brojKartice = rs.getString("brojkartice");
        String imePrezVlasnik = rs.getString("imeprezvlasnik");
        ret = new Kartica(brojKartice, imePrezVlasnik, email);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void update(Kartica kartica) {
    String sql = "UPDATE kartica SET brojKartice = ?, imePrezVlasnik = ? WHERE email = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, kartica.getBrojKartice());
      stmt.setString(2, kartica.getImePrezVlasnik());
      stmt.setString(3, kartica.getEmail());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
