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
    public int ConfirmLogin(String username, String password) throws Exception {
        this.ConnectDB();
        
        // Display values
        System.out.print(", Username: " + username);
        System.out.println(", Password: " + password);

        String query = "SELECT * "
                + "FROM public.\"Users\" "
                + "WHERE \"Username\"='" + username + "' AND \"Password\"='" + password + "'";
        rs = this.SearchDB(query);
        // STEP 5: Extract data from result set

        if (rs.next() == true) {
            System.out.println("Login confirmed in the database.");
            if (rs.getBoolean(3)) {
                rs.close();
                this.CloseDB();
                return 1;
            } else {
                rs.close();
                this.CloseDB();
                return 2;
            }

        } else {
            System.out.println("Login refused by the database.");
            rs.close();
            this.CloseDB();
            return 0;
        }
        
    }

    public void MakeRegister(String username, String password, String local, boolean patient) throws Exception {
        this.ConnectDB();
        
        // Display values
        System.out.println("Username: " + username + ", Password: " + password);
        System.out.println("Localization: " + local);
        String query;
        if (patient == true) {
            query = "INSERT INTO public.\"Users\"( \"Username\", \"Password\", \"Patient\") "
                    + "VALUES ('" + username + "', '" + password + "', true);";
        } else {
            query = "INSERT INTO public.\"Users\"( \"Username\", \"Password\", \"Localization\", \"Patient\") "
                    + "VALUES ('" + username + "', '" + password + "','" + local + "', false);";
        }

        this.InsertDB(query);

        System.out.println("Insertion with sucess");
        this.CloseDB();
    }

    public void MakeRequest(String username, String local) throws Exception {
        this.ConnectDB();
        
        // Display values
        System.out.println("Username: " + username + ", Localization: " + local);

        String query = "INSERT INTO Registration "
                + "VALUES (100, 'Zara', 'Ali', 18)";
        this.InsertDB(query);

        System.out.println("Insertion with sucess");
        this.CloseDB();
    }

    public void SetVolunteerField(String username) throws Exception {
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
