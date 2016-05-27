package PP.pp_graph;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class mysql_connection {
	private String driver = "com.mysql.jdbc.Driver";
	 
	private String url = "jdbc:mysql://localhost:3306";
 
	private String user = "root";

	private String password = "ppzuche";
	
	private Connection conn;

	public mysql_connection(String Driver,String URL, String User, String Passwd){
		driver = Driver;
		url = URL;
		user = User;
		password = Passwd;
		connect2mysql();
	}
	
	private void connect2mysql(){
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName(driver).newInstance();
        } catch (Exception ex) {
            System.out.println("wrong at mysql driver");
        }
        try {
            conn =
               DriverManager.getConnection(url,user,password );
            
            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
	}
	
	public void close_mysql_connect() throws SQLException{
		try{
		conn.close();
		}
		catch(SQLException sqlEx){
		
		}
		
	}
	
	public ResultSet query(String sql){
		Statement stmt = null;
		ResultSet result = null;
		try {
		    stmt = conn.createStatement();
		    result = stmt.executeQuery(sql);
		    return result;
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    return null;
		}
	}
	
	
	
	
	
}
