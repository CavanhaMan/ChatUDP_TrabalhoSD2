package chatudp_service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import chatudp_trabalho2sd.Mensagem;
import chatudp_trabalho2sd.Mensagem.Acao;

public class ServidorService {

    private ServerSocket serverSocket;
    private Socket conexao;
    int portaRemota;
    FileWriter arquivo;
    PrintWriter gravarArq;
    String arq = "";

    public ServidorService() {
        try {

            serverSocket = new ServerSocket(2000);

            while (true) {
                System.out.println("Aguardando conexão...");
                conexao = serverSocket.accept();
                System.out.println("Cliente conectou!");

                portaRemota = conexao.getPort();

                new Thread(new ListenerSocket(conexao)).start();
            }
        } catch (IOException ex) {System.out.println(ex.getMessage());}
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream saida;
        private ObjectInputStream entrada;

        public ListenerSocket(Socket s) {
            try {
                this.saida = new ObjectOutputStream(s.getOutputStream());
                this.entrada = new ObjectInputStream(s.getInputStream());
            } catch (IOException ex) {System.out.println(ex.getMessage());}
        }

        @Override
        public void run() {
            Mensagem mensagem = null;

            try {

                while ((mensagem = (Mensagem) entrada.readObject()) != null) {
                    Acao acao = mensagem.getAcaoDoCliente();

                    System.out.println(mensagem.getTextoDaMensagem());

                    switch (acao) {
                        case CONECTAR:
                            conectar(mensagem, saida);
                            break;
                        case DESCONECTAR:
                            desconectar(mensagem, saida);
                            return;
                        case ENVIAR:
                            enviar_para_todos(mensagem,saida);
                            break;
                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                Mensagem m = new Mensagem();
                m.setNomeDoCliente(mensagem.getNomeDoCliente());
                desconectar(m, saida);
                System.out.println(mensagem.getNomeDoCliente() + " deixou o chat.");
            } 
    }


    private void desconectar(Mensagem msg, ObjectOutputStream s) {
        msg.setTextoDaMensagem(" saiu do chat.");
        msg.setAcaoDoCliente(Acao.ENVIAR);

        enviar_para_todos(msg,s);
        System.out.println("Usuário " + msg.getNomeDoCliente() + " saiu da sala.");
    }

    private void conectar(Mensagem msg, ObjectOutputStream s) {
        try {
            s.writeObject(msg);
        } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: ChatUDP_TrabalhoSD2.Service.ServidorService.enviar_para_um()");}
    }
    
    private void enviar_para_todos(Mensagem msg, ObjectOutputStream s) {
        try {
            s.writeObject(msg);
        } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: ChatUDP_TrabalhoSD2.Service.ServidorService.enviar_para_um()");}
    }


}
}