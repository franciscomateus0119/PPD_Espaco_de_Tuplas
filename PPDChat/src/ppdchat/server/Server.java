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
    int x = 0;
    private boolean primeirasala = true;

    //protected ArrayList<ClientForm> clients;
    //protected Map<String, ClientForm> clientbyname = new HashMap<>();
    protected Map<String, String> clientchat = new HashMap<>();
    protected Map<String, ArrayList<String>> usersinchat = new HashMap<>();
    //protected ArrayList<String> names;
    //protected ArrayList<String> chatnames;
    //public Map<Integer, String> names = new HashMap<>();

    Lookup finder;
    JavaSpace space;

    public Server() throws RemoteException {
        //names = new ArrayList<>();
        //chatnames = new ArrayList<>();
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
        ArrayList<String> firstarray = new ArrayList<>();
        writeUserList(firstarray);
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
                            
                            Message userTemp = new Message();
                            userTemp.type = "ListaUsuarios";
                            userTemp.destino = "Espaco";
                            Message userList = (Message) space.read(userTemp, null, 60 * 1000);
                            if(userList!=null){
                                ArrayList<String> newUserArray = new ArrayList<>();
                                newUserArray = userList.userInChatList;
                                x = 0;
                                while (x < newUserArray.size()) {
                                    writeMessage(msg.name + ": ", msg.chatname, msg.content, newUserArray.get(x));
                                    x = x + 1;
                                }
                            }
                            //msg.namesList = listanomes;
                            
                            break;
                        case "NewChat":
                            System.out.println("NewChat Acionado! Pedido para criar a sala: " + msg.chatname);
                            //Procurar por lista de salas
                            /*
                            if(!primeirasala){
                                ArrayList<String> novoArray = new ArrayList<>();
                                writeNewChat(msg.name, novoArray, "Espaco");
                            }
                            */
                            Message templ = new Message();
                            templ.destino = "Espaco";
                            templ.type = "ListaChat";
                            Message listachat = (Message) space.take(templ, null, 60 * 1000);
                            //Se a lista não existe ->Crie a primeira sala, primeiro elemento da lista de salas e informe a todos
                            if (listachat == null) {
                                ArrayList<String> newarray = new ArrayList<>();
                                newarray.add(msg.chatname);
                                //Cria o chat e poe no espaço
                                writeChat(msg.chatname,"Espaco");
                                //writeNewChat(msg.name, newarray, "Espaco");
                                //Cria a lista de salas
                                writeListaChat(newarray, "Espaco");
                                //Informa aos usuarios a criação da nova sala
                                Message userTemp2 = new Message();
                                userTemp2.type = "ListaUsuarios";
                                userTemp2.destino = "Espaco";
                                Message userList2 = (Message) space.read(userTemp2, null, 60 * 1000);
                                if(userList2!=null){
                                    ArrayList<String> newUserArray2 = new ArrayList<>();
                                    newUserArray2 = userList2.userInChatList;
                                    x = 0;
                                    while (x < newUserArray2.size()) {
                                        writeNewChat(msg.name, msg.chatname, newarray, newUserArray2.get(x));
                                        x = x + 1;
                                    }
                                }
                                
                                msg.servidorLeu = true;
                                System.out.println("Primeira Sala e Lista de Salas criados!");
                                
                            } //Se a lista existe
                            else {
                                ArrayList<String> newarray = new ArrayList<>();
                                newarray = listachat.chatList;
                                if (!newarray.contains(msg.chatname)) {
                                    newarray.add(msg.chatname);
                                    //System.out.println("Novo Chat criado: " + msg.chatname);
                                    writeChat(msg.chatname, "Espaco");
                                    System.out.println("Sala " + msg.chatname + " criada!");
                                    msg.servidorLeu = true;
                                    Message userTemp3 = new Message();
                                    userTemp3.type = "ListaUsuarios";
                                    userTemp3.destino = "Espaco";
                                    Message userList3 = (Message) space.read(userTemp3, null, 60 * 1000);
                                    if (userList3 != null) {
                                        ArrayList<String> newUserArray3 = new ArrayList<>();
                                        newUserArray3 = userList3.userInChatList;
                                        x = 0;
                                        while (x < newUserArray3.size()) {
                                            writeNewChat(msg.name, msg.chatname, newarray, newUserArray3.get(x));
                                            x = x + 1;
                                        }

                                        writeListaChat(newarray, "Espaco");
                                        System.out.println("Sala " + msg.chatname + " adicionada à lista de salas");
                                    }
                                }

                            }

                            break;
                        case "NewUserChat":
                            //Procurar por lista de salas
                            Message temp4 = new Message();
                            temp4.destino = "Espaco";
                            temp4.type = "ListaChat";
                            Message listadechat = (Message) space.take(temp4, null, 60 * 1000);
                            //Se a lista não existe
                            if (listadechat == null) {

                                ArrayList<String> newarray = new ArrayList<>();
                                newarray.add(msg.name + msg.chatname);
                                writeListaChat(newarray, "Espaco");
                            } //Se a lista existe
                            else {
                                ArrayList<String> newarray = new ArrayList<>();
                                newarray = listadechat.chatList;
                                if (!newarray.contains(msg.name + msg.chatname)) {
                                    System.out.println("Novo Chat criado: " + msg.name + msg.chatname);
                                    writeChat(msg.name + msg.chatname, "Espaco");
                                    msg.servidorLeu = true;
                                    Message userTemp4 = new Message();
                                    userTemp4.type = "ListaUsuarios";
                                    userTemp4.destino = "Espaco";
                                    Message userList4 = (Message) space.read(userTemp4, null, 60 * 1000);
                                    if (userList4 != null) {
                                        ArrayList<String> newUserArray4 = new ArrayList<>();
                                        newUserArray4 = userList4.userInChatList;
                                        x = 0;
                                        while (x < newUserArray4.size()) {
                                            if (msg.name.equals(newUserArray4.get(x))) {
                                                writeNewChat(msg.chatname, msg.name + msg.chatname, newarray, newUserArray4.get(x));
                                            }
                                            if (msg.chatname.equals(newUserArray4.get(x))) {
                                                writeNewChat(msg.name, msg.name + msg.chatname, newarray, newUserArray4.get(x));
                                            }
                                            x = x + 1;
                                        }
                                        newarray.add(msg.name + msg.chatname);
                                        writeListaChat(newarray, "Espaco");
                                    }
                                }
                            }

                            break;
                        case "NewClient":
                            if (msg.name == null) {
                                System.out.println("O NOME É NULOOOOOOOOOO");
                            }
                            Message userTemp5 = new Message();
                            userTemp5.type = "ListaUsuarios";
                            userTemp5.destino = "Espaco";
                            Message userList5 = (Message) space.take(userTemp5, null, 60 * 1000);
                            if (userList5 != null) {
                                ArrayList<String> newUserArray5 = new ArrayList<>();
                                //newUserArray5 = userList5.userInChatList;
                                if (userList5.userInChatList == null) {
                                    if (msg.name != null && !msg.name.equals("")) {
                                        newUserArray5.add(msg.name);
                                        System.out.println("Novo cliente adicionado: " + msg.name);
                                        userList5.userInChatList = newUserArray5;
                                        System.out.println("Total de clientes: " + userList5.userInChatList.size());
                                        Message temp = new Message();
                                        temp.destino = "Espaco";
                                        temp.type = "ListaUsuarios";
                                        space.take(temp, null, 15 * 1000);
                                        writeUserList(userList5.userInChatList);
                                    }
                                }
                                else {
                                    if (!userList5.userInChatList.contains(msg.name) && msg.name != null && !msg.name.equals("")) {
                                        System.out.println("Novo cliente adicionado: " + msg.name);
                                        userList5.userInChatList.add(msg.name);
                                        System.out.println("Total de clientes: " + userList5.userInChatList.size());
                                        Message temp = new Message();
                                        temp.destino = "Espaco";
                                        temp.type = "ListaUsuarios";
                                        space.take(temp, null, 15 * 1000);
                                        writeUserList(userList5.userInChatList);
                                    }
                                }
                            }
                            break;
                        case "EntrarRequest":
                            System.out.println("Pedido de " + msg.name + " para sair de " + msg.content + " para " + msg.chatname);
                            //Origem não é nula  >Procura se a sala origem existe
                            if (msg.content != null && !msg.content.equals("") && !primeirasala) {
                                Message temp2 = new Message();
                                temp2.destino = "Espaco";
                                temp2.type = "Chat";
                                temp2.chatname = msg.content; //Nome da sala não nulo
                                Message sairchat = (Message) space.take(temp2, null, 10 * 1000);
                                //Se a sala existir -> Remova seu nome da sala origem!
                                if (sairchat != null) {
                                    ArrayList<String> newlist = sairchat.userInChatList;
                                    newlist.remove(msg.name);
                                    sairchat.userInChatList = newlist;
                                    System.out.println("Saiu de" + msg.content);
                                    space.write(sairchat, null, 180 * 1000);
                                    

                                }

                            }
                            //Origem é nula
                            else if((msg.content == null || !msg.content.equals(""))){ }
                            

                            //Procurar a Sala que se deseja entrar no Espaço
                            Message temp = new Message();
                            temp.destino = "Espaco";
                            temp.type = "Chat";
                            temp.chatname = msg.chatname;
                            Message chatmsg = (Message) space.take(temp, null, 60 * 1000);
                            //Sala destino é nula!
                            if (chatmsg == null) {
                                System.out.println("A sala" + msg.chatname + " NÃO foi encontrada!");
                                writeEnterRequestResult(msg.name, msg.chatname, "Falhou");
                                //Sala destino não é nula!
                            } else {
                                System.out.println("A sala" + msg.chatname + " FOI encontrada!");
                                if (chatmsg.userInChatList == null) {
                                    ArrayList<String> novalista = new ArrayList<>();
                                    novalista.add(msg.name);
                                    chatmsg.userInChatList = novalista;//Adiciona o usuário na lista de usuários da sala.
                                } else {
                                    ArrayList<String> novalista = chatmsg.userInChatList;
                                    novalista.add(msg.name);
                                    chatmsg.userInChatList = novalista;//Adiciona o usuário na lista de usuários da sala.
                                }

                                try {
                                    space.write(chatmsg, null, 180 * 1000);
                                    writeEnterRequestResult(msg.name, msg.chatname, "Sucesso");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case "AtualizarListaSala":
                            Message temp3 = new Message();
                            temp3.destino = "Espaco";
                            temp3.type = "ListaChat";
                            Message listChat = (Message) space.read(temp3, null, 60 * 1000);
                            if (listChat != null) {
                                ArrayList<String> listadenomes = new ArrayList<>();
                                listadenomes = listChat.chatList;
                                writeAtualizarListaSala(msg.name, listadenomes);
                            }

                            break;
                        case "AtualizarListaUser":
                            Message temp5 = new Message();
                            temp5.destino = "Espaco";
                            temp5.type = "Chat";
                            temp5.chatname = msg.chatname;
                            Message selectedChat = (Message) space.read(temp5, null, 60 * 1000);
                            //Verifica se o Chat ainda existe
                            if (selectedChat != null) {
                                ArrayList<String> listadenomes = new ArrayList<>();
                                listadenomes = selectedChat.userInChatList;
                                writeAtualizarListaUser(msg.name, listadenomes);
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
    //Informar que um novoChat foi criado!
    public void writeNewChat(String name, String chatname, ArrayList<String> listasalas, String destino) {
        try {
            Message msg = new Message();
            msg.type = "NewChat";
            msg.destino = destino;
            msg.name = name;
            msg.chatname = chatname;
            msg.chatList = listasalas;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("(writeNewChat)NewCHAT " +chatname +" enviado para: " + destino);
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
    //Botar o Chat no Espaço
    public void writeChat(String chatname, String destino) {
        try {
            Message msg = new Message();
            msg.type = "Chat";
            msg.destino = destino;
            msg.chatname = chatname;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 180 * 1000);
                    System.out.println("(writeChat)CHAT" + chatname+" enviado para: " + destino);
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

    public void writeUserList(ArrayList<String> listanomes) {
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

    public void writeEnterRequestResult(String name, String chatname, String result) {
        try {
            Message msg = new Message();
            msg.type = "EnterRequestResult";
            msg.destino = name;
            msg.chatname = chatname;
            msg.content = result;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("(writeEnterRequestResult) Lista de Usuários da Sala enviada para o JavaSpace");
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

    public void writeAtualizarListaSala(String name, ArrayList<String> nomedassalas) {
        try {
            Message msg = new Message();
            msg.type = "AtualizarListaSala";
            msg.destino = name;
            msg.chatList = nomedassalas;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("(AtualizarListaSala)Lista de Salas enviada para o JavaSpace");
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
    public void writeAtualizarListaUser(String name, ArrayList<String> nomesdosusuarios) {
        try {
            Message msg = new Message();
            msg.type = "AtualizarListaUser";
            msg.destino = name;
            msg.userInChatList = nomesdosusuarios;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("(AtualizarListaUser) Lista de Usuários da Sala enviada para o JavaSpace");
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

    public void writeListaChat(ArrayList<String> arrayList, String destino) {
        try {
            Message msg = new Message();
            msg.type = "ListaChat";
            msg.destino = destino;
            msg.chatList = arrayList;
            Platform.runLater(() -> {
                try {
                    space.write(msg, null, 60 * 1000);
                    System.out.println("(writeListaChat)Lista de Salas enviada para o JavaSpace");
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
