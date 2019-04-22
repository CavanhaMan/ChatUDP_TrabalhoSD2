package chatudp_trabalho2sd;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public class Mensagem implements Serializable{
    
    private String nomeDoCliente;
    private String textoDaMensagem;
    private Acao acaoDoCliente;
    
    public enum Acao{CONECTAR, DESCONECTAR, ENVIAR}

    public String getNomeDoCliente() {return nomeDoCliente;}
    public void setNomeDoCliente(String nomeDoCliente) {this.nomeDoCliente = nomeDoCliente;}
    public String getTextoDaMensagem() {return textoDaMensagem;}
    public void setTextoDaMensagem(String textoDaMensagem) {this.textoDaMensagem = textoDaMensagem;}
    public Acao getAcaoDoCliente() {return acaoDoCliente;}
    public void setAcaoDoCliente(Acao acaoDoCliente) {this.acaoDoCliente = acaoDoCliente;}    
}
