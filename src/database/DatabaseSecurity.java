/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import business.Admin;
import business.Doctor;
import business.Patient;
import business.Type;
import business.User;
import views.MainApp;

/**
 * @author Tiffany
 *
 */
public class DatabaseSecurity extends MainApp{

	private Type type = null;
	
	/**
	 * Checks whether an account exists
	 * 
	 * @param username
	 * @param password
	 * @return bololean
	 */
	public boolean login(String username, String password) {
		if (username == null || username.trim().equals("")) {
			return false;
		}

		String pass;
		try {
			// Create statement
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT password, type, user_id "
							+ "FROM users "
							+ "WHERE username=?;");

			stmt.setString(1, username);

			// Execute statement
			ResultSet rs = conn.executeStatement(stmt);
			rs.next();

			//get password
			pass = rs.getString(1);
			// get type
			type = Type.valueOf(rs.getString(2));
			// get user id
			user_id = rs.getInt(3);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error with login");
			return false;
		}

		return (pass.equals(password));
	}
	
	/**
	 * Creates an account for a given user name. If the user name already
	 * exists, or is not a valid user name, nothing will happen. Invalid user
	 * names include empty strings and null values
	 * 
	 * @param username
	 *            The user name
	 * @param password
	 *            The password
	 * @return True if the user was added to the database. False otherwise
	 */
	public boolean newUser(User user, String username, String password) {
		if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
			System.out.println("Username or password cannot be empty");
			return false;
		}

		// User does not exist
		if (!login(username, password)) {
			PreparedStatement preparedStmt = conn
					.prepareStatement("INSERT INTO users VALUES " + "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);");


			try {
				preparedStmt.setString(1, username);
				preparedStmt.setString(2, password);
				preparedStmt.setString(3, user.getFirstname());
				preparedStmt.setString(4, user.getLastname());
				preparedStmt.setString(5, user.getPhone());
				preparedStmt.setString(6, user.getEmail());			

				switch (user.getType()) {
				case Admin:
					preparedStmt.setFloat(7, ((Admin) user).getSalary());
					preparedStmt.setObject(8, null);
					break;
				case Doctor:
					preparedStmt.setFloat(7, ((Doctor) user).getSalary());
					preparedStmt.setObject(8, null);
					break;
				case Patient:
					preparedStmt.setObject(7, null);
					preparedStmt.setObject(8, ((Patient) user).getNotes());
					break;
				default:
					System.out.println("Something's weird");
					break;
				}
				preparedStmt.setString(9, user.getType().toString());
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
				System.out.println("Error preparing statement");
			}

			try {
				// Add user
				conn.executeUpdateStatement(preparedStmt);
				System.out.println("Successfully added user " + username);
				return true;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
				System.out.println("Error adding user");
				return false;
			}
		}
		return false;
	}
	
	public Type getType() {
		return type;
	}
}
