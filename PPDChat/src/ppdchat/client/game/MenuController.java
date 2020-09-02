/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client.game;

import ppdchat.client.Client;
import ppdchat.PPDChat;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import net.jini.space.JavaSpace;
import ppdchat.client.game.Lookup;
import ppdchat.utils.*;



/**
 * FXML Controller class
 *
 * @author Matheus
 */
public class MenuController {
    //private Client client;
    private boolean jogoIniciado;
    BackgroundImage startimg = new BackgroundImage( new Image( getClass().getResource("conteudo/start.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    Background startbg = new Background(startimg);
    String nome;
    public Random random;
    public int numero;
    public ArrayList<String> getnames = new ArrayList<>();
    
    @FXML TextField TF_NOME;
    
    @FXML private Button buttonConnect;
    @FXML private Button BUTTON_VERIFICAR;
    
    private JavaSpace space = null;
    private Lookup finder;
    Message temp;
    Message mensagem = null;
    //private Client clientInstance;
    private Client client;

    @FXML
    public void initialize() {
        buttonConnect.setBackground(startbg);
        temp = new Message();
        temp.destino = "Espaco";
        temp.type = "ListaUsuarios"; 
    }
    
    @FXML
    public void verificarnome(MouseEvent event){
        if (space == null) {
            System.out.println("Procurando pelo servico JavaSpace...");
            finder = new Lookup(JavaSpace.class);
            space = (JavaSpace) finder.getService();
            if (space != null) {
                System.out.println("Encontrado: " + space);
            }
            try {
                mensagem = (Message) space.read(temp, null, 300 * 1000);
            } catch (Exception e) {

            }
        }
        BUTTON_VERIFICAR.setDisable(true);
        BUTTON_VERIFICAR.setOpacity(0.5);
        try {
            if (TF_NOME.getText() != null && !TF_NOME.getText().equals("")) {
                nome = TF_NOME.getText();
                if (mensagem != null) {
                    System.out.println("Lista de nomes encontrada! Tamanho:  " + mensagem.namesList.size());
                    getnames = mensagem.namesList;
                    System.out.println("Tamanho de getname: " + getnames.size());
                    if(mensagem.namesList.size()>=1){
                        for(int y=0;y<mensagem.namesList.size();y++){
                            System.out.println("(Space)Nome " + y + " : " + mensagem.namesList.get(y));
                            System.out.println("(Space)Contém nome? " + mensagem.namesList.get(y).equals(TF_NOME.getText()));
                        }
                        for(int y=0;y<getnames.size();y++){
                            System.out.println("(Get)Nome " + y + " : " + mensagem.namesList.get(y));
                            System.out.println("(Get)Contém nome? " + getnames.get(y).equals(TF_NOME.getText()));
                        }
                    }
                    System.out.println("(Space)Contém nome? " + mensagem.namesList.contains(TF_NOME.getText()));
                    System.out.println("(Get)Contém nome? " + getnames.contains(TF_NOME.getText()));
                    if (mensagem.namesList.contains(TF_NOME.getText())) {
                        TF_NOME.setPromptText("Nome já existe!");
                        System.out.println("Nome já existe!");
                        BUTTON_VERIFICAR.setDisable(false);
                        BUTTON_VERIFICAR.setOpacity(1);
                    } else {
                        System.out.println("Nome disponível!");
                        TF_NOME.setDisable(true);
                        TF_NOME.setOpacity(0.5);
                        buttonConnect.setDisable(false);
                        buttonConnect.setOpacity(1.0);
                    }
                }
                else{
                    mensagem = (Message) space.read(temp, null, 300 * 1000);
                }

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void connect(ActionEvent event){
        try{
            client = new Client(nome, space);
            System.out.println("Setting MenuController");
            client.setMenuController(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public void gameStartReady(){
        Map<String, Object> data = new HashMap<>();
        //data.put("client", clientInstance);
        data.put("client", client);
        jogoIniciado = true;
        PPDChat.changeScreen("game", data);
        
    }
    
    
    public void getText(){
        Platform.runLater(() -> setNome(TF_NOME.getText()));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
}
