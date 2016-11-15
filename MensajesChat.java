import java.util.Observable;

public class MensajesChat extends Observable{
  private String mensaje;

  public MensajesChat(){}

  public String getMensaje(){
    return this.mensaje;
  }

  public void setMensaje(String mensaje){
    this.mensaje = mensaje;
    this.setChanged();
    this.notifyObservers(this.getMensaje());
  }
}
