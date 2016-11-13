/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.task.manager.executor;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketGetChatRequest;

/**
 *
 * @author Ega Prianto
 */
public class TaskGetChatExecutor extends TaskExecutor{

    public TaskGetChatExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        PacketGetChatRequest packetGetChatRequest = (PacketGetChatRequest) packet;
//        ChatServerController.dbManager.getChats();
    }
    
}
