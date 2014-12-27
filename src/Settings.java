import javax.swing.JOptionPane;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

/**
 * This class creates the Settings Frame. This frame holds the setting to change your password.
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Settings extends JFrame {
	
	private String operation;
	private boolean finish = false;
	private JButton back = null;
	private JButton pass = null;
	private String name;
	private Connection conn;
	private String id;
	
	/**
	 * This constructs a new settings frame. 
	 * 
	 * @param name is the name of the user
	 * @param conn is the connection to the database
	 * @param id is the id of the user.
	 */
	public Settings(String name, Connection conn, String id) {
		super("Settings");
		this.conn = conn;
		this.id = id;
		setLayout(new FlowLayout());
		this.name = name;
		back = new JButton("Back");
		pass = new JButton("Change Password");
		add(pass);
		add(back);
		Teventhandler event = new Teventhandler();
		back.addActionListener(event);
		pass.addActionListener(event);
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
		 * to happen. If the change password button gets pressed, we create a new frame for that 
		 * functionality.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == back) {
				Mainmenu menu = new Mainmenu(name, conn, id);
				menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				menu.setSize(600, 300);
				menu.setVisible(true);
				Settings.this.dispose();
			}
			else if (event.getSource() == pass) {
				Passwordchange password = new Passwordchange(name, conn, id);
				password.setDefaultCloseOperation(EXIT_ON_CLOSE);
				password.setSize(600,300);
				password.setVisible(true);
				Settings.this.dispose();
			}
		}
	}
}
