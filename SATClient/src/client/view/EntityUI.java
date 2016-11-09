/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.model.ConnectionReceiver;
import client.model.ConnectionSender;
import client.model.clientData.Chat;
import client.model.clientData.PrivateChat;
import server.model.packet.ChatType;

/**
 *
 * @author ASUS A455LF
 */
public class EntityUI extends javax.swing.JPanel {

    public ChatType chatType;
    public String idLawan;
    public ConnectionReceiver connRecv;
    public ConnectionSender connSend;

    public EntityUI(ConnectionReceiver connRecv, ConnectionSender connSend, ChatType chatType, String idLawan) {
        initComponents();
        this.chatType = chatType;
        this.idLawan = idLawan;
        this.connRecv = connRecv;
        this.connSend = connSend;
        this.jLabelID.setText(idLawan);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelID = new javax.swing.JLabel();
        jButtonChat = new javax.swing.JButton();

        jLabelID.setText("Friend Name ");

        jButtonChat.setText("CHAT");
        jButtonChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelID, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButtonChat)
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelID)
                    .addComponent(jButtonChat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChatActionPerformed
        
        Chat newChat;
        if (chatType == ChatType.PRIVATE) {
            newChat = new PrivateChat(idLawan);
        } else {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        this.connRecv.chatRoomsData.put(idLawan, newChat);
        ChatRoom.popChatWindow(newChat, chatType, connRecv.user.get().getId(), idLawan, connSend,connRecv);
    }//GEN-LAST:event_jButtonChatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonChat;
    private javax.swing.JLabel jLabelID;
    // End of variables declaration//GEN-END:variables
}
