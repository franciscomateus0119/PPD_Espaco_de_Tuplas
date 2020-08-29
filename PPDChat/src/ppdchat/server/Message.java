package ppdchat.server;
import ppdchat.client.*;
import ppdchat.client.ClientForm;
import net.jini.core.entry.Entry;
public class Message implements Entry {
    public ClientForm clientForm;
    public String destination;
    public String type;
    public String chatname;
    public String name;
    public String content;
    public Message() {
    }
}