package bitServer;

import java.util.ArrayList;

public class Manager {
	private TcpIpMultiServer server;
	//private AccountDB1 db;
	private AccountDB2 db;
	/*SINGLETON*/
	private static Manager Singleton = new Manager();
	public static Manager getInstance() {
		return Singleton;
	}
	private Manager() {
		db = new AccountDB2();
		//db = new AccountDB1();
		server = new TcpIpMultiServer();//통신 
	}
	/*RUN SERVER AND DATABASE*/
	public void run() {
		if(!db.run()) {
			System.out.println("database failed. exiting");
			System.exit(0);
		}
		server.run();
	}
	/*PARSE CLIENTS MESSAGE*/
	public String receiveData(String msg) {
		return Parser.receiveData(msg);
		
	}
	/* METHODS */
	public String makeAccount(int id, String name, int balance) {
		Account acc = new Account(id, name, balance);
		acc.print();
		//if successfully saved in db return ACK message
		if(db.insert(id, name, balance)) return Packet.makeAccount_ack(id, true);
		return Packet.makeAccount_ack(id, false);
		
	}
	public String selectAccount(int id) {
		//get account from db
		Account acc = db.select(id);
		//if found account
		if(acc != null) return Packet.selectAccount_ack(acc, true);
		else return Packet.selectAccount_ack(null, false);
	}
	public String input(int id, int amount) {
		if(db.update(id, amount, true)) return Packet.input_ack(id, amount, true);
		else return Packet.input_ack(id, amount, false);
		
	}
	public String output(int id, int amount) {
		System.out.println("in manager output(): " + id + " " + amount);
		if(db.update(id, amount, false)) return Packet.output_ack(id, amount, true);
		else return Packet.output_ack(id, amount, false);
	}
	public String deleteAccount(int id) {
		if(db.delete(id)) return Packet.deleteAccount_ack(id, true);
		else return Packet.deleteAccount_ack(id, false);
	}
	public String selectAll() {
		// TODO Auto-generated method stub
		ArrayList<Account> res = db.selectAll();
		if(res != null) return Packet.selectAll_ack(res, true);
		else return Packet.selectAll_ack(null, false);
	}
}
