/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdchat;
//import ppdchat.server.ServerInterface;
import ppdchat.server.Server;
import ppdchat.server.Server;
//import ppdchat.server.ServerInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Matheus
 */
public class InitServer extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {}
    public static void main(String args[]){
        System.out.println("Starting...");
        try {
            Server server = new Server();
            System.out.println("Server Started Sucessfully!");
            
        } catch (Exception e) {
            System.out.println("Failed Server Initialization!");
            e.printStackTrace();
        }
    }
}
