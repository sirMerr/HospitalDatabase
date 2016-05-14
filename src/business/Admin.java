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

	public Admin(int id, String firstname, String lastname, String email, String phone, Float salary) {
		super(id, firstname, lastname, email, phone, Type.Admin);
		setSalary(salary);
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
