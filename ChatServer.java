import java.net.*;
import java.io.*;

public class ChatServer implements Runnable{
	private ChatServerThread clients[] = new ChatServerThread[10];
	private ServerSocket server = null;
	private Thread thread = null;
	private int ClientCount = 0;


	public ChatServer (int port){
		try{
			System.out.println("Escuchando en el puerto " + port + ", espero por favor ...\n");
			server = new ServerSocket(port);
			System.out.println("Server started: " + server);
			start();
		}
		catch(IOException ioe){
			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
		}
	}

	public void run(){
		while(thread != null){
			try{
				System.out.println("Esperando un cliente ...");
				addThread(server.accept());
			}
			catch(IOException ioe){
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	public void start(){  
   		if (thread == null){  
   			thread = new Thread(this); 
        	thread.start();
      	}
   	}

	public void stop(){
		if (thread != null){  
			thread.stop(); 
		thread = null;
		}
	}

	private int findClient(int ID){
		for(int i=0; i<ClientCount; i++){
			if(clients[i].getID() == ID)
				return i;
		}
		return -1;
	}

	public synchronized void handle(int ID, String input){
		if(input.equals(".hasta luego")){
			clients[findClient(ID)].send(".hasta luego");
			remove(ID);
		}
		else{
			for(int i=0; i<ClientCount; i++){
				clients[i].send(ID + ": " + input);
			}
		}
	}

	//Desconexion de un cliente. 
	public synchronized void remove(int ID){
		int pos = findClient(ID);
		//Ajuste de vector de usuairos conectados
		if(pos >=0 ){
			ChatServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);

			if(pos < ClientCount -1){
				for(int i = pos+1; i<ClientCount; i++){
					clients[i-1] = clients[i];
				}
			}
			ClientCount = ClientCount -1;
			try{
				toTerminate.close();
			}
			catch(IOException ioe){
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket){
		if(ClientCount < clients.length){
			System.out.println("Client aceptado: " + socket);
			clients[ClientCount] = new ChatServerThread(this, socket);

			try{
				clients[ClientCount].open();
				clients[ClientCount].start();
				ClientCount++;
			}
			catch(IOException ioe){
				System.out.println("Error hebra: " + ioe);
			}
		}
		else{
			System.out.println("Client rechazado: maximo " + clients.length + " alcazado.");
		}

	}

	//MAIN
	public static void main(String args[]){
		
		ChatServer server = null;
			if (args.length != 1)
				System.out.println("Uso: java ChatServer port");
			else
				server = new ChatServer(Integer.parseInt(args[0]));
	}

}
