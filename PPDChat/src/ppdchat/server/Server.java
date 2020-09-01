/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import ppdchat.client.Client;
//import ppdchat.server.Message;
import ppdchat.utils.*;
import ppdchat.server.Lookup;

/**
 *
 * @author Matheus
 */
public class Server {

    private int clientesConectados = 0;
    private int reiniciarpartida = 0;
    int x = 0;
    //private SpaceHandler spacehandler = SpaceHandler.getInstance();

    //protected ArrayList<ClientForm> clients;
    //protected Map<String, ClientForm> clientbyname = new HashMap<>();
    
    protected Map<String, String> clientchat = new HashMap<>();
    protected ArrayList<String> names;
    protected ArrayList<String> chatnames;
    //public Map<Integer, String> names = new HashMap<>();

    Lookup finder;
    JavaSpace space;

    public Server() throws RemoteException {
        //clients = new ArrayList<>();
        names = new ArrayList<>();
        chatnames = new ArrayList<>();
        this.finder = new Lookup(JavaSpace.class);
        this.space = (JavaSpace) finder.getService();
        if (space == null) {
            System.out.println("Não foi possível encontrar o JavaSpace!");
        } else {
            System.out.println("JavaSpace encontrado: " + space);
        }
        System.out.println("Server Started Sucessfully!");
        messageHandler();
    }

    public void messageHandler() {
        System.out.println("A função messageHandler() foi iniciada!");
        while (true) {
            try {
                Message template = new Message();
                template.destino = "Servidor";
                template.servidorLeu = false;
                if (template == null) {
                    System.out.println("Template nulo!");
                }
                Message msg = (Message) space.take(template, null, 300 * 1000);
                if (!names.contains(msg.name)) {
                    System.out.println("Novo cliente adicionado: " + msg.name);
                    names.add(msg.name);
                    System.out.println("Total de clientes: " + names.size());
                }
                if (msg != null) {
                    switch (msg.type) {
                        case "Mensagem":
                            System.out.println("Destino da Mensagem: " + msg.destino);
                            System.out.println("Mensagem recebida de " + msg.name + ": " + msg.content);
                            msg.servidorLeu = true;
                            x = 0;
                            while (x < names.size()) {
                                writeMessage(msg.name + ": ", msg.chatname, msg.content, names.get(x));
                                x = x + 1;
                            }
                            
                            //Envia uma mensagem única para cada nome registrado!
                            /*
                            while (x < names.size()) {
                                if (!names.get(x).equals(msg.name)) {
                                    writeMessage(msg.name + ": ", msg.content, names.get(x));
                                }
                                x = x + 1;
                            }
                            */
                            break;
                        case "NewChat":
                            if(!chatnames.contains(msg.chatname)){
                                System.out.println("Novo Chat criado: " + msg.chatname);
                                chatnames.add(msg.chatname);
                                msg.servidorLeu = true;
                                x = 0;
                                /*
                                while (x < names.size()) {
                                    if (!names.get(x).equals(msg.name)) {
                                        writeNewChat(msg.name, msg.chatname, names.get(x));
                                    }
                                    x = x + 1;
                                }
                                */
                                while (x < names.size()) {
                                    writeNewChat(msg.name, msg.chatname, names.get(x));

                                    x = x + 1;
                                }
                            }
                            
                            //clientchat.put(msg.name, msg.chatname);
                            break;
                        case "NewClient":
                            if (!names.contains(msg.name)) {
                                System.out.println("Novo cliente adicionado: " + msg.name);
                                names.add(msg.name);
                                System.out.println("Total de clientes: " + names.size());
                            }
                            break;
                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeMessage(String name, String chatname, String message, String destino) {
        try {
            Message msg = new Message();
            msg.type = "Mensagem";
            msg.destino = destino;
            msg.name = name;
            msg.content = message;
            msg.chatname = chatname;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("Mensagem enviada para: " + destino);
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

    public void writeNewChat(String name, String chatname, String destino){
        try {
            Message msg = new Message();
            msg.type = "NewChat";
            msg.destino = destino;
            msg.name = name;
            msg.chatname = chatname;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("CHAT enviado para: " + destino);
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
    
    // <editor-fold defaultstate="collapsed" desc="Old Project">

    /*
    @Override
    public void registerClient(ClientInterface client) throws RemoteException{
        System.out.println(clients.size());
        if(clients.size()<=4){
            System.out.println("Novo Cliente!");
            clients.add(client);
            System.out.println("Nº de clientes: " + clients.size());
            System.out.println("Jogador " + clients.size() + " se conectou! Preparando sua Janela de Chat!");
            iniciarJogo();
        }
    }
    @Override
    public void broadcastTexto(ClientInterface client, String texto){
        if(clients.size() <= 4){
            int i = clients.indexOf(client);
            int clientsSize = clients.size();
            int j = 0;
            
            if (clients.size() == 2) {
                try {
                    if (i == 0) {
                        clients.get(1).receberTexto(texto);
                    }
                    if (i == 1) {
                        clients.get(0).receberTexto(texto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (clients.size() == 3) {
                try {
                    if (i == 0) {
                        clients.get(1).receberTexto(texto);
                        clients.get(2).receberTexto(texto);
                    }
                    if (i == 1) {
                        clients.get(0).receberTexto(texto);
                        clients.get(2).receberTexto(texto);
                    }
                    if (i == 2) {
                        clients.get(0).receberTexto(texto);
                        clients.get(1).receberTexto(texto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (clients.size() == 4) {
                try {
                    if (i == 0) {
                        clients.get(1).receberTexto(texto);
                        clients.get(2).receberTexto(texto);
                        clients.get(3).receberTexto(texto);
                    }
                    if (i == 1) {
                        clients.get(0).receberTexto(texto);
                        clients.get(2).receberTexto(texto);
                        clients.get(3).receberTexto(texto);
                    }
                    if (i == 2) {
                        clients.get(0).receberTexto(texto);
                        clients.get(1).receberTexto(texto);
                        clients.get(3).receberTexto(texto);
                    }
                    if (i == 3) {
                        clients.get(0).receberTexto(texto);
                        clients.get(1).receberTexto(texto);
                        clients.get(2).receberTexto(texto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
        }
    }
    
    @Override
    public void broadcastStart(ClientInterface client) throws RemoteException{
        client.receberStart();
    }
    
    @Override
    public void receberStart() throws RemoteException{
        clientesConectados();
    }
    
    @Override
    public void broadcastStatus(ClientInterface client, String id, String status){
        if(clients.size() <= 4){
            int i = clients.indexOf(client);
            if (clients.size() == 2) {
                try {
                    if (i == 0) {
                        clients.get(1).receberStatus(id, status);
                    }
                    if (i == 1) {
                        clients.get(0).receberStatus(id, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (clients.size() == 3) {
                try {
                    if (i == 0) {
                        clients.get(1).receberStatus(id, status);
                        clients.get(2).receberStatus(id, status);
                    }
                    if (i == 1) {
                        clients.get(0).receberStatus(id, status);
                        clients.get(2).receberStatus(id, status);
                    }
                    if (i == 2) {
                        clients.get(0).receberStatus(id, status);
                        clients.get(1).receberStatus(id, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (clients.size() == 4) {
                try {
                    if (i == 0) {
                        clients.get(1).receberStatus(id, status);
                        clients.get(2).receberStatus(id, status);
                        clients.get(3).receberStatus(id, status);
                    }
                    if (i == 1) {
                        clients.get(0).receberStatus(id, status);
                        clients.get(2).receberStatus(id, status);
                        clients.get(3).receberStatus(id, status);
                    }
                    if (i == 2) {
                        clients.get(0).receberStatus(id, status);
                        clients.get(1).receberStatus(id, status);
                        clients.get(3).receberStatus(id, status);
                    }
                    if (i == 3) {
                        clients.get(0).receberStatus(id, status);
                        clients.get(1).receberStatus(id, status);
                        clients.get(2).receberStatus(id, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
        }
        
    }
    

    @Override
    public void broadcastNick(ClientInterface client, String nick) throws RemoteException{
        int i = clients.indexOf(client);
        if(names.size()!=4){
            names.put(names.size(), nick);    
            if(names.size()==1){
                clients.get(0).receberNick(nick);
            }
            else if(names.size()==2){
                clients.get(0).receberNick(nick);
                clients.get(1).receberNick(nick);
                clients.get(1).receberNick(names.get(0));
                
            }
            else if(names.size()==3){
                clients.get(0).receberNick(nick);
                clients.get(1).receberNick(nick);
                clients.get(2).receberNick(nick);
                clients.get(2).receberNick(names.get(0));
                clients.get(2).receberNick(names.get(1));
                
            }
            else if(names.size()==4){
                clients.get(0).receberNick(nick);
                clients.get(1).receberNick(nick);
                clients.get(2).receberNick(nick);
                clients.get(3).receberNick(nick);
                clients.get(3).receberNick(names.get(0));
                clients.get(3).receberNick(names.get(1));
                clients.get(3).receberNick(names.get(2));
            }

        }
    }
    
    @Override
    public void enviarConfig(ClientInterface client, String tipo) throws RemoteException{
        client.receberConfig(tipo);
    }

    private void clientesConectados() throws RemoteException{
        this.clientesConectados+=1;
        if(this.clientesConectados <= 4){
            configurarCliente();
            
        }
    }
    
    private void iniciarJogo(){
        System.out.println("Preparações terminadas - CHAT START!");
        reiniciarpartida = 0;
        try{
            broadcastStart(clients.get(clients.size()-1));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarCliente() throws RemoteException{
        if(clients.size()==1){
            enviarConfig(this.clients.get(0),"A");
        }
        else if(clients.size()==2){
            enviarConfig(this.clients.get(1),"B");
        }
        else if(clients.size()==3){
            enviarConfig(this.clients.get(2),"C");
        }
        else if(clients.size()==4){
            enviarConfig(this.clients.get(3),"D");
        }
    }
    
     */
//</editor-fold>
}
