import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;

import javax.swing.JFrame;


/**
 * This class runs our GUI. It first connects to the database using a driver called jdbc then creates a log in screen.
 * @author Chang Bum Kim
 * @author Elaine Chao
 */
public class Driver{
	/**
	 * this is the connection to the database
	 */
	Connection conn = null;
	
	/**
	 * This is the log in screen that gets created on start up.
	 */
	private Titlescreen a = null;
	
	/**
	 * Constructs a Driver, which makes a connection to the database.
	 */
	public Driver() {
		try {
			//the driver that needs to be installed.
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			System.out.println("error");
		}
		try {
			//creates the connection here.
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Whiteboard", "root", "root");
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	public static void main(String[] args) {
		Driver drive = new Driver();
		drive.gotoLog();
	}
	/**
	* This method is a helper to create the titlescreen
	*/
	public void gotoLog() {
		this.a = new Titlescreen(conn);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.setSize(400, 200);
		a.setVisible(true);
	}
}
