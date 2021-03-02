package bitClient;


public class App {
	/*SINGLETON*/
	private App() { init(); }
	private static App singleton = new App();//only instantiated ONCE
	public static App getInstance() { return singleton; } //public access this instance
	
	private Bank bank = Bank.getInstance();
	public void init() { BitGlobal.logo(); }
	
	public void run() {
		while (true) {
			bank.selectAll();
			switch(BitGlobal.menu()) {
			case 1: //create new acc
				bank.makeAccount(); break;
			case 2: //select account
				bank.selectAccount(); break;
			case 3: //deposit
				bank.transaction(true); break;
			case 4: //withdraw
				bank.transaction(false); break;
			case 5: //delete
				bank.deleteAccount(); break;
			case 6: return;//exit
			}
			BitGlobal.pause();
				
		}
		
	}
	public void exit() { BitGlobal.ending(); }
}
