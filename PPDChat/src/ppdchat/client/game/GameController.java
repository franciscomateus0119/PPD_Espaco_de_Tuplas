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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.scene.layout.HBox;
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
public class GameController {

    private MainGameController main;
    Stage stage;

    String nome = "Anonimo";
    String nChat;
    String chatAtual = "";
    public ArrayList<String> chatnames = new ArrayList<>();
    public ArrayList<String> usernames = new ArrayList<>();
    public ArrayList<TextArea> textareas = new ArrayList<>();
    Map<String, TextArea> chats = new HashMap<>();
    TextArea textareateste;
    ListView<String> listviewSalas;
    ListView<String> listviewUsuarios;
    ObservableList<String> items;
    ObservableList<String> usuarios;

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
    @FXML
    TextArea TA_BOX;
    @FXML
    TextField TF_MSG;
    @FXML
    TextField TF_CRIAR_SALA;
    @FXML
    TextField TF_ENTRAR_SALA;
    @FXML TextField TF_CRIAR_SALA_USER;
    @FXML
    Button BUTTON_SEND;
    @FXML
    Button BUTTON_CRIAR;
    @FXML
    Button BUTTON_ENTRAR;
    @FXML
    Button BUTTON_ATUALIZAR;
    @FXML
    Button BUTTON_USUARIOS;
    @FXML Button BUTTON_CRIAR_USER;
    
    @FXML Label LABEL_SELECTED_CHAT;
    @FXML Label LABEL_SELECTED_USER;
    @FXML HBox HBOX_LISTVIEW;
    @FXML HBox HBOX_USERVIEW;
    @FXML HBox HBOX_SALA;
    //@FXML ListView LISTVIEW_SALAS_DISPONIVEIS;

    public void init(MainGameController mainGameController) {
        main = mainGameController;
        stage = PPDChat.getStage();
        listviewSalas = new ListView<>();
        listviewSalas.setPrefWidth(200);
        listviewSalas.setPrefHeight(200);
        listviewSalas.setLayoutX(376);
        listviewSalas.setLayoutY(192);
        listviewSalas.setVisible(true);
        listviewSalas.toFront();
        HBOX_LISTVIEW.getChildren().addAll(listviewSalas);
        listviewUsuarios = new ListView<>();
        listviewUsuarios.setPrefWidth(200);
        listviewUsuarios.setPrefHeight(200);
        listviewUsuarios.setLayoutX(376);
        listviewUsuarios.setLayoutY(192);
        listviewUsuarios.setVisible(true);
        listviewUsuarios.toFront();
        HBOX_USERVIEW.getChildren().addAll(listviewUsuarios);
        textareateste = new TextArea();
        
        items = FXCollections.observableArrayList();
        usuarios = FXCollections.observableArrayList();
        aceitarEnter();
        listViewListener();
        listViewUsuariosListener();
    }

    @FXML
    public void sendText(MouseEvent event) {
        if(chatAtual!=null && !chatAtual.equals("")){
            String texto = TF_MSG.getText() + "\n";
            TF_MSG.clear();
            TF_MSG.setPromptText("Digite sua Mensagem");
            enviarTextoMensagem(texto);
        }
        else{
            TF_MSG.clear();
            TF_MSG.setPromptText("Selecione uma sala antes de enviar!");
        }
        
    }

    public void enviarTextoMensagem(String texto) {
        Platform.runLater(() -> main.getClient().writeMessageToServer(nome, chatAtual, texto));
    }

    public void setNome(String nome) {
        if (!nome.equals("") && !(nome == null)) {
            this.nome = nome;
        }

    }
    
    @FXML
    public void atualizarListaSalas(MouseEvent event){
        main.getClient().writeAtualizarListaSala(nome);
    }
    
    @FXML
    public void atualizarListaUsuarios(MouseEvent event){
        if(chatAtual!=null && !chatAtual.equals("")){
            main.getClient().writeAtualizarListaUser(nome, chatAtual);
        }
        
    }

    @FXML
    public void criarSala(MouseEvent event) {
        //Se o nome da Sala não for vazio nem nulo, crie a sala
        if(chatnames.contains(TF_CRIAR_SALA) && (!TF_CRIAR_SALA.getText().equals("") || TF_CRIAR_SALA.getText() != null)){
            TF_CRIAR_SALA.clear();
            TF_CRIAR_SALA.setPromptText("Esta sala ja existe!");
        }
        else if (!TF_CRIAR_SALA.getText().equals("") || TF_CRIAR_SALA.getText() != null) {
            String textareaname = TF_CRIAR_SALA.getText();
            Platform.runLater(() -> {
                main.getClient().writeNewChatToServer(nome, textareaname);
                //main.getClient().writeNewChatToServer(nome, textareaname);
                //main.getClient().writeNewChatToServer(nome, textareaname);
            });
            System.out.println("Nova sala criada: " + textareaname);
            TF_CRIAR_SALA.clear();
        } else {
            TF_CRIAR_SALA.clear();
            TF_CRIAR_SALA.setPromptText("DIGITE UM NOME NÃO VAZIO");
        }

    }
    
    @FXML
    public void criarSalaUser(MouseEvent event){
        if(chatnames.contains(TF_CRIAR_SALA_USER.getText()) && !TF_CRIAR_SALA_USER.getText().equals("") && TF_CRIAR_SALA_USER.getText()!=null){
            //TF_CRIAR_SALA_USER.clear();
            TF_CRIAR_SALA_USER.setPromptText("Esta sala ja existe!");
        }
        else if(!TF_CRIAR_SALA_USER.getText().equals("") && TF_CRIAR_SALA_USER.getText()!=null && !TF_CRIAR_SALA_USER.getText().equals(nome)){
            String textareaUsername = TF_CRIAR_SALA_USER.getText();
            Platform.runLater(()->{
                main.getClient().writeNewUserChatToServer(nome, textareaUsername);
            });
            System.out.println("Nova salaUser criada: " + textareaUsername);
            TF_CRIAR_SALA_USER.clear();
        }
        else if(TF_CRIAR_SALA_USER.getText().equals(nome)){
            //TF_CRIAR_SALA_USER.clear();
            TF_CRIAR_SALA_USER.setPromptText("ESCOLHA UM NOME QUE NÃO SEJA O SEU");
        }
        else{
            //TF_CRIAR_SALA_USER.clear();
            TF_CRIAR_SALA_USER.setPromptText("ESCOLHA UM NOME NÃO VAZIO");
        }
    }
    
    public void adicionarSala(ArrayList<String> nomedassalas) {
        //String textareaname = nomedasala;
        
        int tamanho = nomedassalas.size();
        //items.clear();
        for(int h = 0;h<tamanho;h++){
            //Se a sequencia de conversapriva (P) estiver no chat
            if(nomedassalas.get(h).contains("(P)")){
                //Se o nome da pessoa estiver contida no nome da sala
                if(nomedassalas.get(h).contains(nome)){
                    //Se a sala ainda não foi adicionada
                    if(!chatnames.contains(nomedassalas.get(h))){
                        TextArea textarea = new TextArea();
                        chatnames.add(nomedassalas.get(h));
                        chats.put(nomedassalas.get(h), textarea);
                        textareas.add(textarea);
                        items.add(nomedassalas.get(h));
                        listviewSalas.setItems(items);
                        System.out.println("Nova sala privada disponível: " + nomedassalas.get(h));
                    }
                }
            }
            else{
                if(!chatnames.contains(nomedassalas.get(h))){
                    TextArea textarea = new TextArea();
                    chatnames.add(nomedassalas.get(h));
                    chats.put(nomedassalas.get(h), textarea);
                    textareas.add(textarea);
                    items.add(nomedassalas.get(h));
                    listviewSalas.setItems(items);
                    System.out.println("Nova sala disponível: " + nomedassalas.get(h));
                }
            }
  
        }  
    }
    
    public void adicionarUser(ArrayList<String> nomesdosusers) {
        //String textareaname = nomedasala;
        listviewUsuarios.getItems().clear();
        usernames.clear();
        int tamanho = nomesdosusers.size();
        //items.clear();
        for(int f = 0;f<tamanho;f++){
            if(!usernames.contains(nomesdosusers.get(f))){
                //TextArea textarea = new TextArea();
                usernames.add(nomesdosusers.get(f));
                //chats.put(nomesdosusers.get(f), textarea);
                //textareas.add(textarea);
                usuarios.add(nomesdosusers.get(f));
                listviewUsuarios.setItems(usuarios);
                System.out.println("Novo usuario disponível: " + nomesdosusers.get(f));
            }
            /*
            else{
                usuarios.add(nomesdosusers.get(f));
                listviewUsuarios.setItems(usuarios);
                System.out.println("Novo usuario disponível: " + nomesdosusers.get(f));
            }
            */
  
        }  
    }
    
    @FXML
    public void entrarSalaRequest(MouseEvent event){
        if(TF_ENTRAR_SALA.getText()!=null && !TF_ENTRAR_SALA.getText().equals("")){
            System.out.println("Feito pedido para sair de " + chatAtual + " para " + TF_ENTRAR_SALA.getText());
            String chatnome = TF_ENTRAR_SALA.getText();
            TF_ENTRAR_SALA.clear();
            Platform.runLater(() -> {
                main.getClient().writeEntrarSalaRequest(nome, chatnome, chatAtual);
            });
        }
    }

    
    public void entrarSala(String sala){
        chatAtual = sala;
        if(TF_MSG.isDisabled() || !TF_MSG.isVisible()){
            TF_MSG.setDisable(false);
            TF_MSG.setVisible(true);
        }
        if(BUTTON_SEND.isDisabled() || !BUTTON_SEND.isVisible()){
            BUTTON_SEND.setDisable(false);
            BUTTON_SEND.setVisible(true);
        }
        if(TA_BOX.isDisabled() || !TA_BOX.isVisible()){
            TA_BOX.setDisable(false);
            TA_BOX.setVisible(true);
        }
    }
    
    public void removerSala(String sala){
        int index = listviewSalas.getItems().indexOf(sala);
        System.out.println("Removendo sala: " + listviewSalas.getItems().get(index));
        listviewSalas.getItems().remove(index);
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

    public void listViewListener() {
        listviewSalas.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            String selectedItem = listviewSalas.getSelectionModel().getSelectedItem();
            int index = listviewSalas.getSelectionModel().getSelectedIndex();
            LABEL_SELECTED_CHAT.setText("Selected Chat: " + selectedItem + " - Index : " + index);
            TF_ENTRAR_SALA.setText(selectedItem);
            System.out.println("Sala selecionada: " + selectedItem);
        });
    }
    public void listViewUsuariosListener() {
        listviewUsuarios.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            String selectedItem = listviewUsuarios.getSelectionModel().getSelectedItem();
            int index = listviewUsuarios.getSelectionModel().getSelectedIndex();    
            LABEL_SELECTED_USER.setText("Selected Chat: " + selectedItem + " - Index : " + index);
            TF_CRIAR_SALA_USER.setText(selectedItem);
            System.out.println("Sala selecionada: " + selectedItem);
        });
    }

        
    }
