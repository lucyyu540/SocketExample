package bitClient;
/*
[client -> Server]
   "MakeAccount@111#ccm#1000"
   
[server -> Client]
	"MakeAccount_ck@111#S"
	"MakeAccount_ack@111#F"
*/
public class Packet {
	public static String makeAccount(int id, String name, int balance) {
		String msg  = "MakeAccount@" + id + "#" + name + "#" + balance;
		return msg;
	}
	public static String selectAccount(int id) {
		String msg = "SelectAccount@" + id;
		return msg;
	}
	public static String input(int id, int amount) {
		String msg = "Input@" + id + "#" + amount;
		return msg;
	}
	public static String output(int id, int amount) {
		String msg = "Output@" + id + "#" + amount;
		return msg;
	}
	public static String deleteAccount(int id) {
		String msg = "DeleteAccount@" + id;
		return msg;
	}
	public static String selectAll() {
		String msg = "SelectAll@";
		return msg;
	}
	

}
