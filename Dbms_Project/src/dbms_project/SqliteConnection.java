package dbms_project;

import java.sql.*;
public class SqliteConnection {
  public static Connection Connector() {
 try {
  Class.forName("org.sqlite.JDBC");
  Connection conn =DriverManager.getConnection("jdbc:sqlite://home//sarbjyot//Project.sqlite");
  return conn;
 } catch (Exception e) {
  System.out.println(e);
  return null;
  // TODO: handle exception
 }
}
}