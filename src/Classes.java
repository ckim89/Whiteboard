import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/**
 * This class creates the student's classes (and their grades in that class).
 * 
 * @author Elaine Chao
 * @author Chang Bum Kim
 */
public class Classes extends JFrame {
	
	private JLabel title;
	private boolean finish = false;
	private String operation;
	private Connection conn;
	private JButton back;
	private String name;
	private String id;
	
	/**
	 * This constructs the new frame that holds all of the students classes as well 
	 * as their grades in their classes.
	 * 
	 * @param name is the name of the user
	 * @param conn is the connection to the database
	 * @param id is the id of the user.
	 */
	public Classes(String name, Connection conn, String id) {
		super("Courses");
		this.conn = conn;
		this.id = id;
		this.name = name;
		List<String> lnames = new ArrayList<String>();
		Scanner line;
		title = new JLabel("Here are your classes, " + name);
		setLayout(new FlowLayout());
		add(title);
		back = new JButton("Back");
		Teventhandler event = new Teventhandler();
		back.addActionListener(event);
		//get all the instructor's names that the student has a class with.
		try {
			Statement mystate = conn.createStatement();
			ResultSet rs = mystate.executeQuery("SELECT Instructor_s_ FROM Enrolled_in, FallCourses WHERE "
					+ "Enrolled_in.CourseID=FallCourses.Class__ AND Enrolled_in.StuID=\"" + this.id + "\"");
			while (rs.next()) {
				String names = (String) rs.getString(1);
				line = new Scanner(names);
				line.next();
				lnames.add(line.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//for each class, create a new table that only shows that particular student's grades.
		for (int i=0;i<lnames.size();i++) {
			Statement mystate1;
			try {
				mystate1 = conn.createStatement();
				ResultSet rs1 = mystate1.executeQuery("SELECT * FROM " + lnames.get(i) + " WHERE StuID=\"" + this.id + "\"");
				JTable table = new JTable(buildTable(rs1));
				table.setPreferredScrollableViewportSize(table.getPreferredSize());
				JScrollPane newpane = new JScrollPane(table);
				JLabel explain = new JLabel(lnames.get(i) + "'s class");
				add(explain);
				add(newpane);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		add(back);
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
		 * This method waits for a button press. If the back button is pressed,
		 * we reroute back to the main menu.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == back) {
				Mainmenu menu = new Mainmenu(name, conn, id);
				menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				menu.setSize(600, 300);
				menu.setVisible(true);
				Classes.this.dispose();
			}
		}
	}
	/**
	 * This method creates a new table from the results of a query.
	 * It creates vectors that hold the columns and rows and retruns it 
	 * back out in a DefaultTableModel, which is used to create a JTable
	 * This code was adapted from stackoverflow
	 * 
	 * @param rs is the Resulting set from a query
	 * @return the new table model with the information added to it.
	 */
	public DefaultTableModel buildTable(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }
	    
	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }
	    return new DefaultTableModel(data, columnNames);
	}
}
