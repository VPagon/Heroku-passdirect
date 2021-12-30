package com.angrynerds.passdirect.dao;

import com.angrynerds.passdirect.entity.*;

import java.sql.*;
import java.util.*;

public class MjerenjeDao {
  private Database database;

  public MjerenjeDao(Database database) {
    this.database = database;
  }

  public void create(Mjerenje mjerenje) {
    String sql = "INSERT INTO mjerenje(oznakavlak, oznakastanica, kgfront, kgback, rbrvagon, datvrijmjerenja) values(?, ?, ?, ?, ?, ?);";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, mjerenje.getOznakaVlak());
      stmt.setInt(2, mjerenje.getOznakaStanica());
      stmt.setInt(3, mjerenje.getKgFront());
      stmt.setInt(4, mjerenje.getKgBack());
      stmt.setInt(5, mjerenje.getRbrVagon());
      stmt.setTimestamp(6, new java.sql.Timestamp(mjerenje.getDatVrijMjerenja().getTime()));
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Mjerenje read(String oznakaVlak, int oznakaStanica, int rbrVagon) {
    Mjerenje ret = null;
    String sql = "SELECT * FROM mjerenje WHERE oznakavlak = ? AND oznakastanica = ? AND rbrvagon = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, oznakaVlak);
      stmt.setInt(2, oznakaStanica);
      stmt.setInt(3, rbrVagon);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        int kgFront = rs.getInt("kgFront");
        int kgBack = rs.getInt("kgBack");
        java.util.Date datVrijMjerenja = rs.getTimestamp("datvrijmjerenja");
        ret = new Mjerenje(oznakaVlak, oznakaStanica, kgFront, kgBack, rbrVagon, datVrijMjerenja);
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public List<Mjerenje> read(String oznakaVlak, int oznakaStanica) {
    List<Mjerenje> ret =new ArrayList<Mjerenje>();
    String sql = "SELECT * FROM mjerenje WHERE oznakavlak = ? AND oznakastanica = ? ORDER BY rbrvagon ASC;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, oznakaVlak);
      stmt.setInt(2, oznakaStanica);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int kgFront = rs.getInt("kgFront");
        int kgBack = rs.getInt("kgBack");
        int rbrVagon = rs.getInt("rbrVagon");
        java.util.Date datVrijMjerenja = rs.getTimestamp("datvrijmjerenja");
        ret.add(new Mjerenje(oznakaVlak, oznakaStanica, kgFront, kgBack, rbrVagon, datVrijMjerenja));
      }
      rs.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public void update(Mjerenje mjerenje) {
    String sql = "UPDATE mjerenje SET kgFront = ?, kgBack = ?, datvrijmjerenja = ? WHERE oznakaVlak = ? AND oznakaStanica = ? AND rbrVagon = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, mjerenje.getKgFront());
      stmt.setInt(2, mjerenje.getKgBack());
      stmt.setTimestamp(3, new java.sql.Timestamp(mjerenje.getDatVrijMjerenja().getTime()));
      stmt.setString(4, mjerenje.getOznakaVlak());
      stmt.setInt(5, mjerenje.getOznakaStanica());
      stmt.setInt(6, mjerenje.getRbrVagon());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Mjerenje mjerenje) {
    String sql = "DELETE FROM mjerenje WHERE oznakaVlak = ? AND oznakaStanica = ? AND rbrVagon = ?;";
    try {
      Connection connection = database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, mjerenje.getOznakaVlak());
      stmt.setInt(2, mjerenje.getOznakaStanica());
      stmt.setInt(3, mjerenje.getRbrVagon());
      stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
