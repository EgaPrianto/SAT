/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketFactory;
import server.model.packet.PacketGotOnline;
import server.model.packet.PacketType;
import server.model.packet.SourceType;
import server.view.ConsoleUI;

/**
 *
 * @author Ega Prianto
 */
public class ConnectionManager implements Runnable {

    public static int PORT = 2406;

    public ServerSocket serverSocket;
    public static final int LIMIT_CONNECTION = 10;
    private Thread thread;
    private boolean isFinish;
    private CopyOnWriteArrayList<String> serverList;
    private ConcurrentHashMap<String, Socket> connectedSockets;
    private CopyOnWriteArrayList<Socket> connectedServerSockets;
    private CopyOnWriteArrayList<ConnectionReceiver> connectionReceivers;
    private ConcurrentLinkedQueue<Packet> packetQueue;

    public ConnectionManager(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, ConcurrentLinkedQueue<Packet> packetQueue) throws IOException {
        init();
        this.packetQueue = packetQueue;
        this.connectedServerSockets = connectedServerSockets;
        this.connectedSockets = connectedSockets;
        this.serverSocket = new ServerSocket(PORT);
    }

    public ConnectionManager(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList< Socket> connectedServerSockets, int customPort, ConcurrentLinkedQueue<Packet> packetQueue) throws IOException {
        init();
        this.packetQueue = packetQueue;
        this.connectedServerSockets = connectedServerSockets;
        this.connectedSockets = connectedSockets;
        this.PORT = customPort;
        this.serverSocket = new ServerSocket(customPort);
    }

    private void init() {
        try {
            this.serverList = new CopyOnWriteArrayList<String>();
            this.serverList.addAll(ChatServerController.dbManager.getAllIpAddressServerExcept(ConsoleUI.idServer));
            System.out.println("This is Server List : " + serverList);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.thread = new Thread(this);
        this.isFinish = false;
        this.connectionReceivers = new CopyOnWriteArrayList<>();
    }

    public void createConnection(Socket socket) throws IOException, ClassNotFoundException, SQLException {
        ConnectionReceiver newConnection = new ConnectionReceiver(socket, this.packetQueue);
        this.connectedSockets.put(socket.getRemoteSocketAddress().toString().substring(1), socket);
        this.connectionReceivers.add(newConnection);
        newConnection.start();
    }

    public void createServerConnection(String ipAddress, int port) throws IOException, ClassNotFoundException, SQLException {
        boolean isExist = false;
        for (int i = 0; i < connectedServerSockets.size(); i++) {
            if (connectedServerSockets.get(i).getRemoteSocketAddress().toString().substring(1).equals(ipAddress + ":" + port)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            Socket serverSocket = new Socket(ipAddress, port);
            Packet packet = new PacketGotOnline(PacketType.GOT_ONLINE, this.connectedSockets.size(), SourceType.SERVER, ConsoleUI.idServer, serverSocket.getLocalAddress().toString().substring(1), PORT);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
            System.out.println("Sending packet to server : " + packet.toString());
            bufferedWriter.write(packet.toString());
            bufferedWriter.flush();
            System.out.println("Adding server to socket");
            this.connectedServerSockets.add(serverSocket);//adding server socket connection
        }
    }

    public void createServerConnectionReceiver(Socket socket) throws IOException, ClassNotFoundException, SQLException {
        ConnectionReceiver newConnection = new ConnectionReceiver(socket, this.packetQueue);
        this.connectionReceivers.add(newConnection);
        newConnection.start();
    }

    @Override
    public void run() {
        System.out.println("Listening started on port: " + serverSocket.getLocalPort());
        try {
            for (String string : serverList) {
                String[] splitted = string.split(":");
                int port = Integer.parseInt(splitted[1]);
                try {
                    Socket serverSocket = new Socket(splitted[0], port);
                    Packet packet = new PacketGotOnline(PacketType.GOT_ONLINE, this.connectedSockets.size(), SourceType.SERVER, ConsoleUI.idServer, serverSocket.getLocalAddress().toString().substring(1), PORT);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
                    System.out.println("Sending packet to server : " + packet.toString());
                    bufferedWriter.write(packet.toString());
                    bufferedWriter.flush();
                    System.out.println("Adding server to socket");
                    this.connectedServerSockets.add(serverSocket);//adding server socket connection
                    ConnectionReceiver newConnection = new ConnectionReceiver(serverSocket, this.packetQueue);
                    this.connectionReceivers.add(newConnection);
                    newConnection.start();
                } catch (ConnectException ex) {
                    //not connected do nothing
                }
            }
            while (!isFinish) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection host: " + clientSocket.getRemoteSocketAddress().toString().substring(1));
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String type = br.readLine();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                PacketGotOnline packetGotOnline = (PacketGotOnline) PacketFactory.createPacketFromString(type);
                if (packetGotOnline.sourceType == SourceType.CLIENT) {
                    //client
                    if (this.connectedSockets.size() <= LIMIT_CONNECTION) {
                        bw.write("Welcome to this server \n");
                        bw.write("Connection Started\n");
                        bw.flush();
                        createConnection(clientSocket);
                    } else {
                        //TODO Masih belum beres
                        bw.write("Sorry, the server is full please try another server \n");
                        bw.write(this.serverList.get(0) + "\n");
                        bw.flush();
                    }
                } else if (packetGotOnline.sourceType == SourceType.SERVER) {
                    //server
                    System.out.println("Server Connected " + packetGotOnline.id + " at " + packetGotOnline.ipAddress + " : " + packetGotOnline.toString());
//                    createServerConnection(packetGotOnline.ipAddress, packetGotOnline.port);
                    System.out.println("Adding server to socket");
                    this.connectedServerSockets.add(clientSocket);//adding server socket connection
                    createServerConnectionReceiver(clientSocket);
//                    ChatServerController.dbManager.insertServer(packetGotOnline.id, packetGotOnline.ipAddress, packetGotOnline.port);
                } else {
                    clientSocket.close();
                    //command wrong close connection
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        this.thread.start();
        this.isFinish = false;
    }

    public void stop() throws InterruptedException {
        this.isFinish = true;
        this.thread.join();
    }

}
