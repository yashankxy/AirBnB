package Users;

public class Renter extends Users{
    private String credit_card;
	public Renter (String email, String firstName, String lastName, String dob, String addr, String occup, String sin, String pswd, String credit_card, String cancellations) {
		super(email, firstName, lastName, dob, addr, occup, sin, pswd, cancellations);
		this.credit_card = credit_card;
	}
	public void setCredit_Card(String credit_card) {
		this.credit_card = credit_card;
	}
	public String getCredit_Card() {
		return credit_card;
	}
}
