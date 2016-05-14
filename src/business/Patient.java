/**
 * 
 */
package business;

/**
 * @author Tiffany
 *
 */
public class Patient extends User {
	
	private String notes;

	public Patient(int id, String firstname, String lastname, String email, String phone) {
		super(id, firstname, lastname, email, phone, Type.Patient);
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
