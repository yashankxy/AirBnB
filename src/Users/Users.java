package Users;

public abstract class Users {

	String email, name,pwd, dob, occup, sin, addr;
	int cancellations;

	public Users(String email, String name, String dob, String addr, String occup, String sin, String pwd) {
		this.email = email;
		this.name = name;
		this.dob = dob;
		this.addr = addr;
		this.occup = occup;
		this.sin = sin;
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	

}
