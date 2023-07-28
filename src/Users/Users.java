package Users;

public abstract class Users {

	String email, firstName, lastName,pwd, dob, occup, sin, addr;
	int cancellations;

	public Users(String email, String firstName, String lastName, String dob, String addr, String occup, String sin, String pwd, String cancellations) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.addr = addr;
		this.occup = occup;
		this.sin = sin;
		this.pwd = pwd;
		this.cancellations = Integer.parseInt(cancellations);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return addr;
	}

	public void setAddress(String address) {
		this.addr = address;
	}
	public String getOccupation() {
		return occup;
	}

	public void setOccupation(String occupation) {
		this.occup = occupation;
	}

	public String getSin() {
		return sin;
	}

	public void setSin(String sin) {
		this.sin = sin;
	}

	public String getPassword() {
		return pwd;
	}

	public void setPassword(String password) {
		this.pwd = password;
	}
	
	public int getCancellations() {
		return cancellations;
	}

	public void setCancellations(int cancellations) {
		this.cancellations = cancellations;
	}

}
