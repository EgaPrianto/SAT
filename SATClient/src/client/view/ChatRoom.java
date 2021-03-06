/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.model.ConnectionReceiver;
import client.model.ConnectionSender;
import client.model.clientData.Chat;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import server.model.db.JDBCMySQLManager;
import server.model.packet.ChatType;
import server.model.packet.PacketChatSend;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author ASUS A455LF
 */
public class ChatRoom extends javax.swing.JFrame implements Observer, Runnable {

    private Chat chats;
    private ChatType chatType;
    private String userId;
    private String idLawan;
    private ConnectionSender connSend;
    private ConnectionReceiver connRecv;

    public ChatRoom(Chat chats, ChatType chatType, String userId, String idLawan, ConnectionSender connSend,ConnectionReceiver connRecv) {
        initComponents();
        this.chats = chats;
        this.chatType = chatType;
        this.userId = userId;
        this.idLawan = idLawan;
        this.connSend = connSend;
        System.out.println(toString());
        this.connRecv = connRecv;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.jTextAreaChatArea.setEditable(false);
        this.jTextAreaChatArea.setText(chats.toString());
        /*Some piece of code*/
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                connRecv.chatRoomsData.remove(idLawan);
                dispose();
            }
        });
    }

    public static void popChatWindow(Chat chats, ChatType chatType, String userId, String idLawan, ConnectionSender connSend,ConnectionReceiver connRecv) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatRoom chatRoom = new ChatRoom(chats, chatType, userId, idLawan, connSend,connRecv);
                chats.addObserver(chatRoom);
                chatRoom.setVisible(true);
            }
        }).start();
    }

    @Override
    public String toString() {
        return "ChatRoom{" + "chats=" + chats + ", chatType=" + chatType + ", userId=" + userId + ", idLawan=" + idLawan + ", jTextFieldChatInput=" + jTextFieldChatInput.getText() + '}';
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaChatArea = new javax.swing.JTextArea();
        jTextFieldChatInput = new javax.swing.JTextField();
        jButtonSendChat = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextAreaChatArea.setColumns(20);
        jTextAreaChatArea.setRows(5);
        jScrollPane1.setViewportView(jTextAreaChatArea);

        jButtonSendChat.setText("SEND");
        jButtonSendChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendChatActionPerformed(evt);
            }
        });

        jLabel1.setText("Insert Chat Below");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSendChat, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonSendChat, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(jTextFieldChatInput))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendChatActionPerformed
        System.out.println(toString());
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        PacketChatSend newPacketChatSend = new PacketChatSend(PacketType.CHAT_SEND, 0, SourceType.CLIENT, chatType, userId, this.idLawan, this.jTextFieldChatInput.getText(), GraphicalUI.DATE_FORMAT.format(currentDate));
        this.chats.addSelfChat(newPacketChatSend.chat, currentTime);
        System.out.println("Sending packet : " + newPacketChatSend.toString());
        System.out.println("body data : " + newPacketChatSend.getBodyData());
        connSend.addPacket(newPacketChatSend);
    }//GEN-LAST:event_jButtonSendChatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSendChat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaChatArea;
    private javax.swing.JTextField jTextFieldChatInput;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        Chat update = (Chat) o;
        this.jTextAreaChatArea.setText(update.toString());
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
