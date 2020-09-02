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
    private boolean primeirasala = true;

    //protected ArrayList<ClientForm> clients;
    //protected Map<String, ClientForm> clientbyname = new HashMap<>();
    protected Map<String, String> clientchat = new HashMap<>();
    protected Map<String, ArrayList<String>> usersinchat = new HashMap<>();
    protected ArrayList<String> names;
    //protected ArrayList<String> chatnames;
    //public Map<Integer, String> names = new HashMap<>();

    Lookup finder;
    JavaSpace space;

    public Server() throws RemoteException {
        //clients = new ArrayList<>();
        names = new ArrayList<>();
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
                            Message listachat = (Message) space.take(templ, null, 20 * 1000);
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
                                int x = 0;
                                while (x < names.size()) {
                                    writeNewChat(msg.name, msg.chatname, newarray, names.get(x));
                                    x = x + 1;
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
                                    x = 0;
                                    while (x < names.size()) {
                                        writeNewChat(msg.name,msg.chatname, newarray, names.get(x));
                                        x = x + 1;
                                    }
                                    
                                    writeListaChat(newarray, "Espaco");
                                    System.out.println("Sala " + msg.chatname + " adicionada à lista de salas");
                                }

                            }

                            break;
                        case "NewUserChat":
                            //Procurar por lista de salas
                            Message temp4 = new Message();
                            temp4.destino = "Espaco";
                            temp4.type = "ListaChat";
                            Message listadechat = (Message) space.take(temp4, null, 20 * 1000);
                            //Se a lista não existe
                            if (listadechat == null) {

                                ArrayList<String> newarray = new ArrayList<>();
                                newarray.add("(P)"+msg.name + msg.chatname);
                                writeListaChat(newarray, "Espaco");
                            } //Se a lista existe
                            else {
                                ArrayList<String> newarray = new ArrayList<>();
                                newarray = listadechat.chatList;
                                if (!newarray.contains(msg.name + msg.chatname)) {
                                    System.out.println("Novo Chat criado: " + msg.name + msg.chatname);
                                    writeChat("(P)"+msg.name + msg.chatname, "Espaco");
                                    msg.servidorLeu = true;
                                    x = 0;
                                    while (x < names.size()) {
                                        if(msg.name.equals(names.get(x))){
                                            writeNewChat(msg.chatname, "(P)"+msg.name + msg.chatname,newarray, names.get(x));
                                        }
                                        if(msg.chatname.equals(names.get(x))){
                                            writeNewChat(msg.name, "(P)"+msg.name + msg.chatname,newarray, names.get(x));
                                        }
                                        x = x + 1;
                                    }
                                    newarray.add("(P)"+msg.name + msg.chatname);
                                    writeListaChat(newarray, "Espaco");
                                }

                            }

                            break;
                        case "NewClient":
                            if (msg.name == null) {
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
                            //Origem não é nula  >Procura se a sala origem existe
                            if (msg.content != null && !msg.content.equals("")) {
                                Message temp2 = new Message();
                                temp2.destino = "Espaco";
                                temp2.type = "Chat";
                                temp2.chatname = msg.content; //Nome da sala não nulo
                                Message sairchat = (Message) space.take(temp2, null, 20 * 1000);
                                //Se a sala existir -> Remova seu nome da sala origem!
                                
                                if (sairchat != null) {
                                    System.out.println(msg.content+ " Encontrada! Saindo dela...");
                                    ArrayList<String> newlist = sairchat.userInChatList;
                                    newlist.remove(msg.name);
                                    sairchat.userInChatList = newlist;
                                    System.out.println("Saiu de" + msg.content);
                                    space.write(sairchat, null, 180 * 1000);
                                }

                            }
                            //Origem é nula e não é a primeira sala
                            //Procurar a Sala que se deseja entrar no Espaço
                            Message temp = new Message();
                            temp.destino = "Espaco";
                            temp.type = "Chat";
                            temp.chatname = msg.chatname;
                            Message chatmsg = (Message) space.take(temp, null, 20 * 1000);
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
                            Message listChat = (Message) space.read(temp3, null, 20 * 1000);
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
                            Message selectedChat = (Message) space.read(temp5, null, 20 * 1000);
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

    
}
