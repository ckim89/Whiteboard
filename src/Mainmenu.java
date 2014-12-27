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
 * This class creates the main menu frame of the GUI.
 * We allow instructors to access their class grades here and students to
 * access only their grades in their classes.
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Mainmenu extends JFrame {
	
	private JLabel welcome;
	private JButton cla;
	private JButton instruct;
	private JButton log;
	private JButton settings;
	private String name;
	private Connection conn;
	private String id;
	
	/**
	 * This constructs a new Main menu frame that has the buttons to redirect 
	 * a user to what they want to do according to their status.
	 * 
	 * @param name is the user's name
	 * @param conn is the connection to the database
	 * @param id is the id of the user.
	 */
	public Mainmenu(String name, Connection conn, String id) {
		super("Whiteboard");
		this.conn = conn;
		this.name = name;
		this.id = id;
		setLayout(new FlowLayout());
		welcome = new JLabel("Welcome to Whiteboard " + this.name);
		add(welcome);
		cla = new JButton("View your classes");
		add(cla);
		instruct = new JButton("Grading: (For Instructors)");
		add(instruct);
		settings = new JButton("Settings");
		add(settings);
		log = new JButton("Log Out");
		add(log);
		Meventhandler event = new Meventhandler();
		settings.addActionListener(event);
		cla.addActionListener(event);
		instruct.addActionListener(event);
		log.addActionListener(event);
		
	}
	
	/**
	 * This class serves as our event handling class. It waits for the user to press
	 * one of the JButtons and performs the correct functions accordingly.
	 * 
	 * @author Elaine Chao
	 * @author Chang Bum Kim
	 */
	private class Meventhandler implements ActionListener {
		/**
		 * This method waits for a button press. If a user is a student and they press 
		 * view classes, a new frame will be created that shows all the students grades in their
		 * classes. If a user is a student and tries to click on the instructor's button, they will be 
		 * denied access. This is the same vice versa. both instructor and student has access to the 
		 * settings button.
		 * 
		 * @param event is the button press
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == cla) {
				//Check to see if the user is an instructor or a student.
				try {
					Statement mystate = conn.createStatement();
					String query = "SELECT Fname FROM Student WHERE Student.ID=\"" + id + "\"";
					ResultSet rs = mystate.executeQuery(query);
					int count = 0;
					String temp = "";
					while(rs.next()) {
						count++;
						temp = (String) rs.getObject(count);
					}
					if (count == 0) {
						JOptionPane.showMessageDialog(null, "You are not a student... You are an Instructor");
					}
					else {
						Classes clas = new Classes(name, conn, id);
						clas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						clas.setSize(800, 400);
						clas.setVisible(true);
						Mainmenu.this.dispose();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (event.getSource() == instruct) {
				//Check to see if the user is an instructor or not.
				try {
					Statement mystate = conn.createStatement();
					String query = "SELECT Lname FROM Instructor WHERE Instructor.FacID=\"" + id + "\"";
					ResultSet rs = mystate.executeQuery(query);
					int count = 0;
					String temp = "";
					while(rs.next()) {
						count++;
						temp = (String) rs.getObject(count);
					}
					if (count == 0) {
						JOptionPane.showMessageDialog(null, "You are not authorized!");
					}
					else {
						Instructor ins = new Instructor(name, conn, id);
						ins.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						ins.setSize(1000, 600);
						ins.setVisible(true);
						Mainmenu.this.dispose();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (event.getSource() == log) {
				//TODO: close everything.
				Titlescreen back = new Titlescreen(conn);
				back.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				back.setSize(400, 200);
				back.setVisible(true);
				Mainmenu.this.dispose();
			}
			else if (event.getSource() == settings) {
				Settings set = new Settings(name, conn, id);
				set.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				set.setSize(400, 200);
				set.setVisible(true);
				Mainmenu.this.dispose();
			}

		}
		
	}	
}
