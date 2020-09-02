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
//import ppdchat.client.Message;
import ppdchat.utils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
//import ppdchat.client.ClientForm;

/**
 *
 * @author Matheus
 */
public class Client{
    //private ServerInterface server;
    private MainGameController mainController;
    private MenuController menuController;
    //private ClientForm clientForm;
    private JavaSpace space;
    private Lookup finder;
    private String nome;
    private int nameCounter = 0;
    Random rand;
    int n;
    
    Runnable runnable;
    Thread thread;

    public Client(String nome, JavaSpace space) {
        this.nome = nome;
        this.space = space;
        /*
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            
            finder = new Lookup(JavaSpace.class);
            space = (JavaSpace) finder.getService();
            
            
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
            //writeUserListServer();
        } catch (Exception e) {
            System.out.println("Não foi possível encontrar o espaço!");
            e.printStackTrace();
            System.exit(-1);
        }
        */
    }

    public void startThread(){
        Runnable runnable = new ReadMessageThread(space, nome, mainController);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setMenuController(MenuController menucontroller){
        menuController = menucontroller;
        System.out.println("Set MenuController");
        Platform.runLater(() -> this.menuController.gameStartReady());
    }

    public void setGameController(MainGameController mainController) {
        this.mainController = mainController;
        //this.clientForm.setMain(mainController);
        System.out.println("MAINGAMECONTROLLER set!");
        Platform.runLater(() -> mainController.getGameController().setNome(this.nome));
        writeNewClient(this.nome);
        writeNewClient(this.nome);
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

    public void writeMessageToClient(String name, String message){
        try{
            Message msg = new Message();
            msg.type = "Mensagem";
            msg.destino = "Cliente";
            msg.name = name;
            msg.content = message;
            space.write(msg, null, 60 * 1000);
            System.out.println("MENSAGEM ENVIADA: " + msg.content);
                
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void writeNewClient(String name){
        try{
            Message msg = new Message();
            msg.type = "NewClient";
            msg.destino = "Servidor";
            msg.name = name;
            space.write(msg, null, 60 * 1000);
            System.out.println("Mensagem de Novo Cliente enviada!");
                
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void writeMessageToServer(String name,String chatname, String message){
        try{
            Message msg = new Message();
            msg.type = "Mensagem";
            msg.destino = "Servidor";
            msg.name = name;
            msg.chatname = chatname;
            msg.content = message;
            space.write(msg, null, 60 * 1000);
            System.out.println("MENSAGEM ENVIADA: " + msg.content);
                
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void writeNewChatToServer(String name, String chatname){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "NewChat";
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
    
    public void writeNewUserChatToServer(String name, String chatname){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "NewUserChat";
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
    
    public void writeEntrarSalaRequest(String name, String chatname, String chatAtual){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "EntrarRequest";
            msg.chatname = chatname;
            msg.content = chatAtual;
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
    
    public void writeAtualizarListaSala(String name){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "AtualizarListaSala";
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
    
    public void writeAtualizarListaUser(String name, String chatname){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "AtualizarListaUser";
            msg.name = name;
            msg.chatname = chatname;
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
    
    public void writeUserListServer(){
        try{
            Message msg = new Message();
            msg.destino = "Servidor";
            msg.type = "Lista";
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
    
    public void readUserList(int numero, Random rand){
        numero = 3;
        try{
            Message temp = new Message();
            temp.destino = "Espaco";
            temp.type = "ListaUsuarios";
            Message mensagem = (Message) space.read(temp, null, 30 * 1000);
            if(mensagem!=null){
                System.out.println("UserList Encontrada!");
                if(mensagem.namesList.contains(nome)){
                    //numero = rand.nextInt(10001);
                    System.out.println("Valor de numero: " + numero);
                    String newNome = "Anonimo" + numero;
                    while(mensagem.namesList.contains(newNome)){
                        this.n = rand.nextInt(10001);
                        newNome = "Anonimo" + numero;
                    }
                    nome = newNome;
                }
            }
            else{
                System.out.println("Mission Failed");
            }
        }catch(Exception e){e.printStackTrace();}
        
        
    }
    

  
    

}
