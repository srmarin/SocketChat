package servidorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

public class ConexionCliente extends Thread implements Observer{
  private Logger log = Logger.getLogger(ConexionCliente.class);
  private Socket socket;
  private MensajesChat mensajes;
  private DataInputStream entradaDatos;
  private DataOutputStream salidaDatos;

  public ConexionCliente(Socket socket, MensajesChat mensajes){
    this.socket = socket;
    this.mensajes = mensajes;

    try{
      entradaDatos =new DataInputStream(socket.getInputStream());
      salidaDatos = new DataOutputStream(socket.getOutputStream());
    }catch(IOException ex){
      log.error("Error al crear los stream de entrada y salida");
    }
  }

  @Override
  public void run(){
    String mensajeRecibido;
    boolean conectado = true;
    mensajes.addObserver(this);
    while(conectado){
      try{
        mensajeRecibido = entradaDatos.readUTF();
        mensajes.setMensaje(mensajeRecibido);
      }catch(IOException ex){
        log.info("cliente con la ip "+socket.getInetAddress().getHostName()+ " desconectado");

        try{
          entradaDatos.close();
          salidaDatos.close();
        }catch(IOException ex2){
          log.error("Error al cerrar los stream de entrada y salida de datos: "+ex2.getMessage());
        }
      }
    }
  }

  @Override
  public void update(Observable o,Object arg){
    try{
      salidaDatos.writeUTF(arg.toString());
    }catch(IOException e){
      log.error("error al enviar mensaje al cliente: "+ex.getMessage());
    }
  }
}
