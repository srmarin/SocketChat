

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorChat{
  public static void main(String[]args){
    int puerto = 8989;
    int maximoConexiones = 10;
    ServerSocket servidor = null;
    Socket socket = null;
    MensajesChat mensajes = new MensajesChat();

    try{
      servidor = new ServerSocket(puerto, maximoConexiones);

      while(true){
        System.out.println("servidor a la espera de conexiones");
        socket = servidor.accept();
        System.out.println("cliente con la ip "+socket.getInetAddress().getHostName() + " conectado");
        ConexionCliente cc = new ConexionCliente(socket, mensajes);
        cc.start();
      }
    }catch(IOException ex){
      System.out.println("Error: "+ex.getMessage());
    }finally{
      try{
        socket.close();
        servidor.close();
      }catch(IOException ex){
        System.out.println("Error al cerrar el server: "+ex.getMessage());
      }
    }
  }
}
