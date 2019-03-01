/**
 * DataManger -- talks to the database for the server
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataManager {
	
	private Connection con;

	public DataManager() {
	     try {
	         Class.forName("com.mysql.jdbc.Driver").newInstance();
	     } catch (Exception e) {
	      System.err.println(e.toString());
	     }
	     String url = "jdbc:mysql://isel.cs.unb.ca:3306/cs2043team2aDB";
		try {
		con = DriverManager.getConnection(url, "cs2043team2a", "cs2043team2a");
		} catch (SQLException e) {
		System.err.println("Database connection error.");
		}
	}
	
	public ArrayList<userObj> getUserList() {
		ArrayList<userObj> userList = new ArrayList<userObj>();
		try {
			Statement st = con.createStatement();
			String sqlQuery = "select * from userTable";
			ResultSet rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				userObj user = new userObj();
				user.hash = rs.getString(2);
				//user.publicKey = rs.getBytes(3);
				userList.add(user);
			}
		} catch (SQLException e) {
			System.err.println("SQL error: getUserLost");
		}
		return userList;
	}
	/**
	public static byte[] getReturningUser(String temp){
		userObj user = new userObj();
		user.hash = temp;
		try {
			Statement st = con.createStatement();
			String sqlQuery = "select * from userTable";
			ResultSet rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				userObj userTemp = new userObj();
				//userTemp.id = rs.getString(1);
				userTemp.hash = rs.getString(2);
				//userTemp.publicKey = rs.getBytes(3);
				if(userTemp.hash.equals(user.hash)){
					user.publicKey = userTemp.publicKey;
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error: getUserLost");
		}
		return user.publicKey;
	}*/
	
	public void addUser(userObj user){
		try {
			Statement st = con.createStatement();
			String sqlQuery = "insert into userTable values " +
					"('" + user.hash + "')";
			System.out.println(sqlQuery);
			st.executeUpdate(sqlQuery);
			} catch (SQLException e) {
				System.err.println("SQL error: addUser");
			}
	}
	public void removeUser(userObj user){
		try {
			Statement st = con.createStatement();
			String sqlQuery = "delete from userTable values " +
					"('" + user.hash + "')";
			System.out.println(sqlQuery);
			st.executeUpdate(sqlQuery);
			} catch (SQLException e) {
				System.err.println("SQL error: removeUser");
			}
	}
}