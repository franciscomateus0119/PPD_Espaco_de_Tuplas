/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client;
import ppdchat.client.game.MainGameController;
import ppdchat.client.game.MenuController;
import ppdchat.utils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Matheus
 */
public class Client{
    //private ServerInterface server;
    private MainGameController mainController;
    private MenuController menuController;
    private String nome;
    private SpaceHandler spacehandler;
    Map<Integer, String> names = new HashMap<>();
    private int nameCounter = 0;
    
    
    
    public Client(String nome){
        super();
        //this.spacehandler = SpaceHandler.getInstance();
        this.nome = nome;
        
        
    }

   

    
    
    public void writeClient(){
        SpaceHandler.getInstance().writeNewClient(this, nome);
        System.out.println("Client sent to TupleSpace!");
    }
    
    public void setMenuController(MenuController menucontroller){
        menuController = menucontroller;
        System.out.println("Set MenuController");
        Platform.runLater(() -> this.menuController.gameStartReady());
    }

    public void setGameController(MainGameController mainController) {
        this.mainController = mainController;
        System.out.println("GAMECONTROLLER set!");
        //System.out.println("Set MainController");
        //System.out.println("Setting SpaceHandler");
        //mainController.getClient().setSpacehandler(mainController.getSpacehandler());
        //System.out.println("SpaceHandler SET");
        //mainController.getGameController().setSpacehandler(spacehandler);
        //mainController.getClient().getSpacehandler().writeNewClient(this, nome);
        mainController.getSpacehandler().getInstance().writeNewClient(this, nome);
        System.out.println("Client sent to TupleSpace!");
        
        //writeClient();
    }

    public String getNome() {
        return nome;
    }
    
     public void setSpacehandler(SpaceHandler spacehandler) {
        this.spacehandler = spacehandler;
    }
    public SpaceHandler getSpacehandler() {
        return spacehandler;
    }
    
    public void enviarTextoMensagem(String nome, String texto){
        Platform.runLater(() -> {
            mainController.getChatToolbarController().mostrarTextoMensagem(nome, texto);
        });
        
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
