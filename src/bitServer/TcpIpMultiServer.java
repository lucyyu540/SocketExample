package bitServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class TcpIpMultiServer {
	 public static final int PORT = 4000;//14.32.18.42
	 public static HashMap<Socket, PrintWriter>clients = new HashMap<Socket, PrintWriter>();
	 ServerSocket serverSocket =null;//대기 socket
	 /*RUN*/
	 public void run() {
		 try{
			 initWaitSocket();/*1. 대기 socket*/
			 while(true){
				 Socket socket = serverSocket.accept();//2. connect to clients & 통신소켓 생성 
				 System.out.println(socket.getInetAddress()+":"+socket.getPort()+"에서 접속했당");
				 ServerReceiver thread = new ServerReceiver(socket, clients);//3. thread
				 thread.start(); //4. run threads
			 }//runs until error --> exception error
		 } catch(Exception e){ System.out.println("server run exception: "+e.getMessage()); } 
	 }
	 /*HELPER*/
	 private void initWaitSocket() throws IOException {
		serverSocket = new ServerSocket(PORT);//socket create, bind, listen
		System.out.println("서버시작,접속 기다림");
	 }
}
class ServerReceiver extends Thread{
	private Socket socket; //통신 소켓 (클라이언트와 연결)
	HashMap<Socket, PrintWriter> clients;
	BufferedReader reader; //클라이언트의input을 읽을수 잇는 객체 
	PrintWriter writer; 
	/*CONSTRUCTOR*/
	public ServerReceiver(Socket socket, HashMap<Socket, PrintWriter> clients) {
		this.socket = socket;
		this.clients = clients;
		try{
			  reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			  writer = new PrintWriter(socket.getOutputStream());
		}catch(Exception e){}
	}
	//send ack message to all clients
	void sendToAll(String msg){
		Iterator<Socket> itr = clients.keySet().iterator();
		while(itr.hasNext()) {
			PrintWriter writer = clients.get(itr.next());
			writer.println(msg);
			writer.flush();
		}
	}
	//send ack message to client
	void sendMessage(String msg) {
		writer.println(msg);
		writer.flush();
		System.out.println("[SENDING TO CLIENT]: " + msg);
	}
	
	//receive message from client --> server manager
	@Override 
	public void run(){// thread function
		try{
			while(reader!=null) {//run
				String msg = reader.readLine();
				System.out.println("[received from client] " +msg);
				String data = Manager.getInstance().receiveData(msg);//relay message to manager
				sendMessage(data);//send ACK message
			}
		  }
		catch(Exception e){e.printStackTrace();}
		finally {//socket close
			clients.remove(socket);
			System.out.println(socket.getInetAddress()+":"+socket.getPort()+"에서 접속 종료함");
			System.out.println("현재 서버접속자수"+clients.size());
			try{//9 소켓 닫음.
				socket.close();
			} catch(Exception e){e.printStackTrace();}
		}
	}

}
