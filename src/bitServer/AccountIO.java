package bitServer;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AccountIO {
	private int id;
	private int input;
	private int output;
	private int balance;
	private Timestamp time;
	public AccountIO(int id, int input, int output, int balance, Timestamp date) {
		this.setId(id);
		this.setInput(input);
		this.setOutput(output);
		this.setBalance(balance);
		this.setTime(date);
	}
	public void print() {
		System.out.printf("[%d]\t %d\t %d\t %d\t %s %s\n", id,input, output, balance, getDate(), getTime());
	}
	/*HELPER*/
	private String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(this.time.getTime());
	}
	private String getTime() {
		return new SimpleDateFormat("HH:mm:ss").format(this.time.getTime());
	}
	/*GETTER SETTER*/
	public int getId() {return id;}
	private void setId(int id) { this.id = id; }
	private void setInput(int input) { this.input = input; }
	private void setOutput(int output) {this.output = output;}
	private void setBalance(int balance) {this.balance = balance;}
	private void setTime(Timestamp date) {this.time = date;}
}
