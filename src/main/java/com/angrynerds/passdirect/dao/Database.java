package com.angrynerds.passdirect.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.angrynerds.passdirect.config.Env;

public class Database {
  private Connection connection;

  public Database() {
  }

  public void connect() {
    try {
      connection = DriverManager.getConnection(Env.Database.URL, Env.Database.USERNAME, Env.Database.PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    try {
      if (connection != null && !connection.isClosed())
        connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
