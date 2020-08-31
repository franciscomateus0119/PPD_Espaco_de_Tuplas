/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import net.jini.space.JavaSpace;
import ppdchat.utils.*;
import ppdchat.client.game.MainGameController;

/**
 *
 * @author Matheus
 */
public class ReadMessageThread implements Runnable {
    String meuNome;
    JavaSpace space;
    MainGameController main;

    public ReadMessageThread(JavaSpace spaceNew, String nome, MainGameController mainController) {
        this.space = spaceNew;
        this.meuNome = nome;
        this.main = mainController;
        
    }

    @Override
    public void run() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
            System.out.println(space);
            
            while (true) {
                Message template = new Message();
                Message msg = (Message) space.take(template, null, 180 * 1000);
                if (msg == null) {
                    System.out.println("Tempo de espera esgotado. Encerrando...");
                    System.exit(0);
                }
                if (msg != null && msg.destino.equals("Cliente") && !msg.quemLeu.contains(meuNome)) {
                    switch (msg.type) {
                        case "Mensagem":
                            //Se Quem enviou a mensagem não fui eu e eu não li a mensagem ainda -> Lê a mensagem, mostra na GUI e a repassa
                            if(!msg.name.equals(meuNome) && !msg.quemLeu.contains(meuNome)){
                                System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content + ". Eu enviei esta mensagem. Ainda não li esta mensagem");
                                Platform.runLater(() -> {
                                    main.getChatToolbarController().mostrarTextoMensagem(msg.name, msg.content);
                                });
                                msg.quemLeu.add(meuNome);
                                space.write(msg, null, 60 * 1000);
                                
                            }
                            //Se eu enviei a mensagem e ainda não li minha própria mensagem --> Lê a mensagem e a repassa =
                            else if(!msg.quemLeu.contains(meuNome)){
                                System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content + ". Ainda não li esta mensagem");
                                msg.quemLeu.add(meuNome);
                                space.write(msg, null, 60 * 1000);
                            }
                            //Se eu não enviei a mensagem e já a li --> Apenas repassa a Mensagem
                            else if (!msg.quemLeu.contains(meuNome)){
                                System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content + ". Eu já li esta mensagem");
                                space.write(msg, null, 60 * 1000);
                            }
                            break;
                        case "ChatSelect":
                            System.out.println("Usuário " + msg.name + " se conectou ao chat " + msg.chatname);
                            break;
                        case "Client":
                        default:
                            break;
                    }

                }
                else if(msg != null && msg.destino.equals("Servidor") && msg.servidorLeu==false){
                    System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content + ". Esta mensagem é para o Servidor!");
                    space.write(msg, null, 60 * 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
