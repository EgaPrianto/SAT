/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

import client.model.clientData.BroadcastChat;
import client.model.clientData.Chat;
import client.model.clientData.Home;
import client.model.clientData.PrivateChat;
import client.model.clientData.User;
import client.view.ChatRoom;
import client.view.GraphicalUI;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import server.model.db.JDBCMySQLManager;
import server.model.packet.ChatType;
import server.model.packet.Packet;
import server.model.packet.PacketChatSend;
import server.model.packet.PacketDefaultResponse;
import server.model.packet.PacketFactory;
import server.model.packet.PacketGetOnlineServer;
import server.model.packet.PacketGotOnline;
import server.model.packet.PacketLoginResponse;
import server.model.packet.PacketLoginServer;
import server.model.packet.PacketLogout;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author Ega Prianto
 */
public class ConnectionReceiver implements Runnable {
    
    public int port;
    public String ip;
    public Socket socket;
    private Thread thread;
    private boolean isFinish;
    public static String MOTD;
    public ConnectionSender connSend;
    
    private BufferedReader br;
    private BufferedWriter bw;
    public ConcurrentHashMap<String, Chat> chatRoomsData;
    public AtomicReference<BroadcastChat> broadcastRoomData;
    public AtomicReference<User> user;
    public AtomicReference<Home> home;
    
    public void setConnSend(ConnectionSender connSend) {
        this.connSend = connSend;
    }
    
    public ConnectionReceiver(String ip, int port) throws IOException {
        this.thread = new Thread(this);
        this.port = port;
        this.ip = ip;
        socket = new Socket(ip, port);
        System.out.println(socket.getLocalSocketAddress().toString());
        this.chatRoomsData = new ConcurrentHashMap<>();
        user = new AtomicReference<>(new User(null, null, false));
        home = new AtomicReference<>(new Home());
        broadcastRoomData = new AtomicReference<>(new BroadcastChat("broadcast"));
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    @Override
    public void run() {
        try {
            bw.write(new PacketGotOnline(PacketType.GOT_ONLINE, 0, SourceType.CLIENT, "", socket.getLocalAddress().toString(), socket.getLocalPort()).toString());
            bw.flush();
            MOTD = br.readLine() + " ";//"WELCOME TO SERVER

            System.out.println(MOTD);
            MOTD += br.readLine();//CONNECTED
            System.out.println(MOTD);
            while (!isFinish) {
                String receivedMsg = br.readLine();
//                System.out.println("Received Packet : "+receivedMsg);
                Packet receivedPacket = PacketFactory.createPacketFromString(receivedMsg);
                System.out.println("Packet Builded :" + receivedPacket.toString());
                switch (receivedPacket.command) {
                    case CHAT_SEND:
                        if (receivedPacket instanceof PacketChatSend) {
                            PacketChatSend chatReceived = (PacketChatSend) receivedPacket;
                            if (chatReceived.chatType == ChatType.PRIVATE) {
                                PrivateChat chatRoomData = (PrivateChat) this.chatRoomsData.get(chatReceived.idPengirim);
                                if (chatRoomData == null) {
                                    chatRoomData = new PrivateChat(chatReceived.idPengirim);
                                    chatRoomData.addOpponentChat(chatReceived.chat, GraphicalUI.DATE_FORMAT.parse(chatReceived.timestamp).getTime());
                                    this.chatRoomsData.put(chatReceived.idPengirim, chatRoomData);
                                    ChatRoom.popChatWindow(chatRoomData, chatReceived.chatType, user.get().getId(), chatReceived.idPengirim, connSend, this);
                                } else {
                                    chatRoomData.addOpponentChat(chatReceived.chat, GraphicalUI.DATE_FORMAT.parse(chatReceived.timestamp).getTime());
                                }
                            } else if (chatReceived.chatType == ChatType.BROADCAST) {
                                broadcastRoomData.get().addUserBroadcastChat(chatReceived.idPengirim, chatReceived.chat, GraphicalUI.DATE_FORMAT.parse(chatReceived.timestamp).getTime());
                            };
                        }
                        break;
                    case LOGIN_RESPONSE:
                        if (receivedPacket instanceof PacketLoginResponse) {
                            PacketLoginResponse loginResponseReceived = (PacketLoginResponse) receivedPacket;
                            switch (loginResponseReceived.response) {
                                case FORBIDDEN:
                                    this.user.get().setAuthenticated(false);
                                    break;
                                case PROCEED:
                                    this.user.get().setUsername(loginResponseReceived.username);
                                    this.user.get().setAuthenticated(true);
                                    break;
                            };
                        }
                        break;
                    case GET_ONLINE_SERVER:
                        if (receivedPacket instanceof PacketGetOnlineServer) {
                            PacketGetOnlineServer getOnlineServer = (PacketGetOnlineServer) receivedPacket;
                            this.home.get().clearOnlineId();
                            this.home.get().addAllOnlineId(getOnlineServer.listID);
                        }
                        break;
                    case LOGIN_SERVER:
                        if (receivedPacket instanceof PacketLoginServer) {
                            PacketLoginServer getLoginServer = (PacketLoginServer) receivedPacket;
                            this.home.get().addOnlineId(getLoginServer.id);
                        }
                        break;
                    case LOGOUT:
                        if (receivedPacket instanceof PacketLogout) {
                            PacketLogout getLogout = (PacketLogout) receivedPacket;
                            this.home.get().removeOnlineId(getLogout.id);
                        }
                        break;
                    case DEFAULT_RESPONSE:
                        if (receivedPacket instanceof PacketDefaultResponse) {
                            PacketDefaultResponse getResponse = (PacketDefaultResponse) receivedPacket;
                            JOptionPane.showConfirmDialog(null, getResponse.response, "Server Response", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ConnectionReceiver.class.getName()).log(Level.SEVERE, null, ex);
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
