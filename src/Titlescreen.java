import javax.swing.JOptionPane;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;


/**
 * This class creates the log in screen. When the user applies the correct ID and password,
 * they will be logged in as either a student or an instructor.
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Titlescreen extends JFrame{
	
	private JButton button;
	private JLabel a;
	private JLabel b;
	private JTextField uid;
	private JPasswordField upass;
	private String name;
	private int count;
	private String id;
	private Connection conn;
	private boolean correct = false;
	
	/**
	 * This constructs the log in screen.
	 * 
	 * @param conn is the connection needed for the database.
	 */
	public Titlescreen(Connection conn) {
		super("Login");		
		this.conn = conn;
		setLayout(new FlowLayout());
		a = new JLabel("Please enter your ID: ");
		add(a);
		uid = new JTextField(15);
		add(uid);
		b = new JLabel("Please enter your Password: ");
		add(b);
		upass = new JPasswordField(15);
		add(upass);
		button = new JButton("Enter");
		add(button);
		Teventhandler event = new Teventhandler();
		button.addActionListener(event);
		
	}
	/**
	 * This class serves as our event handling class. It waits for the user to press
	 * one of the JButtons and performs the correct functions accordingly.
	 * 
	 * @author Elaine Chao
	 * @author Chang Bum Kim
	 */
	private class Teventhandler implements ActionListener {
		/**
		 * This method waits for an event to occur. In our case, we wait for button presses 
		 * to happen. If a user submits the incorrect password or ID, this method will output a 
		 * dialogue screen that alerts them. If the info is correct, we will create the main menu frame.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			String a = "";
			char[] b = new char[15];
			if (event.getSource() == button) {
				b = upass.getPassword();
				String s = "";
				for (int i = 0; i < b.length; i++) {
					s = s + b[i];
				}
				a = uid.getText();
				name = a;
				//Query to make sure that the user's id and password match up in either the student table or the instructor table.
				try {
					count = 0;
					Statement mystate = conn.createStatement();
					String query = "SELECT ID FROM Student WHERE Student.Pass=\"" + s + "\"";
					ResultSet rs = mystate.executeQuery(query);
					while(rs.next()) {
						count++;
						id = (String) rs.getObject(count);
						if (id.equals(a)) {
							correct = true;
						}
					}
					Statement mystate1 = conn.createStatement();
					String query1 = "SELECT FacID FROM Instructor WHERE Instructor.Pass=\"" + s + "\"";
					ResultSet rs1 = mystate1.executeQuery(query1);
					int temp;
					while(rs1.next()) {
						count++;
						temp = (int) rs1.getObject(count);
						id = "" + temp;
						if (id.equals(a)) {
							correct = true;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!correct) {
					JOptionPane.showMessageDialog(null, "Your password or username was invalid!");
					Titlescreen.this.dispose();
					Titlescreen titscreen = new Titlescreen(conn);
					titscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					titscreen.setSize(400, 200);
					titscreen.setVisible(true);
				} 
				else {
					try {
						Statement mystate = conn.createStatement();
						String query = "SELECT Fname FROM Student WHERE Student.ID=\"" + id + "\"";
						ResultSet rs = mystate.executeQuery(query);
						while(rs.next()) {
							name = (String) rs.getObject(count);
						}
						Statement mystate1 = conn.createStatement();
						String query1 = "SELECT Lname FROM Instructor WHERE Instructor.FacID=\"" + id + "\"";
						ResultSet rs1 = mystate1.executeQuery(query1);
						while(rs1.next()) {
							name = (String) rs1.getObject(count);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Mainmenu menu = new Mainmenu(name, conn, id);
					menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					menu.setSize(600, 300);
					menu.setVisible(true);
					Titlescreen.this.dispose();
				}
			}
		}
	}
}
