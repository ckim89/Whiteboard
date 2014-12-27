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
 * This class creates the Password change frame. Once in this frame, the GUI will 
 * prompt the user to place their old password along with their new password.
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Passwordchange extends JFrame {
	
	private JLabel oldprompt;
	private JLabel newprompt;
	private JLabel confirm;
	private boolean finish = false;
	private JTextField oldpass = null;
	private JTextField newpass = null;
	private JTextField confirmation = null;
	private JButton enter = null;
	private JButton back = null;
	private String name;
	private Connection conn;
	private String id;
	
	/**
	 * This constructs the new Password change frame.
	 * 
	 * @param name is the name of the user
	 * @param conn is the connection to the database
	 * @param id is the id of ths user
	 */
	public Passwordchange(String name, Connection conn, String id) {
		super("Change your password");
		setLayout(new FlowLayout());
		this.name = name;
		this.conn = conn;
		this.id = id;
		oldprompt = new JLabel("Please enter your old password");
		add(oldprompt);
		oldpass = new JTextField(15);
		add(oldpass);
		newprompt = new JLabel("Please enter your new password");
		add(newprompt);
		newpass = new JTextField(15);
		add(newpass);
		confirm = new JLabel("Please enter your new password to confirm");
		add(confirm);
		confirmation = new JTextField(15);
		add(confirmation);
		enter = new JButton("Enter");
		add(enter);
		back = new JButton("Back");
		add(back);
		
		Teventhandler event = new Teventhandler();
		enter.addActionListener(event);
		back.addActionListener(event);
		
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
		 * to happen. If the submit button is pressed, this method grabs all the String inputted by 
		 * the user and makes sure that the info is valid. If it is, we update the database with 
		 * the new password and alert the user that their password has been changed. If not, we 
		 * alert the user that the info is not valid.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == back) {
				Settings set = new Settings(name, conn, id);
				set.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				set.setSize(400, 200);
				set.setVisible(true);
				Passwordchange.this.dispose();
			}	
			else if (event.getSource() == enter) {
				String old = oldpass.getText();
				String newer = newpass.getText();
				String comf = confirmation.getText();
				String temp = "";
				String temptwo = "";
				Boolean stu = false;
				//check the two new passwords match up.
				if (comf.equals(newer)) {
					//query to check if the old password gives the student ID currently
					try {
						Statement mystate = conn.createStatement();
						String query = "SELECT ID FROM Student WHERE Student.Pass=\"" + old + "\"";
						ResultSet rs = mystate.executeQuery(query);
						while(rs.next()) {
							temp = (String) rs.getObject(1);
							stu = true;
						}
						Statement mystate1 = conn.createStatement();
						String query1 = "SELECT FacID FROM Instructor WHERE Instructor.Pass=\"" + old + "\"";
						ResultSet rs1 = mystate1.executeQuery(query1);
						int temp1;
						while(rs1.next()) {
							temp1 = (int) rs1.getObject(1);
							temp = temp + temp1;
						}
						if (temp.equals(id)) {
							if (stu) {
								Statement mystatetwo = conn.createStatement();
								String querytwo = "UPDATE Student SET pass=\"" + newer + "\" WHERE ID=\"" + id + "\"";
								int rstwo = mystatetwo.executeUpdate(querytwo);
								JOptionPane.showMessageDialog(null, "Your password has been changed!");
							}
							else {
								Statement mystatetwo = conn.createStatement();
								String querytwo = "UPDATE Instructor SET pass=\"" + newer + "\" WHERE FacID=\"" + id + "\"";
								int rstwo = mystatetwo.executeUpdate(querytwo);
								JOptionPane.showMessageDialog(null, "Your password has been changed!");
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Your old password was not correct!");
				}
				else {
					JOptionPane.showMessageDialog(null, "Your new passwords do not match!");
				}
			}
		}
	}
}
