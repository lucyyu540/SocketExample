package bitServer;
import java.sql.*;
import java.util.ArrayList;
/*USING PREPARED STATEMENTS*/
public class AccountDB1 {
	Connection con;
	PreparedStatement st;
	public boolean run() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("*driver loaded*");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/accountManagement?serverTimezone=UTC","root","password");
			System.out.println("*database connected*");
			BitGlobalStatement.init(con);
			return true;
		}
		catch (Exception e) { 
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/*QUERIES*/
	public boolean insert(int id, String name, int balance) {
		try {
			BitGlobalStatement.state_insert.setInt(1, id);
			BitGlobalStatement.state_insert.setString(2, name);
			BitGlobalStatement.state_insert.setInt(3, balance);
			int i = BitGlobalStatement.state_insert.executeUpdate();
			if(i > 0) return true;
			throw new Exception();
		}
		catch(Exception e) {
			System.out.println("[ERROR] insert(id,name,bal) " + e.getMessage());
			return false;}
	}
	public Account select(int id) {
		try {
			BitGlobalStatement.state_select.setInt(1, id);
			ResultSet rs =  BitGlobalStatement.state_select.executeQuery();
			rs.next();
			int accid = rs.getInt("accid");
			int balance = rs.getInt("balance");
			Timestamp newTime = rs.getTimestamp("newTime");
			String name = rs.getString("name");
			return new Account(accid, name, balance, newTime);
			
		} catch (Exception e) {
			System.out.println("[ERROR] select(id) " + e.getMessage());
			return null;
		}
	}
	public ArrayList<Account> selectAll() {
		try {
			ResultSet rs =  BitGlobalStatement.state_selectAll.executeQuery();
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
			int i;
			if(deposit) {
				BitGlobalStatement.state_updateInput.setInt(1, money);
				BitGlobalStatement.state_updateInput.setInt(2, id);
				i = BitGlobalStatement.state_updateInput.executeUpdate();

			}
			else {
				BitGlobalStatement.state_updateOutput.setInt(1, money);
				BitGlobalStatement.state_updateOutput.setInt(2, id);
				BitGlobalStatement.state_updateOutput.setInt(3, money);
				i = BitGlobalStatement.state_updateOutput.executeUpdate();
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
		try {
			BitGlobalStatement.state_delete.setInt(1, id);
			int i = BitGlobalStatement.state_delete.executeUpdate();
			if(i > 0) return true;
			throw new Exception();			
		}
		catch (Exception e) {
			System.out.println("[ERROR] delete(id) " + e.getMessage());
			return false;
		}
	}

}
class BitGlobalStatement {
	public static PreparedStatement state_insert;
	public static PreparedStatement state_select;
	public static PreparedStatement state_selectAll;
	public static PreparedStatement state_updateOutput;
	public static PreparedStatement state_updateInput;
	public static PreparedStatement state_delete;

	public static void init(Connection con) {
		try {
			String insert = "insert into account(accid, name, balance) values(?,?,?);";
			state_insert = con.prepareStatement(insert);
			
			String select = "select * from account where accid = ?;";
			state_select = con.prepareStatement(select);
			String selectAll = "select * from account;";
			state_selectAll = con.prepareStatement(selectAll);
			
			String updateInput = "update account set balance=balance+? where accid = ?";
			state_updateInput = con.prepareStatement(updateInput);
			
			String updateOutput = "update account set balance=balance-? where accid = ? and balance >= ?";
			state_updateOutput = con.prepareStatement(updateOutput);
			
			String delete = "delete from account where accid = ?";
			state_delete = con.prepareStatement(delete);
		}
		catch(Exception e) {System.out.println("bitglobalstatement: " + e.getMessage());}
	}
}
