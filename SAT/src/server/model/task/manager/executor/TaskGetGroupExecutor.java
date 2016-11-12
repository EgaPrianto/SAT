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
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.db.JDBCMySQLManager;
import server.model.packet.Packet;
import server.model.packet.PacketGetGroupRequest;
import server.model.packet.PacketGetGroupResponse;
import server.model.packet.PacketGetOnlineRequest;
import server.model.packet.PacketGetOnlineResponse;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author Ega Prianto
 */
public class TaskGetGroupExecutor extends TaskExecutor {

    public TaskGetGroupExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        try {
            PacketGetGroupRequest receivedPacket = (PacketGetGroupRequest) packet;
            ArrayList<String> ids = ChatServerController.dbManager.getGroups(receivedPacket.id);
            PacketGetGroupResponse toSend = new PacketGetGroupResponse(PacketType.GET_GROUP_RESPONSE, connectedSockets.size(), SourceType.SERVER, ids);
            Socket socket = connectedSockets.get(receivedPacket.ipAddressPort);
            System.out.print("Sending packet Get Group: " + toSend.toString());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(toSend.toString());
            bufferedWriter.flush();
            this.thread.join();
        } catch (SQLException ex) {
            Logger.getLogger(TaskGetGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskGetGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TaskGetGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
