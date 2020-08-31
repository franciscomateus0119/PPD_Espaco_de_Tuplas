/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.client.game;

import ppdchat.PPDChat;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import net.jini.space.JavaSpace;


/**
 * FXML Controller class
 *
 * @author Matheus
 */
public class GameController{
    private MainGameController main;
    //private SpaceHandler spacehandler;
    Stage stage;
    
    String nome = "Anonimo";
    String nChat;
    Map<Integer, String> chats = new HashMap<>();
    
    // <editor-fold defaultstate="collapsed" desc="Old Project">
    /*
    Boolean circle_a_on = true;
    Boolean circle_b_on = true;
    Boolean circle_c_on = true;
    Boolean circle_d_on = true;
    
    Boolean circle_a_ready = false;
    Boolean circle_b_ready = false;
    Boolean circle_c_ready = false;
    Boolean circle_d_ready = false;
    
    
    Boolean sou_A = false;
    Boolean sou_B = false;
    Boolean sou_C = false;
    Boolean sou_D = false;
    
    Boolean resgatar = false;
    Boolean firstResgate = false;
    Boolean resgatarChat = false;
    
    String meuNome = "X";
    String lastSelected;
    
    
    
    Color green = Color.LIGHTGREEN;
    Color gray = Color.LIGHTGRAY;
    
    
    Map<String, Circle> circles = new HashMap<>();
    Map<String, Button> togglebuttons = new HashMap<>();
    Map<String, Circle> button_to_circle = new HashMap<>();
    Map<String, String> contatos = new HashMap<>();
   
    @FXML Circle CIRCLE_A;
    @FXML Circle CIRCLE_B;
    @FXML Circle CIRCLE_C;
    @FXML Circle CIRCLE_D;
    
    @FXML Rectangle RECTANGLE_A;
    @FXML Rectangle RECTANGLE_B;
    @FXML Rectangle RECTANGLE_C;
    @FXML Rectangle RECTANGLE_D;
    
    
    @FXML Button BUTTON_SWITCH_A;
    @FXML Button BUTTON_SWITCH_B;
    @FXML Button BUTTON_SWITCH_C;
    @FXML Button BUTTON_SWITCH_D;
    

    
    @FXML Label LABEL_CHAT;
    @FXML Label LABEL_CONTATO_A;
    @FXML Label LABEL_CONTATO_B;
    @FXML Label LABEL_CONTATO_C;
    @FXML Label LABEL_CONTATO_D;
            
    @FXML TextField TF_GAME;
    
    @FXML ImageView IMAGE_SEND;
    */
    //</editor-fold>
    
    @FXML TextArea TA_BOX;
    @FXML TextField TF_MSG;
    @FXML Button BUTTON_SEND;
    

    
    public void init(MainGameController mainGameController){
        main = mainGameController;
        //initbuttons();
        stage = PPDChat.getStage();   
        aceitarEnter();
    }
     
    @FXML
    public void sendText(MouseEvent event){
        String texto = TF_MSG.getText() + "\n";
        TA_BOX.appendText("Você: " + texto);
        enviarTextoMensagem(texto);
    }
    
    public void enviarTextoMensagem(String texto){
        Platform.runLater(() -> main.getClient().writeMessageToServer(nome, texto));
        //Platform.runLater(() -> main.getClient().writeMessageToClient(nome, texto));
    }

    public void setNome(String nome) {
        if(!nome.equals("") && !(nome == null)){
            this.nome = nome;
        }
        
    }
    
    
    
    public void aceitarEnter() {
        TF_MSG.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isShiftDown()) {
                    if (event.getCode() == KeyCode.ENTER) {
                        String texto = TF_MSG.getText();
                        TF_MSG.setText(texto + "\n");
                        TF_MSG.positionCaret(TF_MSG.getText().length());
                    }
                } else {
                    if (event.getCode() == KeyCode.ENTER) {
                        String texto = TF_MSG.getText();
                        event.consume(); 
                        System.out.println("Mensagem Enviada");
                        TF_MSG.clear();
                    }
                }
            }
        });     
    }
    /*
    public void setSpacehandler(SpaceHandler spacehandler) {
        this.spacehandler = spacehandler;
    }
    */
    
    // <editor-fold defaultstate="collapsed" desc="Old Project">
    /*
    @FXML
    void toggle(MouseEvent event) throws RemoteException{
       Button button = (Button) event.getSource();
       String buttonID = button.getId();
       System.out.println(sou_A);
       System.out.println(sou_B);
       if (buttonID.matches(BUTTON_SWITCH_A.getId())){
           if (circle_a_on == true){
               button_to_circle.get(buttonID).setFill(gray);
               circle_a_on=false;
               resgatar = true;
               resgatarChat = false;
               circle_a_ready = false;
               main.getClient().enviarStatus(CIRCLE_A.getId(),"OFF");
           }
           else{
               button_to_circle.get(buttonID).setFill(green);
               circle_a_on=true;
               resgatar = false;
               resgatarChat = true;
               circle_a_ready = true;
               main.getClient().enviarStatus(CIRCLE_A.getId(),"ON");
               try{
               }catch(Exception e){e.printStackTrace();}
               firstResgate = true;
           }
       }
       else if(buttonID.matches(BUTTON_SWITCH_B.getId())){
           if (circle_b_on == true){
               button_to_circle.get(buttonID).setFill(gray);
               circle_b_on=false;
               resgatar = true;
               resgatarChat = false;
               circle_b_ready = false;
               main.getClient().enviarStatus(CIRCLE_B.getId(),"OFF");
           }
           else{
               button_to_circle.get(buttonID).setFill(green);
               circle_b_on=true;
               resgatar = false;
               resgatarChat = true;
               circle_b_ready = true;
               main.getClient().enviarStatus(CIRCLE_B.getId(),"ON");
               try{
               }catch(Exception e){e.printStackTrace();}
           }
       }
       else if(buttonID.matches(BUTTON_SWITCH_C.getId())){
           if (circle_c_on == true){
               button_to_circle.get(buttonID).setFill(gray);
               circle_c_on=false;
               resgatar = true;
               resgatarChat = false;
               circle_c_ready = false;
               main.getClient().enviarStatus(CIRCLE_C.getId(),"OFF");
           }
           else{
               button_to_circle.get(buttonID).setFill(green);
               circle_c_on=true;
               resgatar = false;
               resgatarChat = true;
               circle_c_ready = true;
               main.getClient().enviarStatus(CIRCLE_C.getId(),"ON");
               try{
               }catch(Exception e){e.printStackTrace();}
           }
       }
       else if(buttonID.matches(BUTTON_SWITCH_D.getId())){
           if (circle_d_on == true){
               button_to_circle.get(buttonID).setFill(gray);
               circle_d_on=false;
               resgatar = true;
               resgatarChat = false;
               circle_d_ready = false;
               main.getClient().enviarStatus(CIRCLE_D.getId(),"OFF");
           }
           else{
               button_to_circle.get(buttonID).setFill(green);
               circle_d_on=true;
               resgatar = false;
               resgatarChat = true;
               circle_d_ready = true;
               main.getClient().enviarStatus(CIRCLE_D.getId(),"ON");
               try{
               }catch(Exception e){e.printStackTrace();}
           }
       }
    }
    void initbuttons(){
        togglebuttons.put(BUTTON_SWITCH_A.getId(), BUTTON_SWITCH_A);
        togglebuttons.put(BUTTON_SWITCH_B.getId(), BUTTON_SWITCH_B);
        togglebuttons.put(BUTTON_SWITCH_C.getId(), BUTTON_SWITCH_C);
        togglebuttons.put(BUTTON_SWITCH_D.getId(), BUTTON_SWITCH_D);

        button_to_circle.put(BUTTON_SWITCH_A.getId(), CIRCLE_A);
        button_to_circle.put(BUTTON_SWITCH_B.getId(), CIRCLE_B);
        button_to_circle.put(BUTTON_SWITCH_C.getId(), CIRCLE_C);
        button_to_circle.put(BUTTON_SWITCH_D.getId(), CIRCLE_D);
        
        circles.put(CIRCLE_A.getId(), CIRCLE_A);
        circles.put(CIRCLE_B.getId(), CIRCLE_B);
        circles.put(CIRCLE_C.getId(), CIRCLE_C);
        circles.put(CIRCLE_D.getId(), CIRCLE_D);
        
        
        
    }
    
    public void setPessoa(String nome){
        if(nome.equals("A")){
            sou_A = true;
            BUTTON_SWITCH_A.setVisible(true);
            BUTTON_SWITCH_A.setDisable(false);
            CIRCLE_A.setDisable(true);
            RECTANGLE_A.setVisible(true);
            RECTANGLE_B.setFill(Color.web("#cdcdcd"));
            RECTANGLE_C.setFill(Color.web("#cdcdcd"));
            RECTANGLE_D.setFill(Color.web("#cdcdcd"));
            
                    
            Platform.runLater(() -> LABEL_CONTATO_A.setText("Você"));
        }
        if(nome.equals("B")){
            sou_B = true;
            BUTTON_SWITCH_B.setVisible(true);
            BUTTON_SWITCH_B.setDisable(false);
            CIRCLE_B.setDisable(true);
            RECTANGLE_B.setVisible(true);
            RECTANGLE_A.setFill(Color.web("#cdcdcd"));
            RECTANGLE_C.setFill(Color.web("#cdcdcd"));
            RECTANGLE_D.setFill(Color.web("#cdcdcd"));
            Platform.runLater(() -> LABEL_CONTATO_B.setText("Você"));
            
        }
        if(nome.equals("C")){
            sou_C = true;
            BUTTON_SWITCH_C.setVisible(true);
            BUTTON_SWITCH_C.setDisable(false);
            CIRCLE_C.setDisable(true);
            RECTANGLE_C.setVisible(true);
            RECTANGLE_A.setFill(Color.web("#cdcdcd"));
            RECTANGLE_B.setFill(Color.web("#cdcdcd"));
            RECTANGLE_D.setFill(Color.web("#cdcdcd"));
            Platform.runLater(() -> LABEL_CONTATO_C.setText("Você"));
        }
        if(nome.equals("D")){
            sou_D = true;
            BUTTON_SWITCH_D.setVisible(true);
            BUTTON_SWITCH_D.setDisable(false);
            CIRCLE_D.setDisable(true);
            RECTANGLE_D.setVisible(true);
            RECTANGLE_A.setFill(Color.web("#cdcdcd"));
            RECTANGLE_B.setFill(Color.web("#cdcdcd"));
            RECTANGLE_C.setFill(Color.web("#cdcdcd"));
            Platform.runLater(() -> LABEL_CONTATO_D.setText("Você"));
        }
    }
    
    @FXML
    public void clickedSend(MouseEvent event) throws JMSException, NamingException, RemoteException{
        String texto = TF_GAME.getText();
        enviarMensagem(texto);
    }
    
    @FXML
    public void enviarMensagem(String texto) throws JMSException, NamingException, RemoteException{
        System.out.println("Enviando Mensagem!");
        System.out.println(texto);
        if(!texto.equals("")){
            texto = lastSelected + meuNome +": " + TF_GAME.getText() + "\n";
            if(sou_A){
                if(circle_b_on && lastSelected.matches("AB") && !circle_b_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_c_on && lastSelected.matches("AC")&& !circle_c_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_d_on && lastSelected.matches("AD")&& !circle_d_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                

            }
            if(sou_B){
                if(circle_a_on && lastSelected.matches("AB") && !circle_a_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_c_on && lastSelected.matches("BC")&& !circle_c_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_d_on && lastSelected.matches("BD")&& !circle_d_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }

            }
            if(sou_C){
                if(circle_a_on && lastSelected.matches("AC") && !circle_a_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_b_on && lastSelected.matches("BC")&& !circle_b_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_d_on && lastSelected.matches("CD")&& !circle_d_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }

            }
            if(sou_D){
                if(circle_a_on && lastSelected.matches("AD") && !circle_a_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_b_on && lastSelected.matches("BD")&& !circle_b_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }
                if(circle_c_on && lastSelected.matches("CD")&& !circle_d_ready){
                    main.getClient().enviarTexto(texto);
                    TF_GAME.clear();
                }

            }  
            TF_GAME.clear();
        }
    }

    @FXML
    public void statusPessoa(String id, String status){
        if (id.matches(CIRCLE_A.getId())){
            if (circle_a_on == true && status.matches("OFF")){
               CIRCLE_A.setFill(Color.web("#e3e3e3"));
               circle_a_on=false;
               circle_a_ready = true;
           }
           else{
               CIRCLE_A.setFill(green);
               circle_a_on=true;
               circle_a_ready = false;
           }
        }
        else if (id.matches(CIRCLE_B.getId())){
            if (circle_b_on == true && status.matches("OFF")){
               CIRCLE_B.setFill(Color.web("#e3e3e3"));
               circle_b_on=false;
               circle_b_ready = true;
           }
           else{
               CIRCLE_B.setFill(green);
               circle_b_on=true;
               circle_b_ready = false;
           }
        }
        else if (id.matches(CIRCLE_C.getId())){
            if (circle_c_on == true && status.matches("OFF")){
               CIRCLE_C.setFill(Color.web("#e3e3e3"));
               circle_c_on=false;
               circle_c_ready = true;
           }
           else{
               CIRCLE_C.setFill(green);
               circle_c_on=true;
               circle_c_ready = false;
           }
        }
        else if (id.matches(CIRCLE_D.getId() )){
            if (circle_d_on == true && status.matches("OFF")){
               CIRCLE_D.setFill(Color.web("#e3e3e3"));
               circle_d_on=false;
               circle_d_ready = true;
           }
           else{
               CIRCLE_D.setFill(green);
               circle_d_on=true;
               circle_d_ready = false;
           }
        }
    }
    
    @FXML
    void lastCirclePressed(MouseEvent event){
        Circle circle = (Circle) event.getSource();
        String id = circle.getId();
        if (sou_A){
            if(id.matches(CIRCLE_B.getId())){
                lastSelected = "AB";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_B.setVisible(true);
                RECTANGLE_C.setVisible(false);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_C.getId())){
                lastSelected = "AC";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_C.setVisible(true);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_D.getId())){
                lastSelected = "AD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_C.setVisible(false);
                RECTANGLE_D.setVisible(true);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
        }
        else if (sou_B){
            if(id.matches(CIRCLE_A.getId())){
                lastSelected = "AB";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(true);
                RECTANGLE_C.setVisible(false);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_C.getId())){
                lastSelected = "BC";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_C.setVisible(true);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_D.getId())){
                lastSelected = "BD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_C.setVisible(false);
                RECTANGLE_D.setVisible(true);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
        }
        else if (sou_C){
            if(id.matches(CIRCLE_A.getId())){
                lastSelected = "AC";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(true);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_B.getId())){
                lastSelected = "BC";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_B.setVisible(true);
                RECTANGLE_D.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_D.getId())){
                lastSelected = "CD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_D.setVisible(true);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
        }
        else if (sou_D){
            if(id.matches(CIRCLE_A.getId())){
                lastSelected = "AD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(true);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_C.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_B.getId())){
                lastSelected = "BD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_B.setVisible(true);
                RECTANGLE_C.setVisible(false);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
            else if(id.matches(CIRCLE_C.getId())){
                lastSelected = "CD";
                IMAGE_SEND.setVisible(true);
                TF_GAME.setVisible(true);
                RECTANGLE_A.setVisible(false);
                RECTANGLE_B.setVisible(false);
                RECTANGLE_C.setVisible(true);
                Platform.runLater(()-> LABEL_CHAT.setText("Chat: " + lastSelected));
                Platform.runLater(() -> main.getChatToolbarController().selectChatBox(lastSelected));
            }
        }
    }
    
    public String quemSou(){
        if(sou_A){
            return "A";
        }
        if(sou_B){
            return "B";
        }
        if(sou_C){
            return "C";
        }
        if(sou_D){
            return "D";
        }
        
        return "NONE";
    }
    
    public boolean estouOnline(){
        if(sou_A){
            return circle_a_on;
        }
        if(sou_B){
            return circle_b_on;
        }
        if(sou_C){
            return circle_c_on;
        }
        if(sou_D){
            return circle_d_on;
        }
        return false;
    }



    public void addContato(String texto, int n) throws JMSException,NamingException{
        if(!texto.matches("")){
            if(n==0){
            }
        }
    }
    public String getMeuNome() {
        return meuNome;
    }

    public void setMeuNome(String meuNome) {
        this.meuNome = meuNome;
    }
    
    public void setContatoA(String nome){
        Platform.runLater(() -> LABEL_CONTATO_A.setText(nome));
    }
    public void setContatoB(String nome){
        Platform.runLater(() -> LABEL_CONTATO_B.setText(nome));
    }
    public void setContatoC(String nome){
        Platform.runLater(() -> LABEL_CONTATO_C.setText(nome));
    }
    public void setContatoD(String nome){
        Platform.runLater(() -> LABEL_CONTATO_D.setText(nome));
    }
    
    public void aceitarEnter() {
        TF_GAME.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isShiftDown()) {
                    if (event.getCode() == KeyCode.ENTER) {
                        String texto = TF_GAME.getText();
                        TF_GAME.setText(texto + "\n");
                        TF_GAME.positionCaret(TF_GAME.getText().length());
                    }
                } else {
                    if (event.getCode() == KeyCode.ENTER) {
                        String texto = TF_GAME.getText();
                        event.consume(); 
                        try {
                            enviarMensagem(texto);
                        } catch (JMSException ex) {
                            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NamingException ex) {
                            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RemoteException ex) {
                            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Mensagem Enviada");
                        TF_GAME.clear();
                    }
                }
            }
        });     
    }
        */
            //</editor-fold>

    

    
}
