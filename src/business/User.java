/**
 * 
 */
package business;

/**
 * @author Tiffany
 *
 */
public class User {

	//global variables
	int id;
	String firstname, lastname, email, phone;
	Type type;
	
	public User(int id, String firstname, String lastname, String email, String phone, Type type) {
		this.id = id;
		this.type = type;
		
		if (validateString(firstname))
			this.firstname = firstname;
		
		if (validateString(lastname))
			this.lastname = lastname;
		
		if (validateString(email))
			this.email = email;
		
		this.phone = phone;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	
	public String getFullName() {
		return firstname + " " + lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setFirstname(String firstname) {
		if (validateString(firstname)) {
			this.firstname = firstname;
		}
	}
	
	public void setLastname(String lastname) {
		if (validateString(lastname)) {
			this.lastname = lastname;
		}
	}
	
	public void setEmail(String email) {
		if (validateString(email)) {
			this.email = email;
		}
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	private boolean validateString(String name) {
		if (name == null || name.trim().equals("")) {
			System.out.println("Invalid input. Please try again");
			return false;
		}
		return true;
	}
}
