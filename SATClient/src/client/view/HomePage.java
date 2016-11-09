/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.model.ConnectionReceiver;
import client.model.ConnectionSender;
import client.model.clientData.Home;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import server.model.packet.ChatType;
import server.model.packet.PacketGetOnlineClient;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author ASUS A455LF
 */
public class HomePage extends javax.swing.JPanel implements Observer{

    GraphicalUI gui;
    ConnectionReceiver connRecv;
    ConnectionSender connSend;
    Dimension panelFriendListDimension;

    public HomePage(GraphicalUI gui, ConnectionReceiver connRecv, ConnectionSender connSend) {
        initComponents();
        panelFriendListDimension = new Dimension(450, 0);
        jPanelFriendList.setLayout(new BoxLayout(jPanelFriendList, BoxLayout.PAGE_AXIS));
        jScrollPaneFriendList.getVerticalScrollBar().setUnitIncrement(20);
        this.jPanelFriendList.setPreferredSize(panelFriendListDimension);
        this.jPanelFriendList.repaint();
        PacketGetOnlineClient requestOnlineClient = new PacketGetOnlineClient(PacketType.GET_ONLINE_CLIENT,
                0,
                SourceType.CLIENT,
                connRecv.user.get().getId(),
                connRecv.socket.getLocalSocketAddress().toString().substring(1));
        connSend.addPacket(requestOnlineClient);
        this.gui = gui;
        this.connRecv = connRecv;
        this.connSend = connSend;
    }
    public void resetFriendList(){
        panelFriendListDimension = new Dimension(450, 0);
        this.jPanelFriendList.removeAll();
        jPanelFriendList.setLayout(new BoxLayout(jPanelFriendList, BoxLayout.PAGE_AXIS));
        this.jPanelFriendList.setPreferredSize(panelFriendListDimension);
        this.jPanelFriendList.repaint();
    }

    public void addNewFriendList(String id) {
        panelFriendListDimension.setSize(panelFriendListDimension.getWidth(), panelFriendListDimension.getHeight() + 46);
        this.jPanelFriendList.add(new EntityUI(connRecv, connSend, ChatType.PRIVATE, id));
        this.jPanelFriendList.setPreferredSize(panelFriendListDimension);
        this.jPanelFriendList.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanelFriendSubFrame = new javax.swing.JPanel();
        jScrollPaneFriendList = new javax.swing.JScrollPane();
        jPanelFriendList = new javax.swing.JPanel();
        jPanelGroupList = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jPanelFriendList.setToolTipText("");
        jPanelFriendList.setPreferredSize(new java.awt.Dimension(453, 200));
        jPanelFriendList.setLayout(new javax.swing.BoxLayout(jPanelFriendList, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPaneFriendList.setViewportView(jPanelFriendList);

        javax.swing.GroupLayout jPanelFriendSubFrameLayout = new javax.swing.GroupLayout(jPanelFriendSubFrame);
        jPanelFriendSubFrame.setLayout(jPanelFriendSubFrameLayout);
        jPanelFriendSubFrameLayout.setHorizontalGroup(
            jPanelFriendSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFriendSubFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneFriendList)
                .addContainerGap())
        );
        jPanelFriendSubFrameLayout.setVerticalGroup(
            jPanelFriendSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFriendSubFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneFriendList, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Friend", jPanelFriendSubFrame);

        jPanelGroupList.setLayout(new javax.swing.BoxLayout(jPanelGroupList, javax.swing.BoxLayout.LINE_AXIS));
        jTabbedPane5.addTab("Group", jPanelGroupList);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jTextField1.setText("insert text here");

        jButton1.setText("SEND");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane5.addTab("Broadcast", jPanel3);

        jButton2.setText("LOGOUT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("REFRESH");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        PacketGetOnlineClient requestOnlineClient = new PacketGetOnlineClient(PacketType.GET_ONLINE_CLIENT,
                0,
                SourceType.CLIENT,
                connRecv.user.get().getId(),
                connRecv.socket.getLocalSocketAddress().toString().substring(1));
        connSend.addPacket(requestOnlineClient);
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelFriendList;
    private javax.swing.JPanel jPanelFriendSubFrame;
    private javax.swing.JPanel jPanelGroupList;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneFriendList;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        Home update = (Home) o;
        resetFriendList();
        for (int i = 0; i < update.getOnlineIds().size(); i++) {
            addNewFriendList(update.getOnlineIds().get(i));
        }
    }
}
