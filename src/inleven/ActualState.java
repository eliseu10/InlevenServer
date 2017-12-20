package inleven;

import java.io.Serializable;

public class ActualState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1073947871805303166L;
	String typeRequest = null;
	
	//login fields
	String username = null;
	String password = null;
	boolean sucessLogin = false;
	
	//register fiedls
	String email = null;
	boolean patient;
	
	//request fields
	String local = null;
	
	//volunteer added to request 
	String volunteer = null;
	
	void login() {
		
	}
	
	void register() {
		
	}
	
	void sendRequest() {
		
	}
	
	
	public void addVolunteer(String v){
		volunteer = v;
	}
}
