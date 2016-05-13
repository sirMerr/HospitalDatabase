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

import business.Type;
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
				appt = rs.getInt(1) + ": " + rs.getString(2)  + " " + rs.getString(3) + " on " + rs.getDate(4);
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
	
	public List<String> getAllMedications() {
		List<String> list = new ArrayList<String>();
		PreparedStatement statement = conn.prepareStatement(
				"SELECT medication_id, name FROM medications;");

		try {
			ResultSet rs = conn.executeStatement(statement);

			while (rs.next()) {
				list.add(rs.getInt(1) + ": " + rs.getString(2));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("Error getting medications list");
		}

		return list;
	}
}
