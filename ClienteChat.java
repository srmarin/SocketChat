package clientechat;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;

public class ClienteChat extends JFrame{
  private JTextArea mensajesChat;
  private Socket socket;

  private int puerto;
  private String host;
  private String usuario;

  public ClienteChat(){
    super("Cliente Chat");

    mensajesChat = new JTextArea();
    mensajesChat.setEnabled(false);
    mensajesChat.setLineWrap(true);
    mensajesChat.setWrapStyleWord(true);
    JScrollPane scrollMensajesChat =new JScrollPane(mensajesChat);
    JTextField tfMensaje = new JTextField("");
    JButton btEnviar = new JButton("Enviar");

    Container c = this.getContentPane();
    c.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.insets = new Insets(20,20,20,20);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(scrollMensajesChat, gbc);
        // Restaura valores por defecto
        gbc.gridwidth = 1;
        gbc.weighty = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 20, 20);

        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(tfMensaje, gbc);
        // Restaura valores por defecto
        gbc.weightx = 0;

        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(btEnviar, gbc);

        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ventana de configuracion inicial
        VentanaConfiguracion vc = new VentanaConfiguracion(this);
        host = vc.getHost();
        puerto = vc.getPuerto();
        usuario = vc.getUsuario();

        log.info("Quieres conectarte a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");

        // Se crea el socket para conectar con el Sevidor del Chat
        try {
            socket = new Socket(host, puerto);
        } catch (UnknownHostException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        btEnviar.addActionListener(new ConexionServidor(socket, tfMensaje, usuario));

    }

    public void recibirMensajesServidor(){
      DataInputStream entradaDatos = null;
      String mensaje ;
      try{
        entradaDatos = new DataInputStream(socket.getInputStream());
      }catch(IOException ex){
        System.err.println("Error al crear el stream deentrada");
      }catch(NullPointerException ex){
        System.error.println("El socket no se creo correctamente");
      }

      boolean conectado =true;
      while(conectado){
        try{
          mensaje = entradaDatos.readUTF();
          mensajesChat.append(mensaje + System.lineSeparator());
        }catch(IOException ex){
          System.err.println("Error al ler del stream de entrada");
          conectado = false;
        }catch(NullPointerException ex){
          System.err.println("El socket no se creo correctamente");
          conectado = false;
        }
      }
    }
    public static void main(String[] args){
      ClienteChat c = new ClienteChat();
      c.recibirMensajesServidor();
    }
  }
