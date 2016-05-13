/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Tiffany
 *
 */
public class DatabaseConnector {
	
	private Connection conn = null;
	private String dbuser = "CS1434872";
	private String dbname = "waldo2.dawsoncollege.qc.ca";
	private String dbpassword = "nickleck";

	public DatabaseConnector(String dbname, String dbuser, String dbpassword) throws SQLException {
		try {
			System.out.println("Creating connection...");
			conn = DriverManager.getConnection(dbname, dbuser, dbpassword);
			System.out.println("Connection created successfully");
		} catch (SQLException e) {
			System.out.println("Creating connection failed");
			throw e;
		}
	}
	
	/**
	 * Get the connection to the db
	 * @return Connection
	 */
	public Connection getConnection() throws SQLException {
		return conn;
	}
	
	/**
	 * Closes connection to database
	 */
	public void closeConn() throws SQLException {
		try {
			System.out.println("Closing connection");
			conn.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			System.out.println("Could not close connection");
			throw e;
		}
	}
	
	/**
	 * Executes an update statement and closes it
	 * 
	 * @param preparedStatement
	 * @throws SQLException
	 */
	public void executeUpdateStatement(PreparedStatement preparedStatement) throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}
	}
	
	/**
	 * Returns a prepared statement a given string
	 * 
	 * @param statement
	 * @return The preparedStatement
	 */
	public PreparedStatement prepareStatement(String statement) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(statement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			System.out.println("Error: could not execute prepared statement");
		}
		return stmt;
	}
	
	/**
	 * Gets results from an executed SQL statement. If the statement does not
	 * return anything, null will be returned. 
	 * 
	 * @param preparedStatement
	 * @return The resultset of the executed statement
	 */
	public ResultSet executeStatement(PreparedStatement preparedStatement) {

		if (preparedStatement == null || preparedStatement.equals("")) {
			return null;
		}

		ResultSet rs = null;

		try {
			rs = preparedStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error executing query");
		}

		return rs;
	}

}
