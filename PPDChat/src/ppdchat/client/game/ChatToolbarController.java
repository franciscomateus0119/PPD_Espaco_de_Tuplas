/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client.game;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.Random;

import java.util.HashMap;
import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.shape.Circle;
import java.rmi.RemoteException;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * FXML Controller class
 *
 * @author Matheus
 */
public class ChatToolbarController{
    private MainGameController main;
    // <editor-fold defaultstate="collapsed" desc="Old Project">

    private String selectedBOX;
    @FXML
    private TextArea TA_BOX_AB;
    @FXML
    private TextArea TA_BOX_AC;
    
    @FXML
    private TextArea TA_BOX_AD;
    @FXML
    private TextArea TA_BOX_BC;
    @FXML
    private TextArea TA_BOX_BD;
    @FXML
    private TextArea TA_BOX_CD;

    
    int nContato = 0;
    Random random = new Random();
    int randomNum = random.nextInt((100 - 1) + 1);
    String nick = "Anônimo" + randomNum +": ";
        //</editor-fold>


    public void init(MainGameController mainGameController){
        main = mainGameController; 
        
    }
    
    public void mostrarTextoMensagem(String nome, String chatname, String texto){
        String mensagem = (nome + ": " + texto);
        Platform.runLater(() -> {
            //main.getGameController().TA_BOX.appendText(mensagem);
            //main.getGameController().TA_BOX.appendText(main.getGameController().TA_BOX.getText());
            /*
            main.getGameController().textareateste.appendText(texto);
            main.getGameController().TA_BOX.clear();
            main.getGameController().TA_BOX.appendText(main.getGameController().textareateste.getText());
            */
            /*main.getGameController().chats.get(chatname).appendText(mensagem);
            main.getGameController().TA_BOX.clear();
            main.getGameController().TA_BOX.appendText(main.getGameController().chats.get(chatname).getText());
            */
            //main.getGameController().TA_BOX.appendText(mensagem + " - " + chatname);
            if(main.getGameController().chatAtual.equals(chatname)){
                
                //System.out.println("Chat Atual: " + main.getGameController().chatAtual);
                //main.getGameController().chats.get(chatname).appendText(mensagem);
                //System.out.println("Chat escolhido: " + main.getGameController().chats.get(chatname).getId());
                //main.getGameController().TA_BOX = main.getGameController().chats.get(chatname);
               //main.getGameController().TA_BOX.clear();
                //main.getGameController().TA_BOX.appendText(main.getGameController().chats.get(chatname).getText());
                //int index = main.getGameController().HBOX_SALA.getChildren().indexOf(main.getGameController().chats.get(chatname));
                main.getGameController().chats.get(chatname).appendText(mensagem);
                main.getGameController().TA_BOX.clear();
                main.getGameController().TA_BOX.appendText(main.getGameController().chats.get(chatname).getText());
                
            }
            
        });
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Old Project">

/*
    //Arrumar para nContato!=3
    public void setContato(String text)throws JMSException,NamingException{    
        if (nContato != 3) {
            System.out.println("(ANTES) nContato: " + nContato);
            main.getGameController().addContato(text, nContato);
            nContato = nContato + 1;
            System.out.println("(DEPOIS) nContato: " + nContato);
        }
        
    }
    
    
    public void mostrarTexto(String text){
        String ch = text.substring(0, 2);
        String texto = text.substring(2) + "\n";
        if(ch.matches("AB") & (main.getGameController().quemSou().matches("A") | main.getGameController().quemSou().matches("B")))
        {
            if(main.getGameController().quemSou().matches("A")){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AB.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AB.appendText(texto)    ;

                    });
                }
                    
            }
            if(main.getGameController().quemSou().matches("B")){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AB.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AB.appendText(texto)    ;

                    });
                }
                    
            }
            
        }
        if(ch.matches("AC") & (main.getGameController().quemSou().matches("A") | main.getGameController().quemSou().matches("C")))
        {
            if(main.getGameController().quemSou().matches("A") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AC.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AC.appendText(texto)    ;

                    });
                }
            }
            if(main.getGameController().quemSou().matches("C") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AC.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AC.appendText(texto)    ;

                    });
                }
            }
        }
        if(ch.matches("AD")& (main.getGameController().quemSou().matches("A") | main.getGameController().quemSou().matches("D")))
        {
            if(main.getGameController().quemSou().matches("A") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AD.appendText(texto)    ;

                    });
                }
            }
            if(main.getGameController().quemSou().matches("D") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_AD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_AD.appendText(texto)    ;

                    });
                }
            }
        }
        if(ch.matches("BC")& (main.getGameController().quemSou().matches("B") | main.getGameController().quemSou().matches("C")))
        {
            if(main.getGameController().quemSou().matches("B") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_BC.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_BC.appendText(texto)    ;

                    });
                }
            }
            if(main.getGameController().quemSou().matches("C") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_BC.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_BC.appendText(texto)    ;

                    });
                }
            }
        }
        if(ch.matches("BD")& (main.getGameController().quemSou().matches("B") | main.getGameController().quemSou().matches("D")))
        {
            if(main.getGameController().quemSou().matches("B") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_BD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_BD.appendText(texto)    ;

                    });
                }
            }
            if(main.getGameController().quemSou().matches("D") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_BD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_BD.appendText(texto)    ;

                    });
                }
            }
        }
        if(ch.matches("CD")& (main.getGameController().quemSou().matches("C") | main.getGameController().quemSou().matches("D")))
        {
            if(main.getGameController().quemSou().matches("C") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_CD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_CD.appendText(texto)    ;

                    });
                }
            }
            if(main.getGameController().quemSou().matches("D") & main.getGameController().estouOnline()){
                if(main.getGameController().estouOnline() && main.getGameController().resgatarChat == false){
                    Platform.runLater(() -> {
                        TA_BOX_CD.appendText(texto)    ;

                    });
                }
                else if(main.getGameController().estouOnline() && main.getGameController().resgatarChat){
                    Platform.runLater(() -> {
                        TA_BOX_CD.appendText(texto)    ;

                    });
                }
            }
        }
    }
    
    public void selectChatBox(String mensagem){
        if(mensagem.matches("AB") ){
            TA_BOX_AB.setVisible(true);
            TA_BOX_AC.setVisible(false);
            TA_BOX_AD.setVisible(false);
            TA_BOX_BC.setVisible(false);
            TA_BOX_BD.setVisible(false);
            TA_BOX_CD.setVisible(false);
        }
        else if(mensagem.matches("AC")){
            TA_BOX_AB.setVisible(false);
            TA_BOX_AC.setVisible(true);
            TA_BOX_AD.setVisible(false);
            TA_BOX_BC.setVisible(false);
            TA_BOX_BD.setVisible(false);
            TA_BOX_CD.setVisible(false);
        }
        else if(mensagem.matches("AD")){
            TA_BOX_AB.setVisible(false);
            TA_BOX_AC.setVisible(false);
            TA_BOX_AD.setVisible(true);
            TA_BOX_BC.setVisible(false);
            TA_BOX_BD.setVisible(false);
            TA_BOX_CD.setVisible(false);
        }
        else if(mensagem.matches("BC")){
            TA_BOX_AB.setVisible(false);
            TA_BOX_AC.setVisible(false);
            TA_BOX_AD.setVisible(false);
            TA_BOX_BC.setVisible(true);
            TA_BOX_BD.setVisible(false);
            TA_BOX_CD.setVisible(false);
        }
        else if(mensagem.matches("BD")){
            TA_BOX_AB.setVisible(false);
            TA_BOX_AC.setVisible(false);
            TA_BOX_AD.setVisible(false);
            TA_BOX_BC.setVisible(false);
            TA_BOX_BD.setVisible(true);
            TA_BOX_CD.setVisible(false);
        }
        else if(mensagem.matches("CD")){
            TA_BOX_AB.setVisible(false);
            TA_BOX_AC.setVisible(false);
            TA_BOX_AD.setVisible(false);
            TA_BOX_BC.setVisible(false);
            TA_BOX_BD.setVisible(false);
            TA_BOX_CD.setVisible(true);
        }
        
    }

    */
        //</editor-fold>

}
