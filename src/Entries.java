
public class Entries {
	String firstname;
	String surname;
	String number;
	String address;
	
	//setters
	public void setFirstname(String firstname){
		this.firstname = firstname;
	}
	
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	//Getters
	public String getFirstname(){
		return firstname;
	}
		
	public String getSurname(){
		return surname;
	}
	
	public String getNumber(){
		return number;
	}
	
	public String getAdress(){
		return address;
	}
	//constructor
	public Entries(String firstname, String surname, String number, String address){
		this.firstname = firstname;
		this.surname = surname;
		this.number = number;
		this.address = address;
	}
	//constructor without address
		public Entries(String firstname, String surname, String number){
			this.firstname = firstname;
			this.surname = surname;
			this.number = number;
			this.address = "";
		}

}
