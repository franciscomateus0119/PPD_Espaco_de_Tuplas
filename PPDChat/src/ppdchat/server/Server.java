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

    //protected ArrayList<ClientForm> clients;
    //protected Map<String, ClientForm> clientbyname = new HashMap<>();
    
    protected Map<String, String> clientchat = new HashMap<>();
    protected Map<String, ArrayList<String>> usersinchat = new HashMap<>();
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
        writeUserList(names);
        while (true) {
            try {
                Message template = new Message();
                template.destino = "Servidor";
                template.servidorLeu = false;
                if (template == null) {
                    System.out.println("Template nulo!");
                }
                Message msg = (Message) space.take(template, null, 300 * 1000);
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
                            break;
                        case "NewChat":
                            if(!chatnames.contains(msg.chatname)){
                                System.out.println("Novo Chat criado: " + msg.chatname);
                                chatnames.add(msg.chatname);
                                writeChat(msg.chatname,"Espaco");
                                //Verifica se há uma lista de salas no espaço
                                Message temp5 = new Message();
                                temp5.destino = "Espaco";
                                temp5.type = "ListaChat";
                                Message newMsg = (Message) space.take(temp5, null, 15 * 1000);
                                //Se não tiver uma lista de salas no espaço --> Crie uma
                                if(newMsg==null){
                                    System.out.print("(NewChat) Não há uma lista de salas no espaço. Criando uma!");
                                    ArrayList<String> chatNomes = new ArrayList<>();
                                    chatNomes.add(msg.chatname);
                                    System.out.print("(NewChat) Tamanho da lista: " + chatNomes.size());
                                    writeListaChats(chatNomes,"Espaco");
                                }
                                //Se a lista existir --> atualizea
                                else{
                                    System.out.println("(NewChat) Lista de Salas Encontrada! Tamanho" + newMsg.chatList.size());
                                    ArrayList<String> chatNomes =  newMsg.chatList;
                                    chatNomes.add(msg.chatname);
                                    newMsg.chatList = chatNomes;
                                    System.out.println("(NewChat) Novo tamanho da lista de salas:" + newMsg.chatList.size());
                                    space.write(newMsg, null, 180 * 1000);
                                }
                                msg.servidorLeu = true;
                                x = 0;
                                while (x < names.size()) {
                                    writeNewChat(msg.name, msg.chatname, names.get(x));
                                    x = x + 1;
                                }
                            }
                            
                            break;
                        case "NewClient":
                            if(msg.name == null){
                                System.out.println("O NOME É NULOOOOOOOOOO");
                            }
                            if (!names.contains(msg.name) && msg.name != null && !msg.name.equals("")) {
                                System.out.println("Novo cliente adicionado: " + msg.name);
                                names.add(msg.name);
                                System.out.println("Total de clientes: " + names.size());
                                Message temp = new Message();
                                temp.destino = "Espaco";
                                temp.type = "ListaUsuarios";
                                space.take(temp, null, 15 * 1000);
                                writeUserList(names);    
                            }
                            break;
                        case "EntrarRequest":
                            System.out.println("Pedido de " + msg.name + " para sair de " + msg.content + " para " + msg.chatname);
                            //Se o nome da sala origem for vazio ou nulo
                            if (msg.content != null && !msg.content.equals("")) {
                                Message templ = new Message();
                                templ.destino = "Espaco";
                                templ.type = "Chat";
                                templ.chatname = msg.content;
                                Message sairchat = (Message) space.take(templ, null, 10 * 1000);
                                //Se a sala existir, remover o usuário da sua lista de usuários presentes
                                if (sairchat != null) {
                                    System.out.println("(EntrarRequest) Lista de salas encontrada!");
                                    if(sairchat.userInChatList != null){
                                        System.out.println("(EntrarRequest)Removendo usuário "+msg.name + " da sala" + msg.content);
                                        ArrayList<String> newlist = sairchat.userInChatList;
                                        if(newlist.contains(msg.content)){
                                            newlist.remove(msg.content);
                                            sairchat.userInChatList = newlist;
                                            System.out.println("Saiu de" + msg.content);
                                            space.write(sairchat, null, 180 * 1000);
                                        }
                                    }
                                }
                            }
                            //Procurar a Sala que se deseja entrar no Espaço
                            Message temp = new Message();
                            temp.destino = "Espaco";
                            temp.type = "Chat";
                            temp.chatname = msg.chatname;
                            Message chatmsg = (Message) space.take(temp, null, 10 * 1000);
                            //Se a sala não for encontrada
                            if (chatmsg == null) {
                                System.out.println("(EntrarRequest) A sala" + msg.chatname + " NÃO foi encontrada!");
                                writeEnterRequestResult(msg.name, msg.chatname, "Falhou");
                                //Verificar se o nome não encontrado existe na lista de salas (Sala não existe mais, porém está na lista)
                                Message temp2 = new Message();
                                temp2.destino = "Espaco";
                                temp2.type = "ListaChat";
                                Message newMensagem = (Message) space.take(temp2, null, 15 * 1000);
                                //Se existir uma lista de Salas
                                if(newMensagem!=null){
                                    System.out.println("(EntrarRequest) Lista de Salas encontradas");
                                    ArrayList<String> chatNomes = newMensagem.chatList;
                                    //Se a sala ainda estiver na lista de salas disponíveis, remove-la
                                    System.out.println("(EntrarRequest) " + msg.chatname + "está na lista de salas? " + chatNomes.contains(msg.chatname));
                                    if(chatNomes.contains(msg.chatname)){
                                        System.out.println("(EntrarRequest) Removendo " + msg.chatname + " da lista de salas!");
                                        chatNomes.remove(msg.chatname);
                                        newMensagem.chatList = chatNomes;
                                        space.write(newMensagem, null, 180 * 1000);
                                    }
                                    //Se não existir, devolva a lista de Salas pro espaço
                                    else{
                                        System.out.println("(EntrarRequest) " + msg.chatname + "não está na lista de salas! ");
                                        space.write(newMensagem, null, 180 * 1000);
                                    }
                                }
                              //Se a sala for encontrada/existir  
                            } else {
                                System.out.println("(EntrarRequest) A sala" + msg.chatname + " FOI encontrada!");
                                //Se a lista de salas que está no espaço for Nula, crie uma
                                if(chatmsg.userInChatList==null){
                                    ArrayList<String> novalista = new ArrayList<>();
                                    novalista.add(msg.name);
                                    chatmsg.userInChatList = novalista;//Adiciona o usuário na lista de usuários da sala.
                                }
                                //Se a lista de salas existente não for nula, adicione a sala destino na lista
                                else{
                                    System.out.println("(EntrarRequest) Lista de Salas não nula! Adicionando sala destino na lista...");
                                    ArrayList<String> novalista = chatmsg.userInChatList;
                                    novalista.add(msg.name);
                                    chatmsg.userInChatList = novalista;//Adiciona o usuário na lista de usuários da sala.
                                }
                                
                                try {
                                    //Devolva a lista para o Espaço
                                    
                                    space.write(chatmsg, null, 180 * 1000);
                                    System.out.println("(EntrarRequest) Lista devolvida para o espaço");
                                    //Avise que o processo de entrar na sala foi um sucesso!
                                    writeEnterRequestResult(msg.name, msg.chatname, "Sucesso");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case "AtualizarListaSalas":
                            //Procura a Lista de Salas
                            Message temp1 = new Message();
                            temp1.destino = "Espaco";
                            temp1.type = "ListaChat";
                            Message newMsg = (Message) space.take(temp1, null, 15 * 1000);
                            //Se não tiver uma lista de salas no espaço
                            //Se a lista de Salas Existir
                            if (newMsg != null) {
                                System.out.println("(AtualizarListaSalas) Lista de salas encontrada! Tamanho: " + newMsg.chatList.size());
                                ArrayList<String> chatNomes = new ArrayList<>();
                                chatNomes = newMsg.chatList;
                                //Envie a lista para o usuário que pediu
                                writeEnviarLista(msg.name,chatNomes, "Sucesso");
                                System.out.println("(AtualizarListaSalas) Lista de salas devolvida para o espaço!" );
                            }
                            //Se a lista de Salas não existir
                            else{
                                System.out.println("(AtualizarListaSalas) Lista de salas não existe! FALHA");
                                //devolva a lista nula!
                                writeEnviarLista(msg.name,null,"Falha");
                            }
                            break;
                        case "Lista":
                            msg.servidorLeu = true;
                            System.out.println("Lista Enviada");
                            
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
                    System.out.println("NewCHAT enviado para: " + destino);
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
    
    public void writeListaChats(ArrayList<String> chatlist, String destino){
        try {
            Message msg = new Message();
            msg.type = "ListaChat";
            msg.destino = destino;
            msg.chatList = chatlist;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 180 * 1000);
                    System.out.println("Lista Chats enviada para: " + destino);
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
    
    public void writeChat(String chatname, String destino){
        try {
            Message msg = new Message();
            msg.type = "Chat";
            msg.destino = destino;
            msg.chatname = chatname;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 180 * 1000);
                    System.out.println("CHAT " +chatname +" enviado para: " + destino);
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
    
    public void writeUserList(ArrayList<String> listanomes){
        try {
            Message msg = new Message();
            msg.type = "ListaUsuarios";
            msg.destino = "Espaco";
            msg.namesList = listanomes;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("Lista de Usuários enviada para o JavaSpace");
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
    
    public void writeEnterRequestResult(String name, String chatname, String result){
        try {
            Message msg = new Message();
            msg.type = "EnterRequestResult";
            msg.destino = name;
            msg.chatname = chatname;
            msg.content = result;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("Lista de Usuários da Sala enviada para o JavaSpace");
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
    
    public void writeEnviarLista(String name, ArrayList<String> listachats, String content){
        try {
            Message msg = new Message();
            msg.type = "EnviarLista";
            msg.destino = name;
            msg.chatList = listachats;
            msg.content = content;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("Lista de Usuários da Sala enviada para o JavaSpace");
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
