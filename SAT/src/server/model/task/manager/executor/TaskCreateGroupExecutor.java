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
import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketCreateGroup;

/**
 *
 * @author Ega Prianto
 */
public class TaskCreateGroupExecutor extends TaskExecutor {

    public TaskCreateGroupExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList< Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        BufferedWriter bufferedWriter = null;
        try {
            PacketCreateGroup packetCreateGroup = (PacketCreateGroup) this.packet;
            ChatServerController.dbManager.insertGroup(packetCreateGroup.id_group, packetCreateGroup.nama);
            // TODO
            Socket socket = connectedSockets.get("1321");
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
