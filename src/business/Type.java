/**
 * 
 */
package business;

/**
 * @author Tiffany Le-Nguyen
 * 
 * Enum for patient, doctor and admin
 */
public enum Type {

	Patient("Patient"), Doctor("Doctor"), Admin("Admin");
	
	private String type;
	
	private Type(String type) { 
		this.type = type;
	}
	
	@Override 
	public String toString() {
		return type;
	}
}
