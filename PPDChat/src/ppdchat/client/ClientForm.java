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
    
    public ClientForm(String nome){
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

    public MainGameController getMain() {
        return main;
    }

    public void setMain(MainGameController main) {
        this.main = main;
    }
    
    
    
    public void enviarTextoMensagem(String nome, String texto){
        Platform.runLater(() -> {
            main.getChatToolbarController().mostrarTextoMensagem(nome, texto);
        });
        
    }
    
    
    /*
    public void writeSomething(String texto){
        System.out.println("Mensagem recebida da Thread: " + texto);
    }
    */
}
