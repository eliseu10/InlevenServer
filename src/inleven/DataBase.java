package inleven;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
    // JDBC driver name and database URL

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://packy.db.elephantsql.com:5432/bnqqywco";

    // Database credentials
    static final String USER = "bnqqywco";
    static final String PASS = "AICGIf3uH0tN5a5CanouP_Tts9OV7aCS";

    // Database variables
    private Connection conn = null;
    private ResultSet rs = null;
    private Statement stmt = null;
/*
    public DataBase() throws Exception {
        ConnectDB();
    }*/

    /*
	 * ************************************************************************
	 *                            BASIC FUNTIONS
	 * ************************************************************************
     */
    private void ConnectDB() throws Exception {

        // STEP 2: Register JDBC driver
        Class.forName(JDBC_DRIVER);

        // STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        
        stmt = conn.createStatement();
    }

    private ResultSet SearchDB(String sql) throws Exception {
        // STEP 4: Execute a query
        System.out.println("Creating statement...");
        rs = stmt.executeQuery(sql);
        return rs;
    }

    private void InsertDB(String sql) throws Exception {
        // STEP 4: Execute a query
        System.out.println("Inserting statement...");
        stmt.executeUpdate(sql);
    }

    private void UpdateDB(String sql) throws Exception {
        // STEP 4: Execute a query
        System.out.println("Updating statement...");
        stmt.executeUpdate(sql);
    }

    public void CloseDB() throws SQLException {
        // STEP 6: Clean-up environment
        stmt.close();
        conn.close();

    }

    /*
	 * ************************************************************************
	 *                            OTHER FUNTIONS
	 * ************************************************************************
     */
    public ActualState ConfirmLogin(ActualState hr) throws Exception {
        this.ConnectDB();

        // Display values
        System.out.println("Checking login...");
        System.out.print("Username: " + hr.username);
        System.out.println(", Password: " + hr.password);

        String query = "SELECT * "
                + "FROM public.\"Users\" "
                + "WHERE \"Username\"='" + hr.username + "' AND \"Password\"='" + hr.password + "'";
        rs = this.SearchDB(query);
        // STEP 5: Extract data from result set

        if (rs.next() == true) {
            System.out.println("Login confirmed in the database.");
            hr.sucessLogin = true;
            hr.phone = rs.getString(3);
            if (rs.getBoolean(5)) {
                System.out.println("Login patient accepted.");
                hr.patient = true;
            } else {
                System.out.println("Login volunteer accepted.");
                hr.local = rs.getString(4);
                hr.patient = false;
                System.out.println("Local: " + hr.local);
            }

        } else {
            System.out.println("Login refused by the database.");
            hr.sucessLogin = false;
        }
        
        rs.close();
        this.CloseDB();
        return hr;
    }

    public void MakeRegister(String username, String password, String phone, String local, boolean patient) throws Exception {
        this.ConnectDB();

        // Display values
        System.out.println("Make a register...");
        System.out.println("Username: " + username + ", Password: " + password);
        System.out.println("Localization: " + local + ", Phone:" + phone);

        String query = "SELECT Count(*)"
                + "FROM public.\"Users\" "
                + "WHERE \"Username\"='" + username + "'";
        rs = this.SearchDB(query);
        rs.next();
        
        
        if ((patient == true) && (rs.getInt(1) == 0)) {
            query = "INSERT INTO public.\"Users\"( \"Username\", \"Password\",\"Phone\", \"Patient\") "
                    + "VALUES ('" + username + "', '" + password + "','" + phone + "', true);";
            this.InsertDB(query);
            System.out.println("Insertion with sucess.");
            
        } else if ((patient == false) && (rs.getInt(1) == 0)) {
            query = "INSERT INTO public.\"Users\"( \"Username\", \"Password\",\"Phone\", \"Localization\", \"Patient\") "
                    + "VALUES ('" + username + "', '" + password + "','" + phone + "','" + local + "', false);";
            this.InsertDB(query);
            System.out.println("Insertion with sucess.");
            
        } else{
            System.out.println("Username already exists.");
        }

        rs.close();
        this.CloseDB();
    }

    public void MakeRequest(String username, String local, String helpType) throws Exception {
        this.ConnectDB();
        
        // Display values
        System.out.println("Make help request...");
        System.out.println("Username: " + username + ", Localization: " + local + ", Help Type: " + helpType);
        
        String query = "INSERT INTO public.\"HelpRequest\"(\"HelpType\", \"Localization\", \"Patient\" ) "
                + "VALUES ('" + helpType + "', '" + local + "', '" + username + "');";
        this.InsertDB(query);

        System.out.println("Insertion with sucess.");
        
        this.CloseDB();
    }

    public void SetVolunteerField(String username, String local) throws Exception {
        this.ConnectDB();
        String id = null;

        // Display values
        System.out.println("Set volunteer field...");
        System.out.println("Username: " + username + ", Local: " + local);

        //search for help request in the same localization
        String query = "SELECT \"ID\" "
                + "FROM public.\"HelpRequest\" "
                + "WHERE \"Localization\" = '" + local + "' and \"Volunteer\" IS NULL;";
        
        rs = this.SearchDB(query);

        while (rs.next()) {
            id = rs.getString(1);
            System.out.println("RequestID: " + id);
            
            //Update the value of volunteer in help request
            query = "UPDATE public.\"HelpRequest\" "
                    + "SET \"Volunteer\"='" + username + "' "
                    + "WHERE \"ID\"=" + id + ";";
            
            this.UpdateDB(query);
            System.out.println("Update with sucess.");
            
            //only for the first help request
            break;
        }
        
        rs.close();
        this.CloseDB();
    }

    public ActualState VerifyHelpRequest(ActualState hr) throws Exception {
        System.out.println("Search for help requests...");
        this.ConnectDB();
        
        // Display values
        System.out.println("Search for help requests...");
        System.out.println("Username: " + hr.username + ", Patient: " + hr.patient);
        
        String query = null;
        int rows = 0;
        if (hr.patient == true) {
            //is a patient
            //search for help request in the same localization
            query = "SELECT Count(*)"
                    + "FROM public.\"HelpRequest\""
                    + "WHERE \"Patient\"='" + hr.username + "' AND \"Volunteer\" IS NOT NULL;";
            rs = this.SearchDB(query);
            rs.next();
            rows = rs.getInt(1);
            System.out.println("Number of rows: " + rows);

            //search for help request in the same localization
            query = "SELECT \"HelpType\", \"Localization\", \"Volunteer\""
                    + "FROM public.\"HelpRequest\""
                    + "WHERE \"Patient\"='" + hr.username + "' AND \"Volunteer\" IS NOT NULL;";

        } 
        if (hr.patient == false){
            //is a volunteer
            //search for help request in the same localization
            query = "SELECT Count(*)"
                    + "FROM public.\"HelpRequest\""
                    + "WHERE \"Volunteer\"='" + hr.username + "' AND \"Patient\" IS NOT NULL;";
            rs = this.SearchDB(query);
            rs.next();
            rows = rs.getInt(1);
            System.out.println("Number of rows: " + rows);
            
            //search for help request in the same localization
            query = "SELECT \"HelpType\", \"Localization\", \"Patient\""
                    + "FROM public.\"HelpRequest\""
                    + "WHERE \"Volunteer\"='" + hr.username + "' AND \"Patient\" IS NOT NULL;";
        }
        rs = this.SearchDB(query);
        
        hr.requestState = new String[rows][3];
        int i=0;
        while (rs.next()){
            hr.requestState[i][0] = rs.getString(1);
            hr.requestState[i][1] = rs.getString(2);
            hr.requestState[i][2] = rs.getString(3);
            System.out.println("Help type: " + hr.requestState[i][0] + ", Local: " + hr.requestState[i][1] + ", "
                    + "Username: " + hr.requestState[i][2] + "");
            i++;
        }
        
        rs.close();
        this.CloseDB();
        return hr;
    }

}
