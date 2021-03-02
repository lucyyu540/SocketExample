package bitClient;

import java.util.ArrayList;

import bitServer.Account;

public class Bank {
	/*FIELD*/
	TciIpMultiClient2 client;//쌍방참조 
	//private AccountDB db;
	/*SINGLETON****************************************************************/
	private Bank() {
		//db = new AccountDB();
		client = new TciIpMultiClient2(this);
		client.run();
	}
	private static Bank singleton = new Bank();
	public static Bank getInstance() { return singleton;}
	/*PARSE8******************************************************************/
	public void receiveData(String msg) {
		Parser.receiveData(msg, this);
	}
	/*METHODS - RECEIVED FROM SERVER********************************************/
	public void makeAccount_ack(int id, String success) {
		System.out.println("[RECEIVE STATUS]: MAKE ACCOUNT " + id + " " + success);
	}
	public void selectAccount_ack(String success, Account acc) {
		if(success.equals("S")) {
			System.out.print("[RECEIVE STATUS]: " + success);
			acc.printLn();
		}
		else System.out.println("[RECEIVE STATUS]: SELECT FAIL");
	}
	public void input_ack(String success, int id, int amount) {
		if(success.equals("S")) {
			System.out.println("[RECEIVE STATUS]: INPUT SUCCESS "+ id + " " + amount);
		}
		else System.out.println("[RECEIVE STATUS]: input failed " );
	}
	public void output_ack(String success, int id, int amount) {
		if(success.equals("S")) {
			System.out.println("[RECEIVE STATUS]: OUTPUT SUCESS " + id + " " + amount );
		}
		else System.out.println("[RECEIVE STATUS]: output failed");
	}
	public void deleteAccount_ack(String success, int id) {
		if(success.equals("S")) {
			System.out.println("[RECEIVE STATUS]: DELETE SUCESS " + id);
		}
		else System.out.println("[RECEIVE STATUS]: delete failed");
	}
	public void selectAll_ack(String success, ArrayList<Account> res) {
		if(success.equals("S")) {
			System.out.println("[RECEIVE STATUS]: SELECT ALL SUCCESS");
			for(Account acc : res) {
				acc.print();
			}
		}
		else System.out.println("[RECEIVE STATUS]: select all failed");
	}
	
	/*METHODS - SEND TO SERVER********************************************/
	public void makeAccount() {
		try {
			int id = BitGlobal.inputNumber("Enter id");
			String name = BitGlobal.inputString("Enter name");
			int balance = BitGlobal.inputNumber("Enter balance");
			String msg = Packet.makeAccount(id, name, balance);
			client.sendMessage(msg);
			System.out.println("sent new account creation to server");
		}
		catch(Exception e) {
			System.out.println("client failed to send new account: "+e.getMessage());
		}

	}
	public void selectAccount() {
		try {
			int id = BitGlobal.inputNumber("Enter id");
			String msg = Packet.selectAccount(id);
			client.sendMessage(msg);
			System.out.println("[select account sent to server]");
		}
		catch(Exception e) {
			System.out.println("[client failed to send select account]: "+e.getMessage());
		}
	}
	public void transaction(boolean deposit) {
		try {
			int id = BitGlobal.inputNumber("Enter id");
			int amount = BitGlobal.inputNumber("Enter amount");
			String msg = null;
			if(deposit) msg = Packet.input(id, amount);
			else msg = Packet.output(id, amount);
			client.sendMessage(msg);
		}
		catch(Exception e) {
			System.out.println("client failed to send transaction: "+e.getMessage());
		}
	}
	public void deleteAccount() {
		try {
			int id = BitGlobal.inputNumber("Enter id");
			String msg = Packet.deleteAccount(id);
			client.sendMessage(msg);
		}
		catch(Exception e) {
			System.out.println("client failed to send delete account: "+e.getMessage());
		}
	}
	public void selectAll() {
		try {
			String msg = Packet.selectAll();
			client.sendMessage(msg);
		}
		catch(Exception e) {
			System.out.println("client failed to send select all: "+e.getMessage());
		}
	}
	

}