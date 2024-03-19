import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updateClients(messageObject message) {
				synchronized (server) {
					for (int i = 0; i < clients.size(); i++) {
						ClientThread t = clients.get(i);
						try {
							t.out.writeObject(message);
						} catch (Exception e) {
						}
					}
				}
			}

			public void sendClientsList() {
				synchronized (server) {
					ClientThread c = clients.get(clients.size() - 1);
					for (int i = 0; i < clients.size() - 1; i++) {
						messageObject msg = new messageObject();
						try {
							msg.message = "new client on server: client #" + clients.get(i).count;
							c.out.writeObject(msg);
						} catch (Exception e) {
						}
					}
				}
			}

			public void groupMessage(messageObject msg) {
				synchronized (server) {
					for (int i = 0; i < clients.size(); i++) {
						if (!msg.clients.contains(clients.get(i).count)) continue;
						msg.sender = count;
						ClientThread c = clients.get(i);
						try {
							c.out.writeObject(msg);
						} catch (Exception e) {
						}
					}
				}
			}

			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}

				messageObject tmp = new messageObject();
				tmp.message = "new client on server: client #"+count;
				updateClients(tmp);
				sendClientsList();
					
				 while(true) {
					    try {
					    	messageObject data = (messageObject) in.readObject();
					    	callback.accept("client: " + count + " sent: " + data.message);
							data.sender = count;
					    	//updateClients(data);
							groupMessage(data);
					    	
					    	}
					    catch(Exception e) {
							messageObject tmp2 = new messageObject();
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
							tmp2.message = "Client #"+count+" has left the server!";
							tmp2.sender = -1;
					    	updateClients(tmp2);
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	
