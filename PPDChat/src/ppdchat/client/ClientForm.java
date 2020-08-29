package ppdchat.client;

import java.util.Scanner;
import net.jini.space.JavaSpace;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.jini.core.transaction.TransactionException;
import ppdchat.client.game.MainGameController;

public class ClientForm implements java.io.Serializable{
    String nome;
    String chat;
    JavaSpace space;
    MainGameController main;
    public ArrayList<String> chatlist;
    
    public ClientForm(JavaSpace space, String nome, MainGameController main){
        this.space = space;
        this.nome = nome;
        this.main = main;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public ArrayList<String> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<String> chatlist) {
        this.chatlist = chatlist;
    }
    
    public void enviarTextoMensagem(String nome, String texto){
        Platform.runLater(() -> {
            main.getChatToolbarController().mostrarTextoMensagem(nome, texto);
        });
        
    }
    
    public void writeMessage(Message msg){
        try {
            Platform.runLater(() -> {
                try {
                    System.out.println(space);
                    space.write(msg, null, 60 * 1000);
                } catch (TransactionException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    public void writeSomething(String texto){
        System.out.println("Mensagem recebida da Thread: " + texto);
    }
    */
}
