/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.packet;

import java.util.ArrayList;

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
                for (int i = 5; i < splitted.length; i++) {
                    listID.add(splitted[i]);
                }
                return new PacketCreateGroup(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], listID);
            case DEFAULT_RESPONSE:
                return new PacketDefaultResponse(packetType,  Integer.parseInt(splitted[1]),
                        SourceType.valueOf(splitted[2]), splitted[3]);
            case GET_CHAT_CLIENT:
                return new PacketGetChatClient(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], Long.parseLong(splitted[5]));
            case GET_CHAT_SERVER:
                return new PacketGetChatServer(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), Long.parseLong(splitted[5]), splitted[6]);
            case GET_ONLINE_CLIENT:
                return new PacketGetOnlineClient(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3]);
            case GET_ONLINE_SERVER:
                for (int i = 3; i < splitted.length; i++) {
                    listID.add(splitted[i]);
                }
                return new PacketGetOnlineServer(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), listID);
            case GOT_ONLINE:
                return new PacketGotOnline(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], Integer.parseInt(splitted[5]));
            case LOGIN_CLIENT:
                return new PacketLoginClient(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4]);
            case LOGIN_RESPONSE:
                return new PacketLoginResponse(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), LoginResponseType.valueOf(splitted[3]), splitted[4]);
            case LOGIN_SERVER:
                return new PacketLoginServer(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3],splitted[4],splitted[5]);
            case LOGOUT:
                return new PacketLogout(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3]);
            case REGISTER:
                return new PacketRegister(packetType, Integer.parseInt(splitted[1]), SourceType.valueOf(splitted[2]), splitted[3], splitted[4], splitted[5]);
        }
        return null;
    }
}
