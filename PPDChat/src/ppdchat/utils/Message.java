package ppdchat.utils;
import java.util.ArrayList;
import ppdchat.server.*;
import ppdchat.client.*;
import net.jini.core.entry.Entry;
public class Message implements Entry {
    public String destino;
    public String type;
    public String chatname;
    public String name;
    public String content;
    public Boolean servidorLeu = false;
    public Boolean destinatarioLeu = false;
    //public ArrayList<String> quemLeu = new ArrayList<>();
    public ArrayList<String> namesList;
    public Message() {
    }
}