/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.task.manager.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ChatServerController;
import server.model.db.JDBCMySQLManager;
import server.model.packet.ChatType;
import server.model.packet.Packet;
import server.model.packet.PacketChatSend;
import server.model.packet.SourceType;

/**
 *
 * @author Ega Prianto
 */
public class TaskChatSendExecutor extends TaskExecutor {

    public TaskChatSendExecutor(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList<Socket> connectedServerSockets, Packet packet) {
        super(connectedSockets, connectedServerSockets, packet);
    }

    @Override
    public void run() {
        try {

            System.out.println("Count Server connected " + connectedServerSockets.size());
            PacketChatSend receivedPacket = (PacketChatSend) this.packet;
            BufferedWriter bufferedWriter;
            PacketChatSend chatSend = new PacketChatSend(receivedPacket.command,
                    this.connectedSockets.size(),
                    SourceType.SERVER,
                    receivedPacket.chatType,
                    receivedPacket.idPengirim,
                    receivedPacket.idPenerima,
                    receivedPacket.chat,
                    receivedPacket.timestamp
            );
            if (receivedPacket.chatType == ChatType.GROUP) {
                System.out.println("Inserting Chat to GroupChat Database");
                ChatServerController.dbManager.insertChatGroup(receivedPacket.idPengirim, receivedPacket.idPenerima, receivedPacket.chat, receivedPacket.timestamp);
                if (receivedPacket.sourceType == SourceType.CLIENT) {
                    //kirim ke semua server
                    for (Socket connectedServerSocket : connectedServerSockets) {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                        bufferedWriter.write(chatSend.toString());
                        bufferedWriter.flush();
                    }
                }
                //mengambil anggota-anggota group;
                List<String> memberIds = ChatServerController.dbManager.getAllMemberIdGroup(receivedPacket.idPenerima);
                //kirim ke anggota-anggota yang lg connect
                for (String memberId : memberIds) {
                    String ipAddressPort = ChatServerController.dbManager.getIpAddressPortUser(memberId);
                    if (!memberId.equals(receivedPacket.idPengirim) || this.connectedSockets.contains(ipAddressPort)) {
                        Socket clientSocket = this.connectedSockets.get(ipAddressPort);
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        bufferedWriter.write(chatSend.toString());
                        bufferedWriter.flush();
                    }
                }
            } else {
                System.out.println("Inserting Chat to UserChat Database");
                ChatServerController.dbManager.insertChat(receivedPacket.idPengirim, receivedPacket.idPenerima, receivedPacket.chat, receivedPacket.timestamp);
                int a;
                if (receivedPacket.chatType == ChatType.BROADCAST) {
                    if (receivedPacket.sourceType == SourceType.CLIENT) {
                        //kirim ke semua server
                        for (Socket connectedServerSocket : connectedServerSockets) {
                            bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                            bufferedWriter.write(chatSend.toString());
                            bufferedWriter.flush();
                        }
                    }
                    String ipAddressPengirim = ChatServerController.dbManager.getIpAddressPortUser(receivedPacket.idPengirim);
                    //mengirim ke semua client yang terhubung
                    for (Socket clientSocket : connectedSockets.values()) {
                        if (!clientSocket.getRemoteSocketAddress().toString().substring(1).equals(ipAddressPengirim)) {
                            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                            bufferedWriter.write(chatSend.toString());
                            bufferedWriter.flush();
                        }
                    }

                } else if (receivedPacket.chatType == ChatType.PRIVATE) {
                    if (receivedPacket.sourceType == SourceType.CLIENT) {
                        //kirim ke semua server
                        for (Socket connectedServerSocket : connectedServerSockets) {
                            bufferedWriter = new BufferedWriter(new OutputStreamWriter(connectedServerSocket.getOutputStream()));
                            System.out.println("Sending to server " + connectedServerSocket.getRemoteSocketAddress().toString() + " :" + chatSend.toString());
                            bufferedWriter.write(chatSend.toString());
                            bufferedWriter.flush();
                        }
                    }
                    //cari ipnya dl d database
                    String ipAddressPenerima = ChatServerController.dbManager.getIpAddressPortUser(receivedPacket.idPenerima);
                    //mengirim ke ipnya kalo terhubung dengan server
                    if (connectedSockets.containsKey(ipAddressPenerima)) {
                        Socket clientSocket = connectedSockets.get(ipAddressPenerima);
                        System.out.println("Send chat to client " + clientSocket.getRemoteSocketAddress().toString() + " :" + chatSend.toString());
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        bufferedWriter.write(chatSend.toString());
                        bufferedWriter.flush();
                    }
                } else {
                    // Tipe pesannya salah
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }
            this.thread.join();
        } catch (Exception ex) {
            Logger.getLogger(TaskChatSendExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
