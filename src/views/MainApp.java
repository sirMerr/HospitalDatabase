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
	protected static String username;
	protected static String password;
	
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
		Type type = null;
		Scanner sc = new Scanner(System.in);
		
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
			adminChoices();
			break;
		case Doctor:
			doctorChoices();
			break;
		case Patient:
			patientChoices();
			break;
		default:
			System.out.println("Something's weird");
			break;
		}
		
		sc.close();
		
	}
	
	//Type of choices
	public static void adminChoices() {
		Scanner sc = new Scanner(System.in);
		int choice = -1;
		
		System.out.println("Welcome Admin. Please Choose:  \n");
		System.out.println("0 - Back to Login");
		System.out.println("1 - View Details");
		System.out.println("2 - View notifications");
		System.out.println("3 - Create New User");
		System.out.println("4 - Give Raise");
		System.out.println("5 - Book Appointment for patient(s)");
		System.out.println("6 - Cancel Appointment for patient(s)");
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 0:
			login();
			break;
		case 1:
			displayDetails();
			break;
		case 2:
			
			break;
		default:
			System.out.println("Please pick a valid number");
			adminChoices();
			break;
		}
		
		sc.close();
	}
	
	public static void patientChoices() {
		Scanner sc = new Scanner(System.in);
		int choice; 
		
		System.out.println("Welcome Patient. Please Choose:  \n");
		System.out.println("0 - Back to Login");
		System.out.println("1 - View Details");
		System.out.println("2 - View Past Appointments");
		System.out.println("3 - View Prescriptions");
		System.out.println("4 - View Medication");
		System.out.println("5 - View Procedures");
		System.out.println("6 - View Invoices");
		System.out.println("7 - View Notes");
		System.out.println("8 - View Current Appointments");
		System.out.println("9 - Add to Waitlist");
		
		sc.close();
	}
	
	public static void doctorChoices() {
		Scanner sc = new Scanner(System.in);
		int choice;
		
		System.out.println("Welcome Patient. Please Choose:  \n");
		System.out.println("0 - Back to Login");
		System.out.println("1 - View Details");
		System.out.println("2 - View Past Appointments");
		System.out.println("3 - View Recent Patients");
		System.out.println("4 - View Upcoming Appointments");
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
		
		sc.close();
	}
	
	//Displays
	public static void displayDetails() {
		Admin user = (Admin) db.getDetails(user_id);
		Scanner sc = new Scanner(System.in);
		String choice;
		
		System.out.println("=========Info=============");
		System.out.println("UserID: " + user.getId());
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		System.out.println("Full Name: " + user.getFullName());
		System.out.println("Phone: " + user.getPhone());
		System.out.println("Email: " + user.getEmail());
		System.out.println("Salary: $" + user.getSalary());
		System.out.println("===============================");
		System.out.println("Would you like to go back? y/n");
		
		choice = sc.nextLine();
		sc.close();
		if (choice.equals("y") || choice.equals("yes"))
			adminChoices();
		else
			System.out.println("Until next time!");
		
	}

}
