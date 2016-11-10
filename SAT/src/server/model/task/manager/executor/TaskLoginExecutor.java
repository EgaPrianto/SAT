/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.task.manager.executor;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.db.JDBCMySQLManager;
import server.model.packet.LoginResponseType;
import server.model.packet.Packet;
import server.model.packet.PacketLoginClient;
import server.model.packet.PacketLoginResponse;
import server.model.packet.PacketLoginServer;
import server.model.packet.PacketType;
import server.model.packet.SourceType;
import server.view.ConsoleUI;

/**
 *
 * @author Ega Prianto
 */
public class TaskLoginExecutor extends TaskExecutor {

    public TaskLoginExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        try {
            BufferedWriter bufferedWriter;
            if (this.packet instanceof PacketLoginClient) {
                PacketLoginClient packetLogin = (PacketLoginClient) this.packet;
                String userPassword = ChatServerController.dbManager.getPasswordUser(packetLogin.id);
                Socket clientSocket = connectedSockets.get(packetLogin.ipAddressPort);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                PacketLoginResponse loginResponse;
                if ((userPassword != null || !userPassword.equals("")) && packetLogin.password.equals(userPassword)) {
                    String username = ChatServerController.dbManager.getUsernameUser(packetLogin.id);
                    ChatServerController.dbManager.updateIpAddressUser(packetLogin.id, packetLogin.ipAddressPort);
                    ChatServerController.dbManager.updateStatusUser(packetLogin.id, "online");
                    ChatServerController.dbManager.updateServerUser(packetLogin.id, ConsoleUI.idServer);
                    loginResponse = new PacketLoginResponse(PacketType.LOGIN_RESPONSE, connectedSockets.size(), SourceType.SERVER, LoginResponseType.PROCEED, username);
                    System.out.println("Client Requested login : " + packetLogin.ipAddressPort);
                    System.out.println("Sending login response : " + loginResponse.toString());
                    bufferedWriter.write(loginResponse.toString());
                    bufferedWriter.flush();
                    PacketLoginServer packetPropagateServer = new PacketLoginServer(PacketType.LOGIN_SERVER, this.connectedSockets.size(), SourceType.SERVER, packetLogin.id, packetLogin.ipAddressPort, ConsoleUI.idServer);
                    //broadcast info login
                    System.out.println("Broadcasting notification to connected client");
                    for (Socket clientSocketNotification : connectedSockets.values()) {
                        if (!clientSocketNotification.getRemoteSocketAddress().toString().substring(1).equals(packetLogin.ipAddressPort)) {
                            System.out.println("notification target client : " + clientSocketNotification.getRemoteSocketAddress().toString());
                            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocketNotification.getOutputStream()));
                            bufferedWriter.write(packetPropagateServer.toString());
                            bufferedWriter.flush();
                        }
                    }
                    System.out.println("Broadcasting notification to connected server");
                    //propagate server
                    for (Socket connectedServerSocket : connectedServerSockets) {
                        System.out.println("notification target server : " + connectedServerSocket.getRemoteSocketAddress().toString());
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                        bufferedWriter.write(packetPropagateServer.toString());
                        bufferedWriter.flush();
                    }
                    //berhasil login
                    // sql update table set status=online where id=packetLogin.id
                } else {
                    loginResponse = new PacketLoginResponse(PacketType.LOGIN_RESPONSE, connectedSockets.size(), SourceType.SERVER, LoginResponseType.FORBIDDEN, null);
                    bufferedWriter.write(loginResponse.toString());
                    bufferedWriter.flush();

                }
            } else if (this.packet instanceof PacketLoginServer) {
                PacketLoginServer packet = (PacketLoginServer) this.packet;
                ChatServerController.dbManager.updateIpAddressUser(packet.id, packet.ipAddressPort);
                ChatServerController.dbManager.updateStatusUser(packet.id, "online");
                ChatServerController.dbManager.updateServerUser(packet.id, packet.idServer);
                System.out.println("Broadcasting notification to connected client");
                //broadcast info login
                for (Socket clientSocketNotification : connectedSockets.values()) {
                    System.out.println("notification target client : " + clientSocketNotification.getRemoteSocketAddress().toString());
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocketNotification.getOutputStream()));
                    bufferedWriter.write(this.packet.toString());
                    bufferedWriter.flush();
                }
            } else {
                //Packet not for this thread
                //do nothing
            }
            this.thread.join();
        } catch (SQLException ex) {
            Logger.getLogger(TaskLoginExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskLoginExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TaskLoginExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
