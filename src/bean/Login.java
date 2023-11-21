package bean;

import java.io.Serializable;

public class Login implements Serializable{

	private String userId;
	private String name;
	private String password;
	private String etc;
	private String hire_date;



	public String getUserid() {
		return userId;
	}
	public void setUserid(String userid) {
		this.userId = userid;
	}
	public String getHire_date() {
		return hire_date;
	}
	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getHire_Date() {
		return hire_date;
	}
	public void setHire_Date(String hire_Date) {
		this.hire_date = hire_Date;
	}


}

