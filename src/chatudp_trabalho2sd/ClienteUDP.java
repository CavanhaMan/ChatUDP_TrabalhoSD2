package chatudp_trabalho2sd;
/*--------------------*
 * Rodrigo CavanhaMan *
 *--------------------*/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
//import java.net.Socket;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteUDP extends javax.swing.JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;

    private final DatagramSocket socket;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private JTextField txtIP;
    private JTextField txtNome;
    
    static String envio, textoMensagem;
    static InetAddress destino = InetAddress.getByName("localhost");

    public ClienteUDP() throws IOException{  
        DatagramSocket socket = new DatagramSocket();
        this.socket = socket;
        
        JLabel lblMessage = new JLabel("Verificar!");
        txtIP = new JTextField("localhost");
        txtNome = new JTextField("Cliente");
        Object[] texts = {lblMessage, txtIP, txtNome };
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        texto = new JTextArea(10,20);
        texto.setEditable(false);
        texto.setBackground(new Color(240,240,240));
        txtMsg = new JTextField(20);
        lblHistorico = new JLabel("Histórico");
        lblMsg = new JLabel("Mensagem");
        btnSend = new JButton("Enviar");
        btnSend.setToolTipText("Enviar Mensagem");
        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Sair do Chat");
        btnSend.addActionListener(this);
        btnSair.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);  
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnSair);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(txtNome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }    

    public static void main(String[] args) {
        try {
            ClienteUDP app = new ClienteUDP();

            DatagramSocket s = new DatagramSocket();
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Digite seu nome para entrar: ");
            String nome = teclado.readLine();
            System.out.println("Bem vindo(a) " + nome);

            envio = nome + " entrou!";

            //Thread t = new ClienteUDP(s);
            //t.start();

            while (!envio.equalsIgnoreCase("")) {
                byte[] buffer = envio.getBytes();
                DatagramPacket msg = new DatagramPacket(buffer, buffer.length, destino, 4545);
                s.send(msg);
                textoMensagem = teclado.readLine();

                if (!textoMensagem.equals("")) {
                    envio = nome + " disse: " + textoMensagem;
                } else {
                    envio = textoMensagem;
                }
            }

            envio = nome + " saiu do chat!";
            byte[] buffer = envio.getBytes();
            DatagramPacket msg = new DatagramPacket(buffer, buffer.length, destino, 4545);
            s.send(msg);
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(ClienteUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarMensagem(String text) {
        byte[] buffer = envio.getBytes();
        DatagramPacket msg = new DatagramPacket(buffer, buffer.length, destino, 4545);
        s.send(msg);
        textoMensagem = teclado.readLine();
        envio = nome + " disse: " + textoMensagem;
    }
    
    public void run() {
        try {
            while (true) {
                DatagramPacket resposta = new DatagramPacket(new byte[1024], 1024);
                socket.receive(resposta);

                System.out.println(new String(resposta.getData()));
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getActionCommand().equals(btnSend.getActionCommand()))
                enviarMensagem(txtMsg.getText());
            else
                if(e.getActionCommand().equals(btnSair.getActionCommand()))
                    sair();
        } catch (IOException e1) {e1.printStackTrace();}                       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                enviarMensagem(txtMsg.getText());
            } catch (IOException e1) {e1.printStackTrace();}                                                          
        }                       
    }

    @Override
    public void keyTyped(KeyEvent e) {    }
    @Override
    public void keyReleased(KeyEvent e) {    }

}
