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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketChatSend;
import server.model.packet.PacketFactory;
import server.model.packet.PacketGetChatRequest;
import server.model.packet.PacketGetChatResponse;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author Ega Prianto
 */
public class TaskGetChatExecutor extends TaskExecutor {

    public TaskGetChatExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        PacketGetChatRequest receivedPacket = (PacketGetChatRequest) packet;
        try {
            List<String> chats = ChatServerController.dbManager.getChats(receivedPacket.chatType, receivedPacket.id_pengirim, receivedPacket.id_penerima, receivedPacket.timestamp);
            List<PacketChatSend> chatSends = new ArrayList<>();
            for (String chat : chats) {
                chatSends.add((PacketChatSend) PacketFactory.createPacketFromString(chat));
            }
            PacketGetChatResponse toSend = new PacketGetChatResponse(PacketType.GET_CHAT_RESPONSE, this.connectedSockets.size(), SourceType.SERVER, chatSends);
            Socket socket = connectedSockets.get(receivedPacket.ipAddressPort);
            System.out.print("Sending packet GetChatsResponse : " + toSend.toString());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(toSend.toString());
            bufferedWriter.flush();
            this.thread.join();
        } catch (SQLException ex) {
            Logger.getLogger(TaskGetChatExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskGetChatExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TaskGetChatExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
