package databases;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Template Java code for the Mini Project assignment. ONLY MODIFY THE CODE WITHIN THE TODO/end TODO
 * BLOCKS! The automated marking system relies on the structure of the code remaining the same.
 */
public class MiniProject {

  /**
   * Execute the first query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
  public static Map<String, Integer> firstQuery(Connection connection) throws SQLException {

    System.out.println("################## 1st Query ###############");

    Map<String, Integer> results = new LinkedHashMap<>();

    // TODO - add code to perform the query and return the results 
    // - remember to close the statement and result set
    // end TODO

    // SQL query to list the top 5 airlines with the fewest delays
    String querySql = "SELECT uniqueCarrier, COUNT(*) as delay_count " + "FROM delayedFlights "
        + "WHERE depDelay IS NOT NULL OR arrDelay IS NOT NULL " + "GROUP BY uniqueCarrier "
        + "ORDER BY delay_count ASC " + "LIMIT 5";

    // Use try-with-resources to ensure Statement and ResultSet are properly closed
    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql)) {

      // Process the result set and populate the results map
      while (resultSet.next()) {
        String airline = resultSet.getString("uniqueCarrier");
        int delayCount = resultSet.getInt("delay_count");

        results.put(airline, delayCount);
      }
    } catch (SQLException e) {
      // Handle exceptions as needed
      e.printStackTrace();
      throw e;
    }

    return results;
  }

  /**
   * Execute the second query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
  public static Map<String, Integer> secondQuery(Connection connection) throws SQLException {

    System.out.println("################## 2nd Query ###############");

    Map<String, Integer> results = new LinkedHashMap<>();

    // TODO - add code to perform the query and return the results 
    // - remember to close the statement and result set

    String querySql = "SELECT a.state, COUNT(*) AS delayCount " + "FROM delayedFlights d "
        + "JOIN airport a ON d.dest = a.airportCode " + "WHERE d.depDelay > 0 OR d.arrDelay > 0 "
        + "GROUP BY a.state "
        + "ORDER BY delayCount DESC " + "LIMIT 5";


    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql)) {

      // Process the result set and populate the results map
      while (resultSet.next()) {
        String state = resultSet.getString("state");
        int delayCount = resultSet.getInt("delayCount");

        results.put(state, delayCount);
      }
    }

    // end TODO
    return results;
  }

  /**
   * Execute the third query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
  public static Map<String, Integer> thirdQuery(Connection connection) throws SQLException {

    System.out.println("################## 3rd Query ###############");

    Map<String, Integer> results = new LinkedHashMap<>();

    // TODO - add code to perform the query and return the results 
    String querySql = "SELECT a.airportName, SUM(d.depDelay) AS totalDepDelay "
        + "FROM delayedFlights d " + "JOIN airport a ON d.origin = a.airportCode "
        + "WHERE d.month = 5 AND d.depDelay > 0 " + "GROUP BY a.airportName "
        + "ORDER BY totalDepDelay DESC " + "LIMIT 3";

    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql)) {

      while (resultSet.next()) {
        String airportName = resultSet.getString("airportName");
        int totalDepDelay = resultSet.getInt("totalDepDelay");
        results.put(airportName, totalDepDelay);
      }
    }
    // - remember to close the statement and result set
    // end TODO

    return results;
  }


  /**
   * Execute the fourth query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
  public static Map<String, Integer> fourthQuery(Connection connection) throws SQLException {

    System.out.println("################## 4th Query ###############");

    Map<String, Integer> results = new LinkedHashMap<>();

    // TODO - add code to perform the query and return the results
    String querySql = "SELECT a.airportName, d.depDelay " + "FROM delayedFlights d "
        + "JOIN airport a ON d.origin = a.airportCode "
        + "WHERE d.distance > 1500 AND d.depDelay > 240 " + "ORDER BY d.depDelay DESC";

    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql)) {

      while (resultSet.next()) {
        String airportName = resultSet.getString("airportName");
        int depDelay = resultSet.getInt("depDelay");
        results.put(airportName, depDelay);
      }
    }
    // - remember to close the statement and result set
    // end TODO

    return results;
  }

  /**
   * Execute the fifth query.
   * 
   * @param connection a database connection
   * @return the results of the query
   * @throws SQLException if a problem occurs when executing the query
   */
  public static Map<String, Integer> fifthQuery(Connection connection) throws SQLException {

    System.out.println("################## 5th Query ###############");

    Map<String, Integer> results = new LinkedHashMap<>();

    // TODO - add code to perform the query and return the results 
    String querySql = "SELECT a.state, COUNT(*) AS delayCount " + "FROM delayedFlights d "
        + "JOIN airport a ON d.origin = a.airportCode "
        + "JOIN airport b ON d.dest = b.airportCode "
        + "WHERE a.state = b.state AND d.depDelay > 120 " + "GROUP BY a.state "
        + "ORDER BY delayCount DESC " + "LIMIT 3 ";
    // - remember to close the statement and result set
    // end TODO

    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql)) {

      while (resultSet.next()) {
        String state = resultSet.getString("state");
        int delayCount = resultSet.getInt("delayCount");
        results.put(state, delayCount);
      }
    }

    return results;
  }

  /**
   * Create the airport table.
   * 
   * @param connection a database connection
   */
  public static void createAirportTable(Connection connection) throws SQLException {
    System.out.println("Creating airport table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement = connection
        .prepareStatement("CREATE TABLE airport (\n" + "airportCode varchar(5) PRIMARY KEY, \n"
            + "airportName varchar(100), \n" + "city varchar(50), \n" + "state varchar(10));");) {
      statement.execute();
    }
  }

  /**
   * Create the airport table.
   * 
   * @param connection a database connection
   */
  public static void createDelayedFlightTable(Connection connection) throws SQLException {

    System.out.println("Creating delayedFlights table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE  delayedFlights \n"
        + "(delayId int PRIMARY KEY, \n" + "month int, \n" + "dayOfMonth int, \n"
        + "dayOfWeek int, \n" + "actualDepTime int, \n" + "scheduledDepTime int, \n"
        + "actualArrTime int,\n" + "scheduledArrTime int, \n" + "uniqueCarrier varchar(5), \n"
        + "flightNum int,\n" + "actualFlightTime int, \n" + "scheduledFlightTime int, \n"
        + "airTime int, \n" + "arrDelay int, \n" + "depDelay int, \n" + "origin varchar(5),\n"
        + "FOREIGN KEY(origin) REFERENCES airport(airportCode), \n" + "dest varchar(5), \n"
        + "FOREIGN KEY(dest) REFERENCES airport(airportCode), \n" + "distance int);");) {
      statement.execute();
    }
  }

  /**
   * Insert data into the airport table.
   * 
   * @param connection a database connection
   * @param file the file containing the data
   * @throws IOException if the file cannot be accessed
   * @throws SQLException if the data cannot be inserted
   */
  public static void insertIntoAirportTableFromFile(Connection connection, String file)
      throws IOException, SQLException {

    System.out.println("Inserting data into airport table");

    // TODO - complete the PreparedStatement with placeholder values
    String insertSql =
        "INSERT INTO airport (airportCode, airportName, city, state) VALUES (?, ?, ?, ?)"
            + "ON CONFLICT (airportCode) DO NOTHING";

    try (PreparedStatement statement = connection.prepareStatement(insertSql)) {

      // Insert for Abilene Regional Airport
      statement.setString(1, "ABI");
      statement.setString(2, "Abilene Regional Airport");
      statement.setString(3, "Abilene");
      statement.setString(4, "TX");
      statement.addBatch();

      // Insert for Adak Airport
      statement.setString(1, "ADK");
      statement.setString(2, "Adak Airport");
      statement.setString(3, "Adak");
      statement.setString(4, "AK");
      statement.addBatch();

      statement.executeBatch();
      System.out.println("Batch insert completed");

    }

    // stream, reader and statement will get closed here as we are using try-with-resources
    try (InputStream airportFile = MiniProject.class.getClassLoader().getResourceAsStream(file);
        BufferedReader br =
            new BufferedReader(new InputStreamReader(airportFile, StandardCharsets.UTF_8));

        PreparedStatement statement = connection.prepareStatement(insertSql);

    ) {
      // end TODO
      
      String currentLine = null;
      String[] brokenLine = null;

      while ((currentLine = br.readLine()) != null) {
        brokenLine = currentLine.split(",");
        int i;
        for (i = 0; i < brokenLine.length; i++) {
          statement.setString(i + 1, brokenLine[i]);
        }

        statement.addBatch();

      }

      statement.executeBatch();

    } catch (SQLException e) {
      throw e;
    }

  }




  /**
   * Insert data into the delayedFlights table.
   * 
   * @param connection a database connection
   * @param file the file containing the data
   * @throws IOException if the file cannot be accessed
   * @throws SQLException if the data cannot be inserted
   */
  public static void insertIntoDelayedFlightTableFromFile(Connection connection, String file)
      throws IOException, SQLException {

    System.out.println("Inserting data into delayedFlights table");

    String insert = "INSERT INTO delayedFlights VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // stream, reader and statement will get closed here as we are using try-with-resources
    try (InputStream delayedFlights = MiniProject.class.getClassLoader().getResourceAsStream(file);
        PreparedStatement statement = connection.prepareStatement(insert);
        BufferedReader br =
            new BufferedReader(new InputStreamReader(delayedFlights, StandardCharsets.UTF_8));) {

      String currentLine = null;
      String[] brokenLine = null;

      while ((currentLine = br.readLine()) != null) {
        brokenLine = currentLine.split(",");
        int i;
        for (i = 0; i < brokenLine.length; i++) {
          if (i == 8 || i == 15 || i == 16) {
            statement.setString(i + 1, brokenLine[i]); // varchar values
          } else {
            statement.setInt(i + 1, Integer.valueOf(brokenLine[i])); // int values
          }
        }

        statement.addBatch();
      }

      statement.executeBatch();

    } catch (SQLException e) {
      throw e;
    }

  }

  /**
   * Drop the airport table and any associated views/tables.
   * 
   * @param connection a database connection
   */
  public static void dropAirportTable(Connection connection) throws SQLException {

    System.out.println("Dropping airport table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement st =
        connection.prepareStatement("DROP TABLE IF EXISTS airport CASCADE");) {
      st.execute();
    }
  }

  /**
   * Drop the delayed flights table and any associated views/tables.
   * 
   * @param connection a database connection
   */
  public static void dropDelayedFlightTable(Connection connection) throws SQLException {

    System.out.println("Dropping delayedFlights table");

    // statement will get closed here as we are using try-with-resources
    try (PreparedStatement statement =
        connection.prepareStatement("DROP TABLE IF EXISTS delayedFlights CASCADE");) {
      statement.execute();
    }
  }


  /**
   * Connect to your Postgres database on teachdb.cs.rhul.ac.uk.
   * 
   * @param user your username
   * @param password your password
   * @param databaseHost the host name of the database server
   * @return a new database connection
   */
  public static Connection connectToDatabase(String user, String password, String databaseHost)
      throws SQLException {

    // TODO - add code to connect to the specified database here
    Connection connection = DriverManager
        .getConnection("jdbc:postgresql://" + databaseHost + "/CS2855%2F" + user, user, password);

    // end TODO
    System.out.println("Connection made successfully");
    return connection;
  }


  /**
   * Main method.
   * 
   * @param args any command line arguments
   */
  public static void main(String[] args) {

    Connection connection = null;

    // scanner will get closed here as we are using try-with-resources
    try (Scanner scanner = new Scanner(System.in);) {
      System.out.println("Please enter your username:");
      String user = scanner.nextLine();
      System.out.println("Please enter your database password:");
      String password = scanner.nextLine();
      System.out.println("Please enter the host name of the database you want to connect to:");
      String host = scanner.nextLine();

      connection = MiniProject.connectToDatabase(user, password, host);

      dropAirportTable(connection);
      dropDelayedFlightTable(connection);

      createAirportTable(connection);
      createDelayedFlightTable(connection);

      insertIntoAirportTableFromFile(connection, "airport.csv");
      insertIntoDelayedFlightTableFromFile(connection, "delayedFlights.csv");

      System.out.println(firstQuery(connection));
      System.out.println(secondQuery(connection));
      System.out.println(thirdQuery(connection));
      System.out.println(fourthQuery(connection));
      System.out.println(fifthQuery(connection));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        // always ensure connections are closed,
        // note we couldn't use a try-with-resources here
        // because we needed the username / password entered
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

}


