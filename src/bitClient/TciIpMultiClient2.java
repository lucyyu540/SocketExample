package bitClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class TciIpMultiClient2 {
	public static final int PORT = 4000;
	public static final String SERVER_IP = "127.0.0.1";//14.32.18.42,  127.0.0.1
	private ClientSender2 clientSender = null;
	private Bank bank;//쌍방 참조 
	/*CONSTRUCTOR*/ 
	public TciIpMultiClient2(Bank bank) {
		this.bank = bank;
	}
	public void run() {
		try{
			Socket socket = new Socket(SERVER_IP, PORT);
			System.out.println("서버 연결 됨");
			clientSender = new ClientSender2(socket);	//non thread
			Thread receiver = new ClientReceiver2(socket, bank); 		//thread	   
			receiver.start();
		}catch(ConnectException e){ 
			e.printStackTrace();
		}catch(Exception e) { }
	}
	public void sendMessage(String msg) {//client.sendMessage in bank
		try {
			System.out.println(msg);
			clientSender.sendMessage(msg);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
/*******************************************************/
class ClientSender2{
	 //Socket socket;
	 PrintWriter writer;
	 String name;
	 ClientSender2(Socket socket){
	  //6.소켓으로부터 인풋스크림, 아웃풋 스크림 얻음
		 //this.socket=socket;  
		 try{
			 writer = new PrintWriter(socket.getOutputStream());
		 }catch(Exception e) {}
	 }
	 public void sendMessage(String msg) {
		 writer.println(msg);
		 writer.flush();
		 System.out.println("sending to server " + msg);
	 }
}
/*******************************************************/
class ClientReceiver2 extends Thread{
	 Socket socket;
	 BufferedReader reader;
	 Bank bank;
	 ClientReceiver2(Socket socket, Bank bank){
		 this.socket=socket;
		 this.bank = bank;
		 try{
	   //6. 소켓으로부터 인풋, 아웃풋 스크림 얻음
			 reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		 }catch(IOException e) {}
	 }
	 @Override
	 public void run(){
	  //7.인풋,아웃풋 이용한 통신
	  //8. 연결이 끊어질때까지 통신
		 while(reader !=null){
			 try{
				 String msg = reader.readLine();//read from server
				 bank.receiveData(msg);//send to bank
				 System.out.println(msg);
			 }catch(IOException e){}
		 }
		 try{ //소켓 close()
			 socket.close();
		 }catch(IOException e) {e.printStackTrace(); }
	 }
}
