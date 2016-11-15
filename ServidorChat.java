package servidorchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ServidorChat{
  public static void main(String[]args){
    PropertyConfigurator.configure("log4j.properties");
    Logger log = Logger.getLogger(ServidorChat.class);
    int puerto = 8989;
    int maximoConexiones = 10;
    ServerSocket servidor = null;
    Socket socket = null;
    MensajesChat mensajes = new MensajesChat();

    try{
      servidor = new ServerSocket(puerto, maximoConexiones);

      while(true){
        log.info("servidor a la espera de conexiones");
        socket = servidor.accept();
        log.info("cliente con la ip "+socket.getInetAddress().getHostName() + " conectado");
        ConexionCliente cc = new ConexionCliente(socket, mensajes);
        cc.start();
      }
    }catch(IOException ex){
      log.error("Error: "+ex.getMessage());
    }finally{
      try{
        socket.close();
        servidor.close();
      }catch(IOException ex){
        log.error("Error al cerrar el server: "+ex.getMessage());
      }
    }
  }
}
