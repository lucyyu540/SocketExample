package bitServer;
/*
[client -> Server]
   "MakeAccount@111#ccm#1000"
   
[server -> Client]
	"MakeAccount_ack@111#S"   			"MakeAccount_ack@111#F"
	"SelectAccount_ack@S#111#ccm#1000"	"SelectAccount_ack@F#111"
*/

public class Parser {
	public static String receiveData(String msg) {
		String[] arr = msg.split("@");
		String method = arr[0];
		String[] in = new String[1];
		if(arr.length>1) in = arr[1].split("#");
		switch (method) {
		case "MakeAccount": 
			int id = Integer.parseInt(in[0]);
			String name = in[1];
			int balance = Integer.parseInt(in[2]);
			return Manager.getInstance().makeAccount(id, name, balance);
		case "SelectAccount" : 
			id = Integer.parseInt(in[0]);
			return Manager.getInstance().selectAccount(id);
		case "Input" : 
			id = Integer.parseInt(in[0]);
			int amount = Integer.parseInt(in[1]);
			return Manager.getInstance().input(id, amount);
		case "Output" : 
			id = Integer.parseInt(in[0]);
			amount = Integer.parseInt(in[1]);
			return Manager.getInstance().output(id, amount);
		case "DeleteAccount" : 
			id = Integer.parseInt(in[0]);
			return Manager.getInstance().deleteAccount(id);
		case "SelectAll": 
			return Manager.getInstance().selectAll();
		}
		return null;
	}
}
