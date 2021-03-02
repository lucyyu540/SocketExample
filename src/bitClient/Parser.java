package bitClient;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bitServer.Account;

/*
[client -> Server]
   "MakeAccount@111#ccm#1000"
   
[server -> Client]
	"MakeAccount_ack@111#S"   			"MakeAccount_ack@111#F"
	"SelectAccount_ack@S#111#ccm#1000"	"SelectAccount_ack@F#111"
*/

public class Parser {
	public static String receiveData(String msg, Bank bank) {
		String[] arr = msg.split("@");
		String method = arr[0];
		String[] in = new String[1];
		if(arr.length>1) in = arr[1].split("#");
		switch (method) {
		case "MakeAccount_ack": 
			int id = Integer.parseInt(in[0]);
			String success = in[1];
			bank.makeAccount_ack(id, success);
			break;
		case "SelectAccount_ack" : 
			success = in[0];
			if(success.equals("S")) {
				id = Integer.parseInt(in[1]);
				String name = in[2];
				int balance = Integer.parseInt(in[3]);
				String timestamp = in[4];
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    Date parsed;
				try {
					parsed = formatter.parse(timestamp);
					Timestamp newTime = new Timestamp(parsed.getTime());
					Account acc = new Account(id, name, balance, newTime);
					bank.selectAccount_ack(success, acc);
				} catch (ParseException e) {e.printStackTrace();}   
			}
			else bank.selectAccount_ack(success, null);
			break;
		case "Input_ack" : 
			success = in[0];
			if(success.equals("S")) {
				id = Integer.parseInt(in[1]);
				int amount = Integer.parseInt(in[2]);
				bank.input_ack(success, id, amount);
			}
			else bank.input_ack(success, -1, -1);
			break;
		case "Output_ack" : 
			success = in[0];
			if(success.equals("S")) {
				id = Integer.parseInt(in[1]);
				int amount = Integer.parseInt(in[2]);
				bank.output_ack(success, id, amount);
			}
			else bank.output_ack(success, -1, -1);
			break;
		case "DeleteAccount_ack" : 
			success = in[0];
			if(success.equals("S")) {
				id = Integer.parseInt(in[1]);
				bank.deleteAccount_ack(success, id);
			}
			else bank.deleteAccount_ack(success, -1);
			break;
		case "SelectAll_ack": 
			success = in[0];//#id@name@balance@time, #
			if(success.equals("S")) {
				ArrayList<Account> res = new ArrayList<Account>();
				for(int i = 1; i < in.length; i ++) {
					String[] temp = in[i].split("%");
					id = Integer.parseInt(temp[0]);
					String name = temp[1];
					int balance = Integer.parseInt(temp[2]);
					String timestamp = temp[3];
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    Date parsed;
					try {
						parsed = formatter.parse(timestamp);
						Timestamp newTime = new Timestamp(parsed.getTime());
						res.add(new Account(id, name, balance, newTime));
					} catch (ParseException e) {e.printStackTrace();}    					
				}
				bank.selectAll_ack(success, res);
			}
			else bank.selectAll_ack(success, null);
			break;
		}
		return null;
	}
}
