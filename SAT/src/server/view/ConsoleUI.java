/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.view;

import server.controller.ChatServerController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.db.JDBCMySQLManager;
import server.model.packet.PacketType;

/**
 *
 * @author Ega Prianto
 */
public class ConsoleUI {
    public static String idServer;
    public static void main(String[] args) throws IOException {

        try {
            //        ServerSocket serverSocket = new ServerSocket(2013);
//        while (true) {
//            Socket sock = serverSocket.accept();
//            System.out.println("connect");
//            System.out.println(sock.getInetAddress().toString().substring(1));
//            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//            br.ready();
//        }

            BufferedReader br = new BufferedReader( 
                    new FileReader(new File("server1.properties")));
            String url = br.readLine();
            idServer = br.readLine();
            System.out.println(url + "\n"+idServer);
            JDBCMySQLManager dbManager = new JDBCMySQLManager(url);
            int port = Integer.parseInt(br.readLine());
            System.out.println("Chat Server is Started at : " + port);
            ChatServerController chatServerController = new ChatServerController(dbManager);
            chatServerController.startServer(port);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
