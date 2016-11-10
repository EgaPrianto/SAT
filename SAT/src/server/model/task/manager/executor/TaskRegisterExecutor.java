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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.packet.Packet;
import server.model.packet.PacketDefaultResponse;
import server.model.packet.PacketRegister;
import server.model.packet.PacketType;
import server.model.packet.SourceType;
import server.view.ConsoleUI;

/**
 *
 * @author Ega Prianto
 */
public class TaskRegisterExecutor extends TaskExecutor {

    public TaskRegisterExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        try {
            PacketRegister receivedPacket = (PacketRegister) packet;
            BufferedWriter bufferedWriter;
            //update status
            System.out.println("Updating status user");
            ChatServerController.dbManager.insertUser(receivedPacket.id, ConsoleUI.idServer, receivedPacket.ipAddressPort, receivedPacket.password, receivedPacket.name, "", "offline");
            if (receivedPacket.sourceType == SourceType.CLIENT) {
                PacketRegister packetPropagation = new PacketRegister(PacketType.REGISTER, this.connectedSockets.size(), SourceType.SERVER, receivedPacket.id, receivedPacket.password, receivedPacket.name);
                packetPropagation.ipAddressPort = receivedPacket.ipAddressPort;
                //Kirim response ke client
                Packet packetResponse = new PacketDefaultResponse(PacketType.DEFAULT_RESPONSE, this.connectedSockets.size(), SourceType.SERVER, "Register Successfull!");
                System.out.println("Sending Register Response : ");
                System.out.println(packetResponse.toString());
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedSockets.get(receivedPacket.ipAddressPort).getOutputStream()));
                bufferedWriter.write(packetResponse.toString());
                bufferedWriter.flush();
                //kirim ke semua server
                System.out.println("Sending propagate Register : ");
                for (Socket connectedServerSocket : connectedServerSockets) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                    System.out.println("Packet to server "+connectedServerSocket.getRemoteSocketAddress().toString()+" : "+packetPropagation.toString());
                    bufferedWriter.write(packetPropagation.toString());
                    bufferedWriter.flush();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskRegisterExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskRegisterExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
