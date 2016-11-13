/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.packet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ega Prianto
 */
public class PacketFactory {

    public static Packet createPacketFromString(String string) {
        System.out.println("Received Packet :\n" + string);
        String[] splitted = string.split(";");
        PacketType packetType = PacketType.valueOf(splitted[0]);
        ArrayList<String> listID = new ArrayList<>();
        switch (packetType) {
            case CHAT_SEND:
                return new PacketChatSend(packetType, Integer.parseInt(splitted[1]),
                        SourceType.valueOf(splitted[2]), ChatType.valueOf(splitted[3]),
                        splitted[4], splitted[5], splitted[6], splitted[7]);
            case CREATE_GROUP:
                for (int i = 6; i < splitted.length; i++) {
                    listID.add(splitted[i]);
                }
                return new PacketCreateGroup(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], splitted[5], listID);
            case DEFAULT_RESPONSE:
                return new PacketDefaultResponse(packetType, Integer.parseInt(splitted[1]),
                        SourceType.valueOf(splitted[2]), splitted[3]);
            case GET_CHAT_REQUEST:
                return new PacketGetChatRequest(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), ChatType.valueOf(splitted[3]), splitted[4], splitted[5], Long.parseLong(splitted[6]));
            case GET_CHAT_RESPONSE:
                List<PacketChatSend> chats = new ArrayList<>();
                for (int i = 4; i < splitted.length; i+=5) {
                    PacketChatSend chat = new PacketChatSend(packetType, Integer.parseInt(splitted[1]),
                        SourceType.valueOf(splitted[2]), ChatType.valueOf(splitted[i]),
                        splitted[i+1], splitted[i+2], splitted[i+3], splitted[i+4]);
                    chats.add(chat);
                }
                return new PacketGetChatResponse(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], chats);
            case GET_ONLINE_REQUEST:
                return new PacketGetOnlineRequest(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3]);
            case GET_ONLINE_RESPONSE:
                for (int i = 3; i < splitted.length; i++) {
                    listID.add(splitted[i]);
                }
                return new PacketGetOnlineResponse(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), listID);
            case GET_GROUP_REQUEST:
                return new PacketGetGroupRequest(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3]);
            case GET_GROUP_RESPONSE:
                for (int i = 3; i < splitted.length; i++) {
                    listID.add(splitted[i]);
                }
                return new PacketGetGroupResponse(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), listID);
            case GOT_ONLINE:
                return new PacketGotOnline(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], Integer.parseInt(splitted[5]));
            case LOGIN_REQUEST:
                return new PacketLoginRequest(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4]);
            case LOGIN_RESPONSE:
                return new PacketLoginResponse(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), LoginResponseType.valueOf(splitted[3]), splitted[4]);
            case LOGIN_SERVER:
                return new PacketLoginServer(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], splitted[5]);
            case LOGOUT:
                return new PacketLogout(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3]);
            case REGISTER:
                return new PacketRegister(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], splitted[5]);
        }
        return null;
    }
}
