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

	Patient("PATIENT"), Doctor("DOCTOR"), Admin("ADMIN");
	
	private String type;
	
	private Type(String type) { 
		this.type = type;
	}
	
	@Override 
	public String toString() {
		return type;
	}
}
