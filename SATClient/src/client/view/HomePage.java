/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.model.ConnectionReceiver;
import client.model.ConnectionSender;
import client.model.clientData.BroadcastChat;
import client.model.clientData.Home;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import server.model.packet.ChatType;
import server.model.packet.PacketChatSend;
import server.model.packet.PacketCreateGroup;
import server.model.packet.PacketGetGroupRequest;
import server.model.packet.PacketGetOnlineRequest;
import server.model.packet.PacketLogout;
import server.model.packet.PacketType;
import server.model.packet.SourceType;

/**
 *
 * @author ASUS A455LF
 */
public class HomePage extends javax.swing.JPanel implements Observer {

    GraphicalUI gui;
    ConnectionReceiver connRecv;
    ConnectionSender connSend;
    Dimension panelFriendListDimension;
    Dimension panelGroupListDimension;

    public HomePage(GraphicalUI gui, ConnectionReceiver connRecv, ConnectionSender connSend) {
        initComponents();
        panelFriendListDimension = new Dimension(450, 0);
        panelGroupListDimension = new Dimension(450, 0);
        jPanelFriendList.setLayout(new BoxLayout(jPanelFriendList, BoxLayout.PAGE_AXIS));
        jPanelGroupList.setLayout(new BoxLayout(jPanelGroupList, BoxLayout.PAGE_AXIS));
        jScrollPaneFriendList.getVerticalScrollBar().setUnitIncrement(20);
        this.jPanelFriendList.setPreferredSize(panelFriendListDimension);
        this.jPanelFriendList.repaint();
        jScrollPaneGroupList.getVerticalScrollBar().setUnitIncrement(20);
        this.jPanelGroupList.setPreferredSize(panelFriendListDimension);
        this.jPanelGroupList.repaint();
        this.jTextAreaBroadcastChat.setEditable(false);
        PacketGetOnlineRequest requestOnlineClient = new PacketGetOnlineRequest(PacketType.GET_ONLINE_REQUEST,
                0,
                SourceType.CLIENT,
                connRecv.user.get().getId());
        connSend.addPacket(requestOnlineClient);
        PacketGetGroupRequest requestGetGroup = new PacketGetGroupRequest(PacketType.GET_GROUP_REQUEST,
                0,
                SourceType.CLIENT,
                connRecv.user.get().getId());
        connSend.addPacket(requestGetGroup);
        this.gui = gui;
        this.connRecv = connRecv;
        this.connSend = connSend;
        this.gui.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                long currTime = System.currentTimeMillis();
                Date currentDate = new Date(currTime);
                PacketLogout packetLoggingout = new PacketLogout(PacketType.LOGOUT, 0, SourceType.CLIENT, connRecv.user.get().getId(), GraphicalUI.DATE_FORMAT.format(currentDate));
                connSend.addPacket(packetLoggingout);
                System.exit(0);

            }
        });
    }

    public void resetGroupList() {
        this.panelGroupListDimension = new Dimension(450, 0);
        this.jPanelGroupList.removeAll();
        this.jPanelGroupList.setLayout(new BoxLayout(jPanelGroupList, BoxLayout.PAGE_AXIS));
        this.jPanelGroupList.setPreferredSize(panelGroupListDimension);
        this.jPanelGroupList.repaint();
    }

    public void addNewGroupList(String idGroup) {
        panelFriendListDimension.setSize(panelGroupListDimension.getWidth(), panelGroupListDimension.getHeight() + 46);
        this.jPanelGroupList.add(new EntityUI(connRecv, connSend, ChatType.GROUP, idGroup));
        this.jPanelGroupList.setPreferredSize(panelGroupListDimension);
        this.jPanelGroupList.repaint();
    }

    public void resetFriendList() {
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
        jPanelGroupSubFrame = new javax.swing.JPanel();
        jScrollPaneGroupList = new javax.swing.JScrollPane();
        jPanelGroupList = new javax.swing.JPanel();
        jButtonCreateNewGroup = new javax.swing.JButton();
        jPanelBroadcastChat = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaBroadcastChat = new javax.swing.JTextArea();
        jTextFieldBroadcastChat = new javax.swing.JTextField();
        jButtonBroadcastChatSend = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButtonLogout = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();

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
                .addComponent(jScrollPaneFriendList, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane5.addTab("Friend", jPanelFriendSubFrame);

        jPanelGroupList.setLayout(new javax.swing.BoxLayout(jPanelGroupList, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPaneGroupList.setViewportView(jPanelGroupList);

        jButtonCreateNewGroup.setText("Create New Group");
        jButtonCreateNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateNewGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelGroupSubFrameLayout = new javax.swing.GroupLayout(jPanelGroupSubFrame);
        jPanelGroupSubFrame.setLayout(jPanelGroupSubFrameLayout);
        jPanelGroupSubFrameLayout.setHorizontalGroup(
            jPanelGroupSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGroupSubFrameLayout.createSequentialGroup()
                .addGroup(jPanelGroupSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGroupSubFrameLayout.createSequentialGroup()
                        .addGap(354, 354, 354)
                        .addComponent(jButtonCreateNewGroup))
                    .addGroup(jPanelGroupSubFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPaneGroupList, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelGroupSubFrameLayout.setVerticalGroup(
            jPanelGroupSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGroupSubFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCreateNewGroup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneGroupList)
                .addGap(12, 12, 12))
        );

        jTabbedPane5.addTab("Group", jPanelGroupSubFrame);

        jTextAreaBroadcastChat.setColumns(20);
        jTextAreaBroadcastChat.setRows(5);
        jScrollPane3.setViewportView(jTextAreaBroadcastChat);

        jButtonBroadcastChatSend.setText("SEND");
        jButtonBroadcastChatSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBroadcastChatSendActionPerformed(evt);
            }
        });

        jLabel1.setText("Insert broadcast text below:");

        javax.swing.GroupLayout jPanelBroadcastChatLayout = new javax.swing.GroupLayout(jPanelBroadcastChat);
        jPanelBroadcastChat.setLayout(jPanelBroadcastChatLayout);
        jPanelBroadcastChatLayout.setHorizontalGroup(
            jPanelBroadcastChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBroadcastChatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBroadcastChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBroadcastChatLayout.createSequentialGroup()
                        .addComponent(jTextFieldBroadcastChat, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonBroadcastChatSend, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelBroadcastChatLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelBroadcastChatLayout.setVerticalGroup(
            jPanelBroadcastChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBroadcastChatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBroadcastChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldBroadcastChat)
                    .addComponent(jButtonBroadcastChatSend, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane5.addTab("Broadcast", jPanelBroadcastChat);

        jButtonLogout.setText("LOGOUT");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        jButtonRefresh.setText("REFRESH");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
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
                .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane5)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        try {

            long currTime = System.currentTimeMillis();
            Date currentDate = new Date(currTime);
            PacketLogout packetLoggingout = new PacketLogout(PacketType.LOGOUT, 0, SourceType.CLIENT, connRecv.user.get().getId(), GraphicalUI.DATE_FORMAT.format(currentDate));
            connSend.addPacket(packetLoggingout);
            this.connRecv.home.get().deleteObservers();
            this.connRecv.broadcastRoomData.get().deleteObservers();
            this.connRecv.user.get().setId(null);
            this.connRecv.user.get().setUsername(null);
            this.connRecv.user.get().setAuthenticated(false);
            this.connRecv.user.get().deleteObservers();
            this.gui.setMainPanelTo(new InitPage(gui, connRecv, connSend));
        } catch (IOException ex) {
            JOptionPane.showConfirmDialog(null, "Error IO", "Error", JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        PacketGetOnlineRequest requestOnlineClient = new PacketGetOnlineRequest(PacketType.GET_ONLINE_REQUEST,
                0,
                SourceType.CLIENT,
                connRecv.user.get().getId());
        connSend.addPacket(requestOnlineClient);
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonBroadcastChatSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBroadcastChatSendActionPerformed
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        PacketChatSend packetChatSend = new PacketChatSend(PacketType.CHAT_SEND, 0,
                SourceType.CLIENT,
                ChatType.BROADCAST,
                this.connRecv.user.get().getId(),
                "broadcast", this.jTextFieldBroadcastChat.getText(), GraphicalUI.DATE_FORMAT.format(currentDate));
        connSend.addPacket(packetChatSend);
        this.connRecv.broadcastRoomData.get().addSelfChat(this.jTextFieldBroadcastChat.getText(), currentTime);
    }//GEN-LAST:event_jButtonBroadcastChatSendActionPerformed

    private void jButtonCreateNewGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateNewGroupActionPerformed
        CreateGroupUI createGroup = new CreateGroupUI();
        int result = JOptionPane.showConfirmDialog(null, createGroup,
                "Please Enter Group Info", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String idGroup = createGroup.getjTextFieldIDGroup().getText();
            String groupName = createGroup.getjTextFieldGroupName().getText();
            String listMemberId = createGroup.getjTextAreaMemberIDs().getText();
            String[] splitted = listMemberId.split(";");
            ArrayList<String> arrayListMemberIDs = new ArrayList<>();
            for (String string : splitted) {
                arrayListMemberIDs.add(string);
            }
            PacketCreateGroup packetCreateGroup = new PacketCreateGroup(PacketType.CREATE_GROUP, 0, SourceType.CLIENT, idGroup, groupName, this.connRecv.user.get().getId(), arrayListMemberIDs);
            connSend.addPacket(packetCreateGroup);
        }
    }//GEN-LAST:event_jButtonCreateNewGroupActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBroadcastChatSend;
    private javax.swing.JButton jButtonCreateNewGroup;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelBroadcastChat;
    private javax.swing.JPanel jPanelFriendList;
    private javax.swing.JPanel jPanelFriendSubFrame;
    private javax.swing.JPanel jPanelGroupList;
    private javax.swing.JPanel jPanelGroupSubFrame;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneFriendList;
    private javax.swing.JScrollPane jScrollPaneGroupList;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextArea jTextAreaBroadcastChat;
    private javax.swing.JTextField jTextFieldBroadcastChat;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BroadcastChat) {
            BroadcastChat update = (BroadcastChat) o;
            this.jTextAreaBroadcastChat.setText(update.toString());
        } else if (o instanceof Home) {
            Home update = (Home) o;
            resetFriendList();
            for (int i = 0; i < update.getOnlineIds().size(); i++) {
                addNewFriendList(update.getOnlineIds().get(i));
            }
            resetGroupList();
            for (int i = 0; i < update.getGroupIds().size(); i++) {
                addNewGroupList(update.getGroupIds().get(i));
            }
        }
    }
}
