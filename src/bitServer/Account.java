package bitServer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Account {
	private int accid;
	private int balance;
	private Timestamp newTime;
	private String name;
	/*CONSTRUCTORS*/
	public Account(int id, String name, int balance, Timestamp date) {
		this.setId(id);
		this.setName(name);
		this.setBalance(balance);
		this.setNewTime(date);
	}
	public Account(int id, String name, int balance) {
		this(id, name, balance, new Timestamp(new Date().getTime()));
		// TODO Auto-generated constructor stub
	}
	/*METHODS*/
	public void inputMoney(int m) throws Exception {
		if(m<=0) throw new Exception("Invalid input!");
		this.setBalance(this.balance+m);
	}
	public void outputMoney(int m) throws Exception {
		if(m<=0) throw new Exception("Invalid input!");
		if(this.balance<m) throw new Exception("Insufficient balance!");
		this.setBalance(this.balance-m);
	}
	public void printLn() {
		System.out.println("[ID]: " + this.accid);
		System.out.println("[Name]: " + this.name);
		System.out.println("[balance]: ₩" + this.balance);
		System.out.println("[Date opened]: " + getDate());
		System.out.println("[Time opened]: " + getTime());
	}
	public void print() {
		System.out.printf("[%d] %s %d %s %s\n", accid, name, balance, getDate(), getTime());
	}
	/*HELPER*/
	private String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(this.newTime.getTime());
	}
	private String getTime() {
		return new SimpleDateFormat("HH:mm:ss").format(this.newTime.getTime());
	}
	/*GETTER SETTER*/
	public int getId() { return accid; }
	private void setId(int id) { this.accid = id; }
	public int getBalance() { return balance; }
	private void setBalance(int balance) { this.balance = balance; }
	public Timestamp getNewTime() {return this.newTime;}
	private void setNewTime(Timestamp date) { this.newTime = date; }
	public String getName() { return this.name; }
	private void setName(String name) { this.name = name; }
}
