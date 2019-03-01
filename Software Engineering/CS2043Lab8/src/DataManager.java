import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar; 

public class DataManager {
	
	private Connection con;

	public DataManager() {
	     try {
	         Class.forName("com.mysql.jdbc.Driver").newInstance();
	     } catch (Exception e) {
	      System.err.println(e.toString());
	     }
	     String url = "jdbc:mysql://isel.cs.unb.ca:3306/macKenzieaDB";
		try {
		con = DriverManager.getConnection(url, "macKenziea", "macKenziea");
		} catch (SQLException e) {
		System.err.println("Database connection error.");
		}
	}
	
	public ArrayList<BookObj> getAllBooks() {
		ArrayList<BookObj> bookList = new ArrayList<BookObj>();
		try {
			Statement st = con.createStatement();
			String sqlQuery = "select * from BookTable";
			ResultSet rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				BookObj book = new BookObj();
				book.id = rs.getString(1);
				book.title = rs.getString(2);
				book.description = rs.getString(3);
				book.author = rs.getString(4);
				book.isbn = rs.getString(5);
				book.publisher = rs.getString(6);
				book.year = rs.getInt(7);
				book.inventory = rs.getInt(8);
				bookList.add(book);
			}
		} catch (SQLException e) {
			System.err.println("SQL error: getAllBooks");
		}
		return bookList;
	}
	
	public void addCustomerInfo(CustomerObj customer){
		try {
			Statement st = con.createStatement();
			String sqlQuery = "insert into CustomerInfoTable values " +
					"('" + customer.id + "','" + customer.firstName + "','" +
					customer.lastName + "','" + customer.address + "','" +
					customer.contactInfo + "')";
			System.out.println(sqlQuery);
			st.executeUpdate(sqlQuery);
			} catch (SQLException e) {
				System.err.println("SQL error: addCustomerInfo");
			}
	}
	
	public void addOrder(String[] bookIds, String shippingAddress, String customerId) {
		try {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String date = year + "-" + month + "-" + day;
			
			Statement st = con.createStatement();
			String sqlQuery = "insert into OrderTable (CustomerId, Date, ShippingAddress, Status) " +
					"values " + "('" + customerId + "','" + date + "','" +
					shippingAddress + "','New')";
			st.executeUpdate(sqlQuery);
			
			sqlQuery = "select MAX(Id) from OrderTable";
			ResultSet rs = st.executeQuery(sqlQuery);
			int orderId = 0;
			if(rs.next()) orderId = rs.getInt(1);
			
			for (int i = 0; i < bookIds.length; i++) {
				sqlQuery = "insert into BookOrderTable values " +
				    "('" + bookIds[i] +"','" + orderId + "')";
				st.executeUpdate(sqlQuery);
			}
			} catch (SQLException e) {
				System.err.println("SQL error: addOrder");
			}
	}
}