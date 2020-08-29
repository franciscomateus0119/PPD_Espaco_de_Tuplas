/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client;
import java.util.HashMap;
import java.util.Map;
import net.jini.space.JavaSpace;
import ppdchat.client.ClientForm;
import ppdchat.client.Message;
/**
 *
 * @author Matheus
 */
public class ReadMessageThread implements Runnable {
    ClientForm meuClient;
    String meuNome;
    Map<String, ClientForm> clientsbyname = new HashMap<>();
    JavaSpace space;
    
    public ReadMessageThread(JavaSpace spaceNew, ClientForm client, String nome){
        this.space = spaceNew;
        this.meuClient = client;
        this.meuNome = nome;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            //Lookup finder = new Lookup(JavaSpace.class);
            //JavaSpace space = (JavaSpace) finder.getService();
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
            System.out.println(space);

            while (true) {
                Message template = new Message();
                Message msg = (Message) space.read(template, null, 60 * 1000);
                if (msg == null) {
                    System.out.println("Tempo de espera esgotado. Encerrando...");
                    System.exit(0);
                }
                if(msg.destination.equals("Cliente")){
                    switch (msg.type) {
                        case "Mensagem":
                            System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content);
                            if(this.meuClient!=clientsbyname.get(msg.name)){
                                clientsbyname.get(msg.name).enviarTextoMensagem(msg.name + ": ", msg.content);
                            }
                            
                            break;
                        case "ChatSelect":
                            System.out.println("Usuário " + msg.name + " se conectou ao chat " + msg.chatname);
                            break;
                        case "Client":
                            System.out.println("Novo Cliente: " + msg.clientForm);
                            clientsbyname.put(msg.name, msg.clientForm);
                            break;
                        default:
                            break;
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}