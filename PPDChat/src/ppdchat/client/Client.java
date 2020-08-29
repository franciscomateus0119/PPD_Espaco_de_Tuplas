/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client;
import java.rmi.RemoteException;
import ppdchat.client.game.MainGameController;
import ppdchat.client.game.MenuController;
import ppdchat.client.Lookup;
import ppdchat.client.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;


/**
 *
 * @author Matheus
 */
public class Client{
    //private ServerInterface server;
    private MainGameController mainController;
    private MenuController menuController;
    private static Client instance;
    private JavaSpace space;
    private Lookup finder;
    private String nome;
    private int nameCounter = 0;

    public Client() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            finder = new Lookup(JavaSpace.class);
            space = (JavaSpace) finder.getService();
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
        } catch (Exception e) {
            System.out.println("Não foi possível encontrar o espaço!");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public static Client getInstance() {
        if(instance == null){
            instance = new Client();
        }
        return instance;
    }
   
    public void writeClient(){
        Platform.runLater(() -> {
            this.writeNewClient(this, nome);
            System.out.println("Client sent to TupleSpace!");
            //mainController.getGameController().setNome(nome);
        });
        
    }
    
    public void setMenuController(MenuController menucontroller){
        menuController = menucontroller;
        System.out.println("Set MenuController");
        Platform.runLater(() -> this.menuController.gameStartReady());
    }

    public void setGameController(MainGameController mainController) {
        this.mainController = mainController;
        System.out.println("GAMECONTROLLER set!");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        System.out.println("Meu nome é: " + nome);
    }

    public JavaSpace getSpace() {
        return space;
    }
    
    /*
     public void setSpacehandler(SpaceHandler spacehandler) {
        this.spacehandler = spacehandler;
    }
    public SpaceHandler getSpacehandler() {
        return spacehandler;
    }
    */
    public void enviarTextoMensagem(String nome, String texto){
        Platform.runLater(() -> {
            mainController.getChatToolbarController().mostrarTextoMensagem(nome, texto);
        });
        
    }
    
    public void writeMessage(String name, String message){
        try{
            Message msg = new Message();
            msg.type = "Mensagem";
            msg.name = name;
            msg.content = message;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                } catch (TransactionException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void writeChatSelect(String chatname, String name){
        try{
            Message msg = new Message();
            msg.type = "ChatSelect";
            msg.chatname = chatname;
            msg.name = name;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                } catch (TransactionException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void writeNewClient(Client client, String name){
        try {
            Message msg = new Message();
            msg.type = "NewClient";
            msg.client = client;
            msg.name = name;
            Platform.runLater(() -> {
                try {
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
    
    public void readMessage(){
        try {
            Message template = new Message();
            Message msg = (Message) space.take(template, null, 60 * 1000);
            if (msg == null) {
                System.out.println("Tempo de espera esgotado. Encerrando...");
                System.exit(0);
            }
            if (msg.type.equals("Mensagem")) {
                System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content);
            } else if (msg.type.equals("ChatSelect")) {
                System.out.println("Usuário " + msg.name + " se conectou ao chat " + msg.chatname);
            }
            
            System.out.println("Mensagem recebida: " + msg.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Old Project">

    /*
    @Override
    public void enviarStart() throws RemoteException{
        server.receberStart();
    }
    
    @Override
    public void receberStart(){
        System.out.println("Iniciando Partida!");
        Platform.runLater(() -> this.menuController.gameStartReady());
    }
    
    @Override
    public void enviarTexto(String texto) throws RemoteException{
        System.out.println("Você Digitou: "+ texto);
        server.broadcastTexto(this, texto);
        Platform.runLater(() ->(mainController.getChatToolbarController().mostrarTexto(texto)));
    }
    
    @Override
    public void receberTexto(String texto){
        System.out.println("Mensagem recebida do servidor: " + texto);
        Platform.runLater(() -> (mainController.getChatToolbarController().mostrarTexto(texto)));
    }
    

    
    @Override
    public void enviarStatus(String id, String status) throws RemoteException{
        server.broadcastStatus(this, id, status);
    }
    
    @Override
    public void receberStatus(String id, String status){

        Platform.runLater(() -> mainController.getGameController().statusPessoa(id, status));
    }
    

    @Override
    public void receberChat(String chatmsg){
        Platform.runLater(() -> mainController.getChatToolbarController().selectChatBox(chatmsg));
    }
    
    @Override
    public void enviarNick(String nick) throws RemoteException{
        server.broadcastNick(this, nick);
    }
    
    @Override
    public void receberNick(String nick) {
        //Setando o nick do cliente. Sempre será o primeiro nick recebido
        System.out.println(names.size());
        if (names.size() == 0) {
            this.names.put(names.size(), nick);
            mainController.getGameController().setMeuNome(nick);
            Platform.runLater(() -> {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        //Setando os próximos 3 contatos
        else if(names.size()!= 4){
            this.names.put(names.size(), nick);
            Platform.runLater(() -> {
                try {

                    mainController.getChatToolbarController().setContato(nick);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
                
        }
        
    }
    
    @Override
    public void receberConfig(String tipo) throws RemoteException{
        if (tipo.matches("A")) {
            System.out.println("(A)Meu nome é: " + this.nome);
            Platform.runLater(() -> {
                mainController.getGameController().setPessoa("A");
            });
            System.out.println("(A)Meu nome é: " + this.nome);
            enviarNick(this.nome);
        } else if (tipo.matches("B")) {
            System.out.println("(B)Meu nome é: " + this.nome);
            Platform.runLater(() -> {
                mainController.getGameController().setPessoa("B");
            });

            System.out.println("(B)Meu nome é: " + this.nome);
            enviarNick(this.nome);
        } else if (tipo.matches("C")) {
            System.out.println("(C)Meu nome é: " + this.nome);
            Platform.runLater(() -> {
                mainController.getGameController().setPessoa("C");
            });

            enviarNick(this.nome);
        } else if (tipo.matches("D")) {
            System.out.println("(D)Meu nome é: " + this.nome);
            Platform.runLater(() -> {
                mainController.getGameController().setPessoa("D");
            });

            enviarNick(this.nome);
        }
        
    }
    */
    //</editor-fold>

    

}
