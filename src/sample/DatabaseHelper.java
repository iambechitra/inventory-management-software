package sample;

import java.sql.*;

public class DatabaseHelper {
    private Connection connection;
    private Statement statement;

    DatabaseHelper() throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:inventory.db");
        statement = connection.createStatement();
        System.out.println("Connection Stabilised!");
    }

    public void executeQuery(String sql) throws SQLException{ statement.executeUpdate(sql); }
    public ResultSet getResults(String sql) throws SQLException{ return statement.executeQuery(sql); }
    public boolean close() throws SQLException{
        if(!connection.isClosed())
            connection.close();

        return true;
    }
}
