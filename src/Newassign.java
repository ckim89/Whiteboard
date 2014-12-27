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
 * This class is created when an instructor wants to create a new assignment for their class
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Newassign extends JFrame{
	
	private JLabel promptname;
	private JLabel promptmax;
	private JLabel promptweight;
	private JTextField assignname;
	private JTextField maxpoints;
	private JTextField weight;
	private JButton submit;
	private JButton back;
	private String name;
	private Connection conn;
	private String id;
	
	/**
	 * This constructs the new frame. It allows the instructor to add the name of the
	 * new assignment, the max number of points on that assignment, and the weight of that
	 * assignment.
	 * 
	 * @param name is the name of the user
	 * @param conn is the connection to the database
	 * @param id is the id of the user.
	 */
	public Newassign(String name, Connection conn, String id) {
		super("New Assignment");
		this.conn = conn;
		this.name = name;
		setLayout(new FlowLayout());
		this.id = id;
		promptname = new JLabel("Add the name of the assignment:");
		add(promptname);
		assignname = new JTextField(30);
		add(assignname);
		promptmax = new JLabel("Add the maximum number of points:");
		add(promptmax);
		maxpoints = new JTextField(10);
		add(maxpoints);
		promptweight = new JLabel("Add the weight of the assignment");
		add(promptweight);
		weight = new JTextField(10);
		add(weight);
		back = new JButton("Back");
		add(back);
		submit = new JButton("Submit");
		add(submit);
		Teventhandler event = new Teventhandler();
		back.addActionListener(event);
		submit.addActionListener(event);
		
		
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
		 * This method waits for a button to be pressed. If the user presses submit,
		 * we take up all the inputs that are given and change the table of that instructor's class.
		 * We create a new column in the database table with that assignment name and place the values of 
		 * the total and weight inside of it.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == back) {
				Instructor teach = new Instructor(name, conn, id);
				teach.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				teach.setSize(1000, 600);
				teach.setVisible(true);
				Newassign.this.dispose();
			}
			if (event.getSource() == submit) {
				String assname = assignname.getText();
				String max = maxpoints.getText();
				String pweight = weight.getText();
				int pmax = Integer.parseInt(max);
				int ppweight = Integer.parseInt(pweight);
				Statement mystate;
				//once submitted, we can change the table to add the new assignment in.
				try {
					mystate = conn.createStatement();
					String query = "ALTER TABLE " + Newassign.this.name + " ADD " + assname + " INT";
					int rstwo = mystate.executeUpdate(query);
					String query2 = "UPDATE " + Newassign.this.name + " SET " + assname + "=" + max + " WHERE StuID=\"" + "Total" + "\"";
					String query3 = "UPDATE " + Newassign.this.name + " SET " + assname + "=" + pweight + " WHERE StuID=\"" + "Weight" + "\"";
					int rs = mystate.executeUpdate(query2);
					rs = mystate.executeUpdate(query3);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Instructor teach = new Instructor(name, conn, id);
				teach.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				teach.setSize(1000, 600);
				teach.setVisible(true);
				Newassign.this.dispose();
			}
		}
	}
}
