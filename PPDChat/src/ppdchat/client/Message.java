package ppdchat.client;
import ppdchat.client.*;
import ppdchat.client.Client;
import net.jini.core.entry.Entry;
public class Message implements Entry {
    public Client client;
    public String type;
    public String chatname;
    public String name;
    public String content;
    public Message() {
    }
}