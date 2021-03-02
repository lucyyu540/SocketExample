package bitServer;

import java.util.ArrayList;

/*
[client -> Server]
   "MakeAccount@111#ccm#1000"	//계좌번호, 이름, 입금액
   "SelectAccount@111"			//계좌번호
   "InputAccount@111#1000"  	//계좌번호, 입금액
   "OutputAccount@111#1000"		//계좌번호, 출금액
   "DeleteAccount@111"			//계좌번호
   "SelectAllAccount@"			//없음.
   
[server -> Client]
	"MakeAccount_ack@111#S"   			"MakeAccount_ack@111#F"
	"SelectAccount_ack@S#111#ccm#1000"	"SelectAccount_ack@F#111"
*/
public class Packet {
	public static String makeAccount_ack(int id, boolean success) {
		String msg = "MakeAccount_ack@" + id + "#" ;
		return success == true ? (msg+="S") : (msg+="F");
	}
	public static String selectAccount_ack(Account acc, boolean success) {
		String msg = "SelectAccount_ack@";
		if(success) msg += "S#"+ acc.getId() + "#"+acc.getName() +  "#"+acc.getBalance()+"#" + acc.getNewTime();
		else msg += "F";
		return msg;
	}
	public static String input_ack(int id, int amount, boolean success) {
		String msg = "Input_ack@";
		if(success) msg += "S#"+ id+ "#" + amount;
		else msg += "F#"+ id + "#" + amount;
		return msg;
	}
	public static String output_ack(int id,int amount, boolean success) {
		String msg = "Output_ack@";
		if(success) msg += "S#"+ id+ "#" + amount;
		else msg += "F#"+ id+ "#" + amount;
		return msg;
	}
	public static String deleteAccount_ack(int id, boolean success) {
		String msg = "DeleteAccount_ack@";
		if(success) msg += "S#"+ id;
		else msg += "F#"+ id;
		return msg;
	}
	public static String selectAll_ack(ArrayList<Account> res, boolean success) {
		String msg = "SelectAll_ack@";
		if(success) {
			msg += "S";
			for (Account acc : res)  {
				msg += "#" + acc.getId() + "%"+ acc.getName() + "%"+ acc.getBalance()+ "%"+ acc.getNewTime();
			}
		}
		else msg += "F";
		return msg;
	}

}
