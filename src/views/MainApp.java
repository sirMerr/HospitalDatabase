/**
 * 
 */
package views;


import java.sql.SQLException;
import java.util.Scanner;

import business.Admin;
import business.Doctor;
import business.Patient;
import business.Type;
import database.Database;
import database.DatabaseConnector;
import database.DatabaseSecurity;

/**
 * @author Tiffany
 *
 */
public class MainApp {
	
	//global variables
	private static Database db = new Database();
	protected static DatabaseConnector conn;
	protected static int user_id;
	
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca/cs1434872";
		String username = "CS1434872";
		String password = "nickleck";
		try {
			
			conn = new DatabaseConnector(url, username, password);
					
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error Could not connect");
			return;
		}
		
		login();
		
	}
	
	public static void login() {
		DatabaseSecurity ds = new DatabaseSecurity();
		Scanner sc = new Scanner(System.in);
		String username, password;
		Type type = null;
		String choice;
		
		System.out.println("Welcome to the hospital. Please Sign In: \n");
		System.out.println("Username:\t");
		username = sc.nextLine();
		System.out.println("Password:\t");
		password = sc.nextLine();
		
		if (ds.login(username, password)) {
			type = ds.getType();
		}
		
		switch (type) {
		case Admin:
			System.out.println("Welcome Admin. Please Choose:  \n");
			System.out.println("1 - View Details");
			System.out.println("2 - View notifications");
			System.out.println("3 - Create New User");
			System.out.println("4 - Give Raise");
			System.out.println("5 - Book Appointment for patient(s)");
			System.out.println("6 - Cancel Appointment for patient(s)");
			break;
		case Doctor:
			System.out.println("Welcome Patient. Please Choose:  \n");
			System.out.println("1 - View Upcoming Appointments");
			System.out.println("2 - View Past Appointments");
			System.out.println("3 - View Recent Patients");
			System.out.println("4 - View Details");
			System.out.println("5 - View Patient File");
			System.out.println("6 - Add Notes to Patient File");
			System.out.println("7 - Add Notes to an Appointment");
			System.out.println("8 - Do Procedure on a Patient");
			System.out.println("9 - Prescribe Medication to a Patient");
			System.out.println("9 - Fulfill a Prescrition from inventory");
			System.out.println("10 - Add invoice to Procedure");
			System.out.println("11 - Add invoice to Medication");
			System.out.println("12 - Book an Appointment");
			System.out.println("13 - Cancel an Appointment");
			break;
		case Patient:
			System.out.println("Welcome Patient. Please Choose:  \n");
			System.out.println("1 - View Current Appointments");
			System.out.println("2 - View Past Appointments");
			System.out.println("3 - View Prescriptions");
			System.out.println("4 - View Medication");
			System.out.println("5 - View Procedures");
			System.out.println("6 - View Invoices");
			System.out.println("7 - View Notes");
			System.out.println("8 - View Details");
			System.out.println("9 - Add to Waitlist");
			break;
		default:
			System.out.println("Something's weird");
			break;
		}
		
		choice = sc.nextLine();
	}
	public static void appointment() {
		System.out.println(db.getAppointments(Type.Patient));
	}

	public static void loginCheck(String username, String password) {
		
	}

}
