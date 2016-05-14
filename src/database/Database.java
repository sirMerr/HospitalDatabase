/**
 * 
 */
package database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class Database extends MainApp{

	public void makeAppointment(int patient_id, int doctor_id, LocalDate date, String notes) {
		if (date.isBefore(LocalDate.now())) {
			System.out.println("Date is invalid. It is scheduled too early");
			return;
		}
		
		PreparedStatement preparedStatement = conn.prepareStatement("CALL make_appt_procedure(?,?,?,?)");
		
		try {
			preparedStatement.setInt(1,patient_id);
			preparedStatement.setInt(2,doctor_id);
			preparedStatement.setDate(3,Date.valueOf(date));
			preparedStatement.setString(4,notes);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error making appointment");
		}
	}
	
	public void cancelAppointment(int appointment_id) {
		PreparedStatement preparedStatement = conn.prepareStatement("CALL cancel_appt_procedure(?);");
		try {
			preparedStatement.setInt(1,appointment_id);

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error cancelling appointment");
		}
	}
	
	public void raiseAdmin(User user) {
		PreparedStatement statement;
		
		if (user.getType() != Type.Admin) {
			System.out.println("You aren't raising an admin's salary!");
			return;
		}
		
		statement = conn.prepareStatement("CALL raise_procedure(?, ?);");
		try {
			statement.setInt(1, user.getId());
			statement.setFloat(2, ((Admin) user).getSalary());

			conn.executeUpdateStatement(statement);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error raising admin salary");
		}
		
	}
	
	public void raiseDoctor(User user) {
		PreparedStatement statement;
		
		if (user.getType() != Type.Doctor) {
			System.out.println("You aren't raising an doctor's salary!");
			return;
		}
		
		statement = conn.prepareStatement("CALL raise_procedure(?, ?);");
		try {
			statement.setInt(1, user.getId());
			statement.setFloat(2, ((Doctor) user).getSalary());

			conn.executeUpdateStatement(statement);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error raising doctor salary");
		}
		
	}
	public void updatePatient(User user) {

		PreparedStatement statement;

		if (user.getType() != Type.Patient) {
			System.out.println("You aren't updating a patient!");
			return;
		}
		statement = conn.prepareStatement("UPDATE users SET notes=? WHERE user_id=?;");
		try {
			statement.setString(1, ((Patient) user).getNotes());
			statement.setInt(2, user.getId());

			conn.executeUpdateStatement(statement);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error updating patient");
		}
	}
	
	public List<User> getAllUsers(Type type) {

		List<User> userList = new ArrayList<User>();
		
		PreparedStatement preparedStatement = conn.prepareStatement(
				"SELECT user_id, firstname, lastname, email, phone, salary "
						+ "FROM users "
						+ "WHERE type=?;");
		try {
			preparedStatement.setString(1, type.toString());
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error selecting the users");
		}

		ResultSet rs = conn.executeStatement(preparedStatement);

		try {
			while (rs.next()) {
				userList.add(new Doctor(rs.getInt("user_id"), rs.getString("firstname"), rs.getString("lastname"),
						rs.getString("email"), rs.getString("phone"), rs.getFloat("salary")));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting users of type: " + type.toString());
		}
		return userList;
	}
	public List<String> getAppointments(Type type) {
		List<String> apptList = new ArrayList<String>();
		
		PreparedStatement preparedStatement = conn.prepareStatement(
				"SELECT " + type + "_id, firstname, lastname, date "
				+"FROM appointments INNER JOIN users ON user_id=" + type + "_id "
				+"WHERE date > NOW() ORDER BY date DESC;");
		try {
			ResultSet rs = conn.executeStatement(preparedStatement);
			String appt;
			while (rs.next()) {
				appt = rs.getInt(1) + ": " 
						+ rs.getString(2)  + " " 
						+ rs.getString(3) + " on " 
						+ rs.getDate(4);
				apptList.add(appt);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting appointments");
		}
		return apptList;
	}
	
	public List<String> getMedications() {
		List<String> medList = new ArrayList<String>();
		
		PreparedStatement preparedStatement = conn.prepareStatement(
				"SELECT medication_id, name, price FROM medications;");

		try {
			ResultSet rs = conn.executeStatement(preparedStatement);

			while (rs.next()) {
				medList.add(rs.getInt(1) + ": " 
						+ rs.getString(2) + " for " 
						+ rs.getString(3));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting medications list");
		}
		return medList;
	}
	
	public List<String> getProcedures() {
		List<String> procList = new ArrayList<String>();

		try {
			ResultSet rs = conn.executeStatement("SELECT procedure_id, name, price FROM procedures");

			while (rs.next()) {
				procList.add(rs.getInt(1) + ": " 
						+ rs.getString(2) + " for " 
						+ rs.getString(3) + "$");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting procedures list");
		}
		return procList;
	}
	
	public List<String> getAppointments(int user_id, Type type1, Type type2) {
		List<String> apptList = new ArrayList<String>();

		PreparedStatement preparedStatement = conn.prepareStatement(
				"SELECT appointment_id, firstname, lastname, date FROM appointments "
						+ "INNER JOIN users ON user_id=" + type1 + "_id "
						+ "WHERE " + type2 + "_id=? "
						+ "AND date > CURDATE() "
						+ "ORDER BY date DESC;");
		try {
			preparedStatement.setInt(1, user_id);
			ResultSet rs = conn.executeStatement(preparedStatement);

			while (rs.next()) {
				String entry = rs.getInt(1) + ": " 
						+ rs.getString(2) + " " 
						+ rs.getString(3) + " on " 
						+ rs.getDate(4);
				apptList.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting appointments by type");
		}
		return apptList;
	}
	
	public List<String> getPastAppointments(int user_id, Type type1, Type type2) {
		List<String> pastApptList = new ArrayList<String>();
		String content;

		try {
			ResultSet rs = conn.executeStatement("SELECT firstname, lastname, date FROM appointments "
					+ "INNER JOIN users ON user_id=" + type1 + "_id "
					+ "WHERE date < NOW() AND "+ user_id + "=" + type2 + "_id "
					+ "ORDER BY date DESC;");

			while (rs.next()) {
				content = "Appointment with " 
						+ rs.getString(1) + " " 
						+ rs.getString(2) + " on " 
						+ rs.getDate(3);
				pastApptList.add(content);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting past appointments");
		}

		return pastApptList;
	}
	
	public List<String> getNextAppointments(int user_id, Type type1, Type type2) {
		List<String> list = new ArrayList<String>();

		PreparedStatement statement = conn.prepareStatement(
				"SELECT firstname, lastname, date "
						+ "FROM appointments "
						+ "INNER JOIN users ON user_id=" + type1 + "_id "
						+ "WHERE date >= NOW() AND ?=" + type2 + "_id "
						+ "ORDER BY date DESC;");
		try {
			statement.setInt(1, user_id);
			ResultSet rs = conn.executeStatement(statement);

			while (rs.next()) {
				String entry = "There is an appointment with " 
						+ rs.getString(1) + " " + rs.getString(2) + " on " 
						+ rs.getDate(3);
				list.add(entry);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("An error occurred while reading appointments");
		}

		return list;
	}
	
	public User getDetails(int user_id) {
		User user = null;
		PreparedStatement statement = conn.prepareStatement(
				"SELECT firstname, lastname, phone, email, salary, notes, type FROM users "
				+ "WHERE user_id=?;");

		try {
			statement.setInt(1, user_id);
			ResultSet rs = conn.executeStatement(statement);

			rs.next();
			Type type = Type.valueOf(rs.getString(7));

			switch (type) {
			case Admin:
				user = new Admin(user_id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getFloat(5));
				break;
			case Doctor:
				user = new Doctor(user_id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getFloat(5));
				break;
			case Patient:
				user = new Patient(user_id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				((Patient) user).setNotes(rs.getString(6));
				break;
			default:
				System.out.println("An error occurred");
				return null;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting appointments user details");
		}

		return user;
	}
	
	public List<String> getWaitlist(int patient_id) {
		List<String> waitlist = new ArrayList<String>();

		PreparedStatement statement = conn.prepareStatement("CALL waitlist_procedure(?);");

		try {
			statement.setInt(1, patient_id);
			ResultSet rs = conn.executeStatement(statement);

			rs.next();
			waitlist.add(rs.getInt(1) + ": " 
					+ rs.getString(2) + " " 
					+ rs.getString(3));

			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting waitlist");
		}

		return waitlist;
	}
	
	public List<String> getAdminLogs() {
		List<String> logs = new ArrayList<String>();
		ResultSet rs = conn.executeStatement("SELECT * FROM admin_logs;");

		try {
			while (rs.next()) {
				logs.add(rs.getInt(1) + ": " 
						+ rs.getString(3) + " \nCreated on: " 
						+ rs.getDate(2));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting admin logs");
		}

		return logs;
	}
	
	public List<String> getProcedureLogs() {
		List<String> procedure_logs = new ArrayList<String>();
		ResultSet rs = conn.executeStatement("SELECT * FROM procedure_logs;");

		try {
			while (rs.next()) {
				procedure_logs.add(rs.getInt(1) + ": " 
						+ rs.getString(3) + " \nCreated on: " 
						+ rs.getDate(2));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting procedure logs");
		}

		return procedure_logs;
	}
	
	public List<String> getNotifications() {
		ResultSet rs = conn.executeStatement("SELECT * FROM notifications;");

		List<String> list = new ArrayList<String>();
		try {
			while (rs.next()) {
				list.add(rs.getInt(1) + ": " 
						+ rs.getString(3) + " \nCreated on: " 
						+ rs.getDate(2));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting notifications");
		}

		return list;
	}
	
	public List<String> getPrescriptions(int patient_id) {
		List<String> prescriptionsList = new ArrayList<String>();

		try {
			ResultSet rs = conn.executeStatement("CALL get_prescriptions_procedure("+ patient_id + ");");
			while (rs.next()) {
				prescriptionsList.add(rs.getInt(1) + ": " + rs.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting prescriptions");
		}
		return prescriptionsList;
	}
	
	public List<String> getProcedures(int patient_id) {
		List<String> list = new ArrayList<String>();
		try {

			ResultSet rs = conn.executeStatement("CALL get_procedures(" + patient_id + ");");
			while (rs.next()) {
				list.add(rs.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting procedures for a patient");
		}
		return list;
	}
	
	public List<String> getInvoices(int patient_id) {
		List<String> invoiceList = new ArrayList<String>();
		try {
			ResultSet rs = conn.executeStatement("CALL get_invoices_procedure(" + patient_id + ");");
			while (rs.next()) {
				invoiceList.add(rs.getInt(1) + ": $" + rs.getFloat(2));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting invoices");
		}
		return invoiceList;
	}
	
	public void performProcedure(int patient_id, int procedure_id, int doctor_id) {
		PreparedStatement preparedStatement = conn.prepareStatement("CALL perform_procedure(?, ?, ?);");

		try {
			preparedStatement.setInt(1, patient_id);
			preparedStatement.setInt(2, procedure_id);
			preparedStatement.setInt(3, doctor_id);

			conn.executeUpdateStatement(preparedStatement);
			System.out.println("Procedure was a success. The patient died, but it was a success.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error performing procedure. Patient is dead.");
		}
	}
	
	public void prescribeMedication(int patient, int medication, int doctor) {
		PreparedStatement statement = conn.prepareStatement("CALL prescribe_medication(?, ?, ?);");

		try {
			statement.setInt(1, patient);
			statement.setInt(2, medication);
			statement.setInt(3, doctor);

			conn.executeStatement(statement);
			System.out.println("Prescribed medication successfully.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error prescribing medication. Patient is sick.");
		}
	}
	
}
