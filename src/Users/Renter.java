package Users;

public class Renter extends Users{
    private String cc_num, cc_name, cc_exp, cc_cvv;
	public Renter (String name, String email, String pwd, String addr, String occup, String sin, String dob, String cc_num, String cc_name, String cc_exp, String cc_cvv) {
		super(email, name, dob, addr, occup, sin, pwd);
		
	}
	//getters and setters
	public String getCc_num() {
		return cc_num;
	}
	public void setCc_num(String cc_num) {
		this.cc_num = cc_num;
	}
	public String getCc_name() {
		return cc_name;
	}
	public void setCc_name(String cc_name) {
		this.cc_name = cc_name;
	}
	public String getCc_exp() {
		return cc_exp;
	}
	public void setCc_exp(String cc_exp) {
		this.cc_exp = cc_exp;
	}
	public String getCc_cvv() {
		return cc_cvv;
	}
	public void setCc_cvv(String cc_cvv) {
		this.cc_cvv = cc_cvv;
	}


}
