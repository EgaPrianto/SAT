/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.task.manager.executor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketLogout;
import server.model.packet.PacketType;
import server.model.packet.SourceType;
import server.view.ConsoleUI;

/**
 *
 * @author Ega Prianto
 */
public class TaskLogoutExecutor extends TaskExecutor {

    public TaskLogoutExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        try {
            PacketLogout packetLogout = (PacketLogout) this.packet;
            ChatServerController.dbManager.updateStatusUser(packetLogout.id, "offline");
            long curTime = System.currentTimeMillis();
            Date currentDate = new Date(curTime);
            ChatServerController.dbManager.updateLastLogoutUser(packetLogout.id, ChatServerController.dbManager.DATE_FORMAT.format(currentDate));
            BufferedWriter bufferedWriter;
            //broadcast info logout
            System.out.println("Broadcasting notification logout to connected client");
            String ipAddressPort = ChatServerController.dbManager.getIpAddressPortUser(packetLogout.id);
            PacketLogout packetNotifyLogout = new PacketLogout(PacketType.LOGOUT, this.connectedSockets.size(), SourceType.SERVER, packetLogout.id, packetLogout.timestamp);
            for (Socket clientSocketNotification : connectedSockets.values()) {
                if (!clientSocketNotification.getRemoteSocketAddress().toString().substring(1).equals(ipAddressPort)) {
                    System.out.println("notification target client : " + clientSocketNotification.getRemoteSocketAddress().toString());
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocketNotification.getOutputStream()));
                    bufferedWriter.write(packetNotifyLogout.toString());
                    bufferedWriter.flush();
                }
            }
            if (packetLogout.sourceType == SourceType.CLIENT) {
                //propagate server
                Packet packetPropagation = new PacketLogout(PacketType.LOGOUT, this.connectedSockets.size(), SourceType.SERVER, packetLogout.id, packetLogout.timestamp);
                for (Socket connectedServerSocket : connectedServerSockets) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                    System.out.println("Sending to server " + connectedServerSocket.getRemoteSocketAddress().toString() + " :" + packetPropagation.toString());
                    bufferedWriter.write(packetPropagation.toString());
                    bufferedWriter.flush();
                }
            }
            //sql 'update table database set status=offline where id=packetLogout.id'
        } catch (SQLException ex) {
            Logger.getLogger(TaskLogoutExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskLogoutExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
