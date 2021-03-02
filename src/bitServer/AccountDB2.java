package bitServer;
import java.sql.*;
import java.util.ArrayList;

/**USING PROCEDURES*/
public class AccountDB2 {
	Connection con;
	PreparedStatement st;
	public boolean run() {
		try {
			System.out.println("using procedures");
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("*driver loaded*");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/accountManagement?serverTimezone=UTC","root","password");
			System.out.println("*database connected*");
			Procedure.init(con);
			return true;
		}
		catch (Exception e) { 
			System.out.println("db connection error: " + e.getMessage());
			return false;
		}
	}
	
	/*QUERIES*/
	public boolean insert(int id, String name, int balance) {
		try {
			//create procedure MakeAccount( IN id INT, IN name varchar(20),  IN balance int)
			Procedure.state_insert.setInt(1, id);
			Procedure.state_insert.setString(2, name);
			Procedure.state_insert.setInt(3, balance);
			int i = Procedure.state_insert.executeUpdate();
			if(i > 0) return true;
			throw new Exception();
		}
		catch(Exception e) {
			System.out.println("[ERROR] insert(id,name,bal) " + e.getMessage());
			return false;}
	}
	public Account select(int id) {
		try {
			//create procedure SelectAccount(IN  id INT, OUT retname varchar(20), OUT retbalance int, OUT  retnewtime timestamp)
			Procedure.state_select.setInt(1, id);
			Procedure.state_select.registerOutParameter(2, Types.VARCHAR);//name
			Procedure.state_select.registerOutParameter(3,  Types.INTEGER);//balance
			Procedure.state_select.registerOutParameter(4,  Types.TIMESTAMP);//newtime
			Procedure.state_select.execute();
			int balance = Procedure.state_select.getInt("retbalance");
			Timestamp newTime = Procedure.state_select.getTimestamp("retnewTime");
			String name = Procedure.state_select.getString("retname");
			return new Account(id, name, balance, newTime);
			
		} catch (Exception e) {
			System.out.println("[ERROR] select(id) " + e.getMessage());
			return null;
		}
	}
	public ArrayList<Account> selectAll() {
		try {
			ResultSet rs =  Procedure.state_selectAll.executeQuery();
			ArrayList<Account> res = new ArrayList<Account>();
			while (rs.next()) {
				int accid = rs.getInt("accid");
				int balance = rs.getInt("balance");
				Timestamp newTime = rs.getTimestamp("newTime");
				String name = rs.getString("name");
				res.add(new Account(accid, name, balance, newTime));	
			}
			return res;
			
		} catch (Exception e) {
			System.out.println("[ERROR] selectAll() " + e.getMessage());
			return null;
		}
	}
	public boolean update(int id, int money, boolean deposit) {
		try {
			//create procedure InputAccount(IN  id INT, IN amount int)
			int i;
			if(deposit) {
				Procedure.state_updateInput.setInt(1, id);
				Procedure.state_updateInput.setInt(2, money);
				i = Procedure.state_updateInput.executeUpdate();

			}
			//create procedure OuputAccount(IN  id INT, IN amount int)
			else {
				Procedure.state_updateOutput.setInt(1, id);
				Procedure.state_updateOutput.setInt(2, money);
				i = Procedure.state_updateOutput.executeUpdate();
			}
			if(i > 0) return true;
			throw new Exception();			
		}
		catch (Exception e) {
			System.out.println("[ERROR] update(id, money, deposit) " + e.getMessage());
			return false;
		}
	}
	public boolean delete(int id) {
		//create procedure DeleteAccount(IN  id int)
		try {
			Procedure.state_delete.setInt(1, id);
			int i = Procedure.state_delete.executeUpdate();
			if(i > 0) return true;
			throw new Exception();			
		}
		catch (Exception e) {
			System.out.println("[ERROR] delete(id) " + e.getMessage());
			return false;
		}
	}
}
class Procedure {
	public static CallableStatement state_insert;
	public static CallableStatement state_select;
	public static CallableStatement state_selectAll;
	public static CallableStatement state_updateOutput;
	public static CallableStatement state_updateInput;
	public static CallableStatement state_delete;

	public static void init(Connection con) {
		System.out.println("initialized");
		try {
			String insert = "{call MakeAccount(?,?,?)};";
			state_insert = con.prepareCall(insert);
			
			String select = "{call SelectAccount(?,?,?,?)};";
			state_select = con.prepareCall(select);
			
			String selectAll = "select * from account;";
			state_selectAll = con.prepareCall(selectAll);
			
			String updateOutput = "{call OutputAccount(?,?)};";
			state_updateOutput = con.prepareCall(updateOutput);
			String updateInput = "{call InputAccount(?,?)};";
			state_updateInput = con.prepareCall(updateInput);
			
			String delete = "{call DeleteAccount(?)};";
			state_delete = con.prepareCall(delete);
		}
		catch(Exception e) {System.out.println(e.getMessage());}
	}
}
