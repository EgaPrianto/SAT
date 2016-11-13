/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.task.manager;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.packet.Packet;
import server.model.packet.PacketChatSend;
import server.model.packet.PacketCreateGroup;
import server.model.packet.PacketGetChatRequest;
import server.model.packet.PacketGetGroupRequest;
import server.model.packet.PacketGetOnlineRequest;
import server.model.packet.PacketLoginRequest;
import server.model.packet.PacketLoginServer;
import server.model.packet.PacketLogout;
import server.model.packet.PacketRegister;
import server.model.task.manager.executor.TaskChatSendExecutor;
import server.model.task.manager.executor.TaskCreateGroupExecutor;
import server.model.task.manager.executor.TaskExecutor;
import server.model.task.manager.executor.TaskGetChatExecutor;
import server.model.task.manager.executor.TaskGetGroupExecutor;
import server.model.task.manager.executor.TaskGetOnlineExecutor;
import server.model.task.manager.executor.TaskLoginExecutor;
import server.model.task.manager.executor.TaskLogoutExecutor;
import server.model.task.manager.executor.TaskRegisterExecutor;

/**
 *
 * @author Ega Prianto
 */
public class TaskManager implements Runnable {

    private boolean isFinish;
    private Thread thread;
    private ConcurrentHashMap<String, Socket> connectedSockets;
    private CopyOnWriteArrayList< Socket> connectedServerSockets;
    private ConcurrentLinkedQueue<Packet> packetQueue;

    public TaskManager(ConcurrentHashMap<String, Socket> connectedSockets, CopyOnWriteArrayList< Socket> connectedServerSockets, ConcurrentLinkedQueue<Packet> packetQueue) {
        this.thread = new Thread(this);
        this.connectedSockets = connectedSockets;
        this.connectedServerSockets = connectedServerSockets;
        this.packetQueue = packetQueue;
    }

    @Override
    public void run() {
        while (!isFinish) {
            try {
                if (!packetQueue.isEmpty()) {
                    Packet newPacket = packetQueue.poll();
                    System.out.println("Assign Task");
                    switch (newPacket.command) {
                        case CREATE_GROUP:
                            if (newPacket instanceof PacketCreateGroup) {
                                System.out.println("Task Chat Send Assigned");
                                new TaskCreateGroupExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;
                        case CHAT_SEND:
                            if (newPacket instanceof PacketChatSend) {
                                System.out.println("Task Chat Send Assigned");
                                new TaskChatSendExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;
                        case GET_ONLINE_REQUEST:
                            if (newPacket instanceof PacketGetOnlineRequest) {
                                System.out.println("Task Get Online Client Assigned");
                                new TaskGetOnlineExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;
                        case GET_GROUP_REQUEST:
                            if (newPacket instanceof PacketGetGroupRequest) {
                                System.out.println("Task Get Group Request Assigned");
                                new TaskGetGroupExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;                            
                        case LOGIN_REQUEST:
                            if (newPacket instanceof PacketLoginRequest) {
                                System.out.println("Task Login Client Assigned");
                                new TaskLoginExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;
                        case LOGIN_SERVER:
                            if (newPacket instanceof PacketLoginServer) {
                                System.out.println("Task Login Server Assigned");
                                new TaskLoginExecutor(connectedSockets, connectedServerSockets, newPacket).start();
                            }
                            break;
                        case LOGOUT:
                            if (newPacket instanceof PacketLogout) {
                                System.out.println("Task Logout Assigned");
                                new TaskLogoutExecutor(connectedSockets, connectedServerSockets, newPacket).start();                                
                            }
                            break;
                        case REGISTER:
                            if (newPacket instanceof PacketRegister) {
                                System.out.println("Task Register Assigned");
                                new TaskRegisterExecutor(connectedSockets, connectedServerSockets, newPacket).start();                                
                            }
                            break;
                        case GET_CHAT_REQUEST:
                            if (newPacket instanceof PacketGetChatRequest) {
                                System.out.println("Task Get Chat Assigned");
                                new TaskGetChatExecutor(connectedSockets, connectedServerSockets, newPacket).start();                                
                            }
                            break;
                    }
                }
                Thread.sleep(60);
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
            }
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
