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
    String nomeagora = "Anonimo";
    String chatAtual;
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
                template.destino = meuNome;
                template.destinatarioLeu = false;
                Message msg = (Message) space.take(template, null, 300 * 1000);
                if (msg == null) {
                    System.out.println("Tempo de espera esgotado. Encerrando...");
                    System.exit(0);
                } else { //Se a Mensagem for Direcionada para Clientes e a Mensagem não é nula
                    switch (msg.type) {
                        case "Mensagem":
                            //Se Quem enviou a mensagem fui eu-> Lê a mensagem, mostra na GUI e coloca de volta pro JavaSpace
                            //if (msg.name.equals(meuNome)) {
                            System.out.println("Eu já li isso? " + msg.destinatarioLeu);
                            System.out.println("Mensagem recebida de " + msg.name + "(" + msg.chatname + ": " + msg.content);
                            Platform.runLater(() -> {
                                main.getChatToolbarController().mostrarTextoMensagem(msg.name, msg.chatname, msg.content);
                            });
                            msg.destinatarioLeu = true;
                            System.out.println("Mandando a mensagem de volta pro Space!");
                            space.write(msg, null, 60 * 1000);

                            break;
                        case "NewChat":
                            System.out.println(msg.name + "Criou uma nova sala: " + msg.chatname);
                            chatAtual = msg.chatname;
                            Platform.runLater(() -> {
                                main.getGameController().adicionarSala(msg.chatname);

                            });
                        case "Anonimo":
                            if(nomeagora.equals("Anonimo")){
                                System.out.println("(Anonimo CASE)Nome já existe. Novo nome: " + msg.name);
                                Platform.runLater(() -> {
                                    main.getClient().setNome(msg.name);
                                    main.getGameController().setNome(msg.name);
                                });
                            }
                            
                        case "NewName":
                            //Nome Ja Existe
                            if (msg.content == null) {
                                System.out.println("(NewName CASE)O nome escolhido já existe. Escolha outro nome!");
                                Platform.runLater(() -> {
                                    main.getGameController().noNewName();
                                    
                                });
                                //Novo Nome
                            } else {
                                Platform.runLater(() -> {
                                    this.meuNome = msg.content;
                                    this.nomeagora = null;
                                    main.getClient().setNome(msg.content);
                                    main.getGameController().setNome(msg.content);
                                });
                                System.out.println("Troca de nome Efetuada. Novo nome: " + msg.content);
                            }

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
