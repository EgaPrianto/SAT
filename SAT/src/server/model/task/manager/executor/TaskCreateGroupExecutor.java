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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketCreateGroup;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

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
            PacketCreateGroup receivedPacket = (PacketCreateGroup) this.packet;
            PacketCreateGroup sendPacket = new PacketCreateGroup(PacketType.CREATE_GROUP,
                    this.connectedSockets.size(),
                    SourceType.SERVER,
                    receivedPacket.id_group,
                    receivedPacket.nama,
                    receivedPacket.idCreator,
                    receivedPacket.listIDMember);
            ChatServerController.dbManager.insertGroup(receivedPacket.id_group, receivedPacket.nama);
            List<String> listIdMember = receivedPacket.listIDMember;
            ChatServerController.dbManager.insertUserToGroup(receivedPacket.id_group, receivedPacket.idCreator, "bakalKey");
            for (String string : listIdMember) {
                ChatServerController.dbManager.insertUserToGroup(receivedPacket.id_group, string, "");
            }
            System.out.println("Sending notification to client Create Group : ");
            //kirim ke creator
            String ipAddressPort = ChatServerController.dbManager.getIpAddressPortUser(receivedPacket.idCreator);
            System.out.println("Current Ip addressPort = " + ipAddressPort + " " + this.connectedSockets.containsKey(ipAddressPort));
            if (this.connectedSockets.containsKey(ipAddressPort)) {
                Socket clientSocket = this.connectedSockets.get(ipAddressPort);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                System.out.println("Packet to Client " + clientSocket.getRemoteSocketAddress().toString() + " Create Group: " + clientSocket.toString());
                bufferedWriter.write(sendPacket.toString());
                bufferedWriter.flush();
            }
            //kasih tau ke semua id yang lagi terhubung kalo bikin group
            for (String string : listIdMember) {
                 ipAddressPort = ChatServerController.dbManager.getIpAddressPortUser(string);
                System.out.println("Current Ip addressPort = " + ipAddressPort + " " + this.connectedSockets.containsKey(ipAddressPort));
                if (this.connectedSockets.containsKey(ipAddressPort)) {
                    Socket clientSocket = this.connectedSockets.get(ipAddressPort);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    System.out.println("Packet to Client " + clientSocket.getRemoteSocketAddress().toString() + " Create Group: " + clientSocket.toString());
                    bufferedWriter.write(sendPacket.toString());
                    bufferedWriter.flush();
                }
            }

            if (receivedPacket.sourceType == SourceType.CLIENT) {
                //kirim ke semua server
                PacketCreateGroup packetPropagation = new PacketCreateGroup(PacketType.CREATE_GROUP,
                        this.connectedSockets.size(),
                        SourceType.SERVER, receivedPacket.id_group,
                        receivedPacket.nama, receivedPacket.idCreator,
                        receivedPacket.listIDMember);
                System.out.println("Sending propagate Create Group : ");
                for (Socket connectedServerSocket : connectedServerSockets) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                    System.out.println("Packet to server " + connectedServerSocket.getRemoteSocketAddress().toString() + " : " + packetPropagation.toString());
                    bufferedWriter.write(packetPropagation.toString());
                    bufferedWriter.flush();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TaskCreateGroupExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
