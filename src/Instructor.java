import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the Grade Center Frame. Only accessible to instructors when they log in.
 * @author Chang Bum Kim
 * @author Elaine Chao
 */
public class Instructor extends JFrame {

	private JLabel title;
	private String operation;
	private boolean finish = false;
	private Connection conn;
	private JButton back;
	private String name;
	private String id;
	private JButton addassign;
	private JTable table;
	private List<String> colnames = new ArrayList<String>();
	private int srow = 0;
	private int scol = 0;

	/**
	 * This constructs the new Frame that is the Grade Center. It finds the instructor's class using the instructor's
	 * last name and allows the instructor to add new assignments or add new grades.
	 * 
	 * @param name is the user's name
	 * @param conn is the connection 
	 * @param id is the user's id
	 */
	public Instructor(String name, Connection conn, String id) {
		super("Grade Center");
		this.conn = conn;
		this.name = name;
		this.id = id;
		Statement mystate;
		ResultSet rs = null;
		table = null;
		addassign = new JButton("Add New Assignment");
		JScrollPane newpane = null;
		
		//creates a table based on the query given.
		try {
			mystate = conn.createStatement();
			rs = mystate.executeQuery("SELECT * FROM " + this.name);
			table = new JTable(buildTable(rs));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		title = new JLabel("Here is your class, " + name);
		add(title);
		setLayout(new FlowLayout());
		back = new JButton("Back");
		add(back);
		newpane = new JScrollPane(table);
		//Waits for a click event from the mouse
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			/**
			 * This method gives the values of row index and column index where the mouse was 
			 * in when it clicked on a cell.
			 * 
			 * @param e is the click event
			 */
		    public void valueChanged(ListSelectionEvent e) {
		        int row = Instructor.this.table.getSelectedRow();
		        int col = Instructor.this.table.getSelectedColumn();
		        srow = row;
		        scol = col;
		    }
		});
		table.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
			/**
			 * This method checks if a cell was being editted but was canceled.
			 * 
			 * @param e is the cell changed event.
			 */
			public void editingCanceled(ChangeEvent e) {
			}
			/**
			 * This method checks to see if a cell has stopped being editted. We use this event to
			 * update our database.
			 * 
			 * @param e is the cell changed event
			 */
			public void editingStopped(ChangeEvent e) {
				String colname = colnames.get(scol);
				String rowname = (String) table.getModel().getValueAt(srow, 0);
				int change = Integer.parseInt((String) Instructor.this.table.getModel().getValueAt(srow, scol));
				Statement mystatetwo;
				try {
					mystatetwo = conn.createStatement();
					String query1 = "UPDATE " + Instructor.this.name + " SET " + colname + "=" + change + " WHERE StuID=\"" + rowname + "\"";
					int rstwo = mystatetwo.executeUpdate(query1);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		add(newpane);
		add(addassign);
		Teventhandler event = new Teventhandler();
		back.addActionListener(event);		
		addassign.addActionListener(event);
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
		 * to happen.
		 * 
		 * @param event is the button press.
		 */
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == back) {
				Mainmenu menu = new Mainmenu(name, conn, id);
				menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				menu.setSize(600, 300);
				menu.setVisible(true);
				Instructor.this.dispose();
			}
			if (event.getSource() == addassign) {
				Newassign ass = new Newassign(name, conn, id); 
				ass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ass.setSize(400,200);
				ass.setVisible(true);
				Instructor.this.dispose();
			}
		}
	}
	
	/**
	 * This method creates a new table from the results of a query.
	 * It creates vectors that hold the columns and rows and returns it 
	 * back out in a DefaultTableModel, which can be used to create a JTable.
	 * This method was adapted from stackoverflow.
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
	    for (int i=0;i<columnNames.size();i++) {
	    	Instructor.this.colnames.add(columnNames.get(i));
	    }
	    return new DefaultTableModel(data, columnNames);
	}
	
}
