package bitClient;

public class Start {
	public static void main (String[] args) {
		//new TciIpMultiClient().run();
		App app = App.getInstance();
		app.run();
		app.exit();
	}

}
