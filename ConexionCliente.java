import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConexionCliente extends Thread implements Observer{
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
      System.out.println("Error al crear los stream de entrada y salida");
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
        System.out.println("cliente con la ip "+socket.getInetAddress().getHostName()+ " desconectado");

        try{
          entradaDatos.close();
          salidaDatos.close();
        }catch(IOException ex2){
          System.out.println("Error al cerrar los stream de entrada y salida de datos: "+ex2.getMessage());
        }
      }
    }
  }

  @Override
  public void update(Observable o,Object arg){
    try{
      salidaDatos.writeUTF(arg.toString());
    }catch(IOException ex){
      System.out.println("error al enviar mensaje al cliente: "+ex.getMessage());
    }
  }
}
