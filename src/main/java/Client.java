import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	private Consumer<Serializable> serverClients;
	private Consumer<Serializable> removeClients;
	
	Client(Consumer<Serializable> call, Consumer<Serializable> call2, Consumer<Serializable> call3){
		removeClients = call3;
		serverClients = call2;
		callback = call;
	}
	
	public void run() {

		String newCStr = "new client on server: " + "client #";
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
			messageObject data = (messageObject) in.readObject();
			if(data.sender != -1) {
				callback.accept("Client #" + data.sender + " said: " + data.message);
			} else {
				callback.accept(data.message);
			}

			if(data.message.contains(newCStr)) {
				String cNum = data.message.substring(data.message.length() - 1, data.message.length());
				serverClients.accept("Client #" + cNum);
			}

			if(data.message.contains("has left the server!")) {
				String cToRemove = data.message.substring(0, 10);
				removeClients.accept(cToRemove);
			}
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(messageObject data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
