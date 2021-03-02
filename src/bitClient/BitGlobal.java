package bitClient;

import java.util.Scanner;

public class BitGlobal {
	static Scanner scn = new Scanner(System.in);
	public static void pause() {
		System.out.print("press enter >> ");
		scn.nextLine();
	}
	public static int menu() {
		System.out.println("==================================");
		System.out.println("1) create new account");
		System.out.println("2) select account");
		System.out.println("3) deposit");
		System.out.println("4) withdraw");
		System.out.println("5) delete account");
		System.out.println("6) exit");
		System.out.println("==================================");
		System.out.print(">> ");
		return Integer.parseInt(scn.nextLine());
	}
	public static void logo() {
		System.out.println("client-server account management program starting ************");
		pause();
	}
	public static void ending() {
		System.out.println("*************program exiting");
	}
	/*SCANNER*/
	public static int inputNumber(String msg) {
		System.out.print(msg+" : ");
		return Integer.parseInt(scn.nextLine());
	}
	public static String inputString(String msg) {
		System.out.print(msg+" : ");
		return scn.nextLine();
	}
}
