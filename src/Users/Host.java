package Users;

public class Host extends Users {
	public Host(String name, String email, String password, String address, String occupation, String sin, String dob) {
		super(email, name, dob, address, occupation, sin, password);
	}
}
