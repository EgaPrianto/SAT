/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.packet.Packet;
import server.model.packet.PacketFactory;
import server.model.packet.PacketGetChatRequest;
import server.model.packet.PacketGetGroupRequest;
import server.model.packet.PacketGetOnlineRequest;
import server.model.packet.PacketLoginRequest;
import server.model.packet.PacketLoginServer;
import server.model.packet.PacketRegister;

/**
 *
 * @author Ega Prianto
 */
public class ConnectionReceiver implements Runnable {

    private Socket socket;
    private Thread thread;
    private BufferedReader inputReader;
    private boolean isFinish;
    private ConcurrentLinkedQueue<Packet> packetQueue;

    public ConnectionReceiver(Socket socket, ConcurrentLinkedQueue<Packet> packetQueue) throws IOException, ClassNotFoundException, SQLException {
        this.socket = socket;
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.thread = new Thread(this);
        this.packetQueue = packetQueue;
    }

    @Override
    public void run() {
        while (!isFinish) {
            try {
                String input = inputReader.readLine();
                Packet packet = PacketFactory.createPacketFromString(input);

                System.out.println("Packet Builded :" + packet.toString());
                switch (packet.command) {
                    case LOGIN_REQUEST:
                        if (packet instanceof PacketLoginRequest) {
                            PacketLoginRequest packetLoginClient = (PacketLoginRequest) packet;
                            packetLoginClient.ipAddressPort = this.socket.getRemoteSocketAddress().toString().substring(1);
                            packetQueue.add(packetLoginClient);
                        }
                        break;
                    case REGISTER:
                        if (packet instanceof PacketRegister) {
                            PacketRegister packetRegister = (PacketRegister) packet;
                            packetRegister.ipAddressPort = this.socket.getRemoteSocketAddress().toString().substring(1);
                            packetQueue.add(packetRegister);
                        }
                        break;
                    case GET_ONLINE_REQUEST:
                        if (packet instanceof PacketGetOnlineRequest) {
                            PacketGetOnlineRequest packetGetOnlineClient=  (PacketGetOnlineRequest) packet;
                            packetGetOnlineClient.ipAddressPort = this.socket.getRemoteSocketAddress().toString().substring(1);
                            packetQueue.add(packetGetOnlineClient);
                        }
                        break;
                    case LOGIN_SERVER:
                        if (packet instanceof PacketLoginServer) {
                            PacketLoginServer packetLoginServer=  (PacketLoginServer) packet;
                            packetQueue.add(packetLoginServer);
                        }
                        break;
                    case GET_GROUP_REQUEST:
                        if (packet instanceof PacketGetGroupRequest) {
                            PacketGetGroupRequest packetGetGroupRequest=  (PacketGetGroupRequest) packet;
                            packetGetGroupRequest.ipAddressPort = this.socket.getRemoteSocketAddress().toString().substring(1);
                            packetQueue.add(packetGetGroupRequest);
                        }
                        break;           
                    case GET_CHAT_REQUEST:
                        if (packet instanceof PacketGetChatRequest) {
                            PacketGetChatRequest packetGetChatRequest=  (PacketGetChatRequest) packet;
                            packetGetChatRequest.ipAddressPort = this.socket.getRemoteSocketAddress().toString().substring(1);
                            packetQueue.add(packetGetChatRequest);
                        }
                        break;           
                    default:
                        packetQueue.add(packet);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionReceiver.class.getName()).log(Level.SEVERE, null, ex);
                ConnectionManager.connectionReceivers.remove(socket);
            } 
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
