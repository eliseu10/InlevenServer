package inleven;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/EMP";

	// Database credentials
	static final String USER = "username";
	static final String PASS = "password";
	
	// Database variables
	private Connection conn = null;
	private ResultSet rs = null;
	
	/*
	 * ************************************************************************
	 *                            BASIC FUNTIONS
	 * ************************************************************************
	 */
	
	private void ConnectDB() throws Exception{
		
		// STEP 2: Register JDBC driver
		Class.forName(JDBC_DRIVER);

		// STEP 3: Open a connection
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
	private ResultSet SearchDB(String sql) throws Exception{
		Statement stmt = null;

		// STEP 4: Execute a query
		System.out.println("Creating statement...");
		stmt = (Statement) conn.createStatement();

		rs = ((java.sql.Statement) stmt).executeQuery(sql);
		
		((Connection) stmt).close();
	    
	    return rs;
	}
	
	private void InsertDB(String sql) throws Exception {
		Statement stmt = null;

		// STEP 4: Execute a query
		System.out.println("Inserting statement...");
		stmt = (Statement) conn.createStatement();
		

		((java.sql.Statement) stmt).executeUpdate(sql);
	}
	
	private void UpdateDB(String sql) throws Exception {
		Statement stmt = null;

		// STEP 4: Execute a query
		System.out.println("Updating statement...");
		stmt = (Statement) conn.createStatement();
		

		((java.sql.Statement) stmt).executeUpdate(sql);
	}
	
	private void CloseDB() throws SQLException{
		// STEP 6: Clean-up environment
		rs.close();
		conn.close();
	      
	}
	
	/*
	 * ************************************************************************
	 *                            OTHER FUNTIONS
	 * ************************************************************************
	 */
	
	public boolean ConfirmLogin(String username, String password) throws Exception{
		this.ConnectDB();
		
		// Display values
		System.out.print(", Username: " + username);
		System.out.println(", Password: " + password);
		
		String query = "SELECT id, first, last, age FROM Employees";
		rs = this.SearchDB(query);
		// STEP 5: Extract data from result set

		if (rs.first() == true) {
			this.CloseDB();
			System.out.println("Login confirmed in the database.");
			return true;
		}else {
			this.CloseDB();
			System.out.println("Login refused by the database.");
			return false;
		}
	}
	
	public void MakeRegister(String username, String password, String local, boolean patient) throws Exception{
		this.ConnectDB();
		
		// Display values
		System.out.println("Username: " + username + ", Password: " + password);
		System.out.println("Localization: " + local);
		
		String query = "INSERT INTO Registration "
				+ "VALUES (100, 'Zara', 'Ali', 18)";
		this.InsertDB(query);
		
		System.out.println("Insertion with sucess");
		
		this.CloseDB();
	}
	
	public void MakeRequest(String username,  String local) throws Exception {
		this.ConnectDB();
		
		// Display values
		System.out.println("Username: " + username + ", Localization: " + local);
		
		String query = "INSERT INTO Registration "
				+ "VALUES (100, 'Zara', 'Ali', 18)";
		this.InsertDB(query);
		
		System.out.println("Insertion with sucess");
		
		this.CloseDB();
	}
	
	public void SetVolunteerField (String username) throws Exception {
		this.ConnectDB();
		
		// Display values
		System.out.println("Username: " + username);
		
		String query = "INSERT INTO Registration "
				+ "VALUES (100, 'Zara', 'Ali', 18)";
		this.UpdateDB(query);
		
		System.out.println("Insertion with sucess");
		
		this.CloseDB();
	}
	
	
}
