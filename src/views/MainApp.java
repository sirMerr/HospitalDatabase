/**
 * 
 */
package views;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import business.Admin;
import business.Doctor;
import business.Patient;
import business.Type;
import business.User;
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
	private static String username;
	private static String password;
	private static Scanner sc = new Scanner(System.in);
	
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
		sc.close();
		
	}
	
	public static void login() {
		DatabaseSecurity ds = new DatabaseSecurity();
		Type type = null;
		
		System.out.println("Welcome to the hospital. Please Sign In: \n");
		System.out.println("Username:\t");
		username = sc.next();
		System.out.println("Password:\t");
		password = sc.next();
		
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
		
		
	}
	
	//Type of choices
	public static void adminChoices() {
		int choice = -1;
		
		System.out.println("Welcome Admin. Please Choose:  \n");
		System.out.println("0 - Back to Login");
		System.out.println("1 - View Details");
		System.out.println("2 - View notifications");
		System.out.println("3 - Create New User");
		System.out.println("4 - Give Raise To Doctor");
		System.out.println("5 - Book Appointment for patient(s)");
		System.out.println("6 - Cancel Appointment for patient(s)");
		System.out.println("7 - Give Raise to Admin");
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 0:
			login();
			break;
		case 1:
			displayDetails();
			break;
		case 2:
			displayNotifications();
			break;
		case 3:
			displayCreate();
			break;
		case 4:
			displayRaiseDoctor();
			break;
		case 5:
			displayBook();
			break;
		case 6:
			displayCancel();
			break;
		case 7:
			displayRaiseAdmin();
			break;
		default:
			System.out.println("Please pick a valid number");
			adminChoices();
			break;
		}
		
	}
	

	public static void patientChoices() {
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
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 0:
			login();
			break;
		case 1:
			displayDetails();
			break;
		case 2:
			displayPastAppointments();
			break;
		case 3:
			displayPrescriptions();
			break;
		case 4:
			displayProcedures();
			break;
		case 5:
			displayInvoices();
			break;
		case 6:
			displayNotes();
			break;
		case 7:
			displayAppointments();
			break;
		case 8:
			displayNotes();
			break;
		case 9:
			displayWaitlist();
			break;
		default:
			System.out.println("Please pick a valid number");
			patientChoices();
			break;
		}
		
	}
	

	public static void doctorChoices() {
		int choice;
		
		System.out.println("Welcome Patient. Please Choose:  \n");
		System.out.println("0 - Back to Login");
		System.out.println("1 - View Details");
		System.out.println("2 - View Past Appointments");
		System.out.println("3 - View Patients");
		System.out.println("4 - View Upcoming Appointments");
		System.out.println("5 - View Patient File");
		System.out.println("6 - Add Notes to Patient File");
		System.out.println("7 - Add Notes to an Appointment");
		System.out.println("8 - Do Procedure on a Patient");
		System.out.println("9 - Prescribe Medication to a Patient");
		System.out.println("10 - Fulfill a Prescrition from inventory");
		System.out.println("11 - Add invoice to Procedure");
		System.out.println("12 - Add invoice to Medication");
		System.out.println("13 - Book an Appointment");
		System.out.println("14 - Cancel an Appointment");
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 0:
			login();
			break;
		case 1:
			displayDetails();
			break;
		case 2:
			displayPastAppointmentsB();
			break;
		case 3:
			displayPatients();
			break;
		case 4:
			displayAppointmentsB();
			break;
		case 5:
			displayPatientDetails();
			break;
		case 6:
			displayUpdatePatient();
			break;
		case 7:
			displayDoProcedure();
			break;
		case 8:
			displayPrescribeMedication();
			break;
		case 9:
			displayBook();
			break;
		case 10:
			displayCancel();
			break;
		default:
			System.out.println("Please pick a valid number");
			patientChoices();
			break;
		}
		
		System.out.println("===============================");
		System.out.println("Would you like to go back? y/n");
		
		String choice2 = sc.next();
		sc.close();
		if (choice2.equals("y") || choice2.equals("yes") || choice2.equals("Y"))
			adminChoices();
		else
			System.out.println("Until next time!");
		
	}
	

	//Displays
	public static void displayDetails() {
		Admin user = (Admin) db.getDetails(user_id);
		
		System.out.println("=========Info=============");
		System.out.println("UserID: " + user.getId());
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		System.out.println("Full Name: " + user.getFullName());
		System.out.println("Phone: " + user.getPhone());
		System.out.println("Email: " + user.getEmail());
		System.out.println("Salary: $" + user.getSalary());

		
	}
	
	private static void displayCancel() {
		System.out.println("=========Cancel Appointment=============");
		System.out.println("AppointmentID: ");
		int appointment_id = sc.nextInt();
		
		db.cancelAppointment(appointment_id);
		
	}

	private static void displayBook() {
		System.out.println("=========Book Appointment:=========");
		System.out.println("PatientID: ");
		int patient_id = sc.nextInt();
		System.out.println("DoctorID: ");
		int doctor_id = sc.nextInt();
		System.out.println("Date: ");
		LocalDate date = Date.valueOf(sc.next()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println("Notes: ");
		String notes = sc.next();
		
		db.makeAppointment(patient_id, doctor_id, date, notes);
		
	}

	private static void displayRaiseDoctor() {
		System.out.println("=========Raise Doctor:=========");
		System.out.println("DoctorID: ");
		int id = sc.nextInt();
		System.out.println("Raise Amount: ");
		Float raise = sc.nextFloat();
		
		Doctor doctor = new Doctor(id, null, null, null, null, raise);
		
		db.raiseDoctor(doctor);
	}
	
	private static void displayRaiseAdmin() {
		System.out.println("=========Raise Admin:=========");
		System.out.println("DoctorID: ");
		int id = sc.nextInt();
		System.out.println("Raise Amount: ");
		Float raise = sc.nextFloat();
		
		Admin admin = new Admin(id, null, null, null, null, raise);
		
		db.raiseDoctor(admin);
	}

	private static void displayCreate() {
		int choice;
		System.out.println("=========Create:=========");
		System.out.println("Do you want to create:");
		System.out.println("1 - Admin");
		System.out.println("2 - Doctor");
		System.out.println("3 - Patient");
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 1:
			createAdmin();
			break;
		case 2:
			createDoctor();
			break;
		case 3:
			createPatient();
			break;
		default:
			System.out.println("Invalid. Please Try Again");
			displayCreate();
			break;
		}
		System.out.println("++++++++++++++++++++++++");
	}

	private static void createPatient() {
		String firstname;
		String lastname;
		String username;
		String password;
		String phone;
		String email;
		
		System.out.println("++++++++Create Admin:++++++++");
		System.out.println("UserName: ");
		username = sc.next();
		System.out.println("Password: ");
		password = sc.next();
		System.out.println("First Name: ");
		firstname = sc.next();
		System.out.println("Last Name: ");
		lastname = sc.next();
		System.out.println("Phone Name: ");
		phone = sc.next();
		System.out.println("Email Name: ");
		email = sc.next();

		
		User user = new Patient(0,firstname, lastname,email,phone);
		createUser(user, username, password);
		
	}

	private static void createDoctor() {
		String firstname;
		String lastname;
		String username;
		String password;
		String phone;
		String email;
		Float salary = 0f;
		
		System.out.println("++++++++Create Doctor:++++++++");
		System.out.println("UserName: ");
		username = sc.next();
		System.out.println("Password: ");
		password = sc.next();
		System.out.println("First Name: ");
		firstname = sc.next();
		System.out.println("Last Name: ");
		lastname = sc.next();
		System.out.println("Phone Name: ");
		phone = sc.next();
		System.out.println("Email Name: ");
		email = sc.next();
		System.out.println("Salary ");
		salary = sc.nextFloat();
		
		User user = new Doctor(0,firstname, lastname,email,phone, salary);
		createUser(user, username, password);
		
	}

	private static void createAdmin() {
		String firstname;
		String lastname;
		String username;
		String password;
		String phone;
		String email;
		Float salary = 0f;
		
		System.out.println("++++++++Create Admin:++++++++");
		System.out.println("UserName: ");
		username = sc.next();
		System.out.println("Password: ");
		password = sc.next();
		System.out.println("First Name: ");
		firstname = sc.next();
		System.out.println("Last Name: ");
		lastname = sc.next();
		System.out.println("Phone Name: ");
		phone = sc.next();
		System.out.println("Email Name: ");
		email = sc.next();
		System.out.println("Salary ");
		salary = sc.nextFloat();
		
		User user = new Admin(0,firstname, lastname,email,phone, salary);
		createUser(user, username, password);
		
	}
	
	private static void createUser(User user, String username, String password) {
		DatabaseSecurity ds = new DatabaseSecurity();
		ds.newUser(user, username, password);
		
	}

	private static void displayNotifications() {
		System.out.println("=========Notifications:=========");
		List<String> notifications = db.getNotifications();
		
		for (int i = 0; i < notifications.size(); i++) {
			System.out.println(notifications.get(i));
		}
		
	}

	private static void displayAppointments() {
		System.out.println("=========Appointments for Patients:=========");
		List<String>list = db.getNextAppointments(user_id,Type.Doctor, Type.Patient);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	
	private static void displayWaitlist() {
		System.out.println("=========Add to Waitlist:=========");
		
		List<String>list = db.getWaitlist(user_id);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println("=Book Appointment:=");
		System.out.println("DoctorId: ");
		int id = sc.nextInt();
		System.out.println("Date: ");
		LocalDate date = Date.valueOf(sc.next()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		db.makeAppointment(user_id, id, date, null);
		
		
		
		
	}
	private static void displayNotes() {
		System.out.println("=========Notes:=========");
		
		Patient patient = (Patient) db.getDetails(user_id);
		
		System.out.println(patient.getNotes());
		
	}

	private static void displayInvoices() {
		System.out.println("=========Invoices:=========");
		
		List<String>list = db.getInvoices(user_id);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}

	private static void displayProcedures() {
		System.out.println("=========Procedures:=========");
		
		List<String>list = db.getProcedures(user_id);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}

	private static void displayPrescriptions() {
		System.out.println("=========Prescriptions:=========");
		
		List<String>list = db.getPrescriptions(user_id);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}

	private static void displayPastAppointments() {
		System.out.println("=========Past Appointments For Patients:=========");
		
		List<String>list = db.getPastAppointments(user_id, Type.Doctor, Type.Patient);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		
		
	}
	
	private static void displayPrescribeMedication() {
		System.out.println("=========Prescribe Medication:=========");
		System.out.println("PatientID :");
		int patient = sc.nextInt();
		System.out.println("MedicationID :");
		int medication = sc.nextInt();

				
		db.prescribeMedication(patient, medication, user_id);
	}


	private static void displayDoProcedure() {
		System.out.println("=========Perform Procedure:=========");
		System.out.println("PatientID :");
		int patient = sc.nextInt();
		System.out.println("ProcedureID :");
		int procedure = sc.nextInt();
		db.performProcedure(patient, procedure, user_id);
		
	}


	private static void displayUpdatePatient() {
		System.out.println("=========Update Patient:=========");
		System.out.println("PatientID: ");
		int id = sc.nextInt();
		System.out.println("UserName: ");
		username = sc.next();
		System.out.println("Password: ");
		password = sc.next();
		System.out.println("First Name: ");
		String firstname = sc.next();
		System.out.println("Last Name: ");
		String lastname = sc.next();
		System.out.println("Phone Name: ");
		String phone = sc.next();
		System.out.println("Email Name: ");
		String email = sc.next();
		System.out.println("Notes: ");
		String notes = sc.next();
		
		Patient patient = new Patient(id, firstname,
				lastname, email, phone);
		patient.setNotes(notes);
		db.updatePatient(patient);
		
	}

	private static void displayPatientDetails() {
		System.out.println("=========Patient Details:=========");
		System.out.println("PatientID: ");
		int id = sc.nextInt();
		
		Patient patient = (Patient)db.getDetails(id);
		
		System.out.println(patient.getFirstname());
		System.out.println(patient.getLastname());
		System.out.println(patient.getPhone());
		System.out.println(patient.getEmail());
		System.out.println(patient.getNotes());
		
	}

	private static void displayPatients() {
		System.out.println("=========Past Patients:=========");
		List<User>list = db.getAllUsers(Type.Patient);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getFullName());
		}
	}
	
	private static void displayAppointmentsB() {
		System.out.println("=========Appointments for Doctors:=========");
		List<String>list = db.getNextAppointments(user_id,Type.Patient, Type.Doctor);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}

	private static void displayPastAppointmentsB() {
		System.out.println("=========Past Appointments For Patients:=========");
		
		List<String>list = db.getPastAppointments(user_id, Type.Patient, Type.Doctor);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}
	
}
