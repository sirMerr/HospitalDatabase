/**
 * 
 */
package business;

/**
 * @author Tiffany
 *
 */
public class Admin extends User {
	
	private Float salary;

	public Admin(int id, String firstname, String lastname, String email, int phone, Type type) {
		super(id, firstname, lastname, email, phone, Type.Admin);
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
