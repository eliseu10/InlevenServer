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
	boolean patient;
        String phone;
	
	//request fields
	String local = null;
        String helpType = null;
        String [][] requestState = null;
        
        //classify request
        String IDreq = null;
        String Classification = null;
        String [][] ranking = null;
	
	//volunteer added to request 
	String volunteer = null;
        String [][] VolunteerContacts = null;
}
