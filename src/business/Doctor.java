/**
 * 
 */
package business;

/**
 * @author Tiffany
 *
 */
public class Doctor extends User {

	//global variables
	private Float salary;
	
	public Doctor(int id, String firstname, String lastname, String email, String phone, Type type) {
		super(id, firstname, lastname, email, phone, Type.Doctor);
	}
	
	public Float getSalary() {
		return salary;
	}
	
	public void setSalary(Float salary) {
		if (validateSalary(salary))
			this.salary = salary;
	}
	
	public boolean validateSalary(Float salary) {
		if (salary < 0) {
			System.out.println("Salary must be bigger than 0");
			return false;
		}
		return true;
	}

}
