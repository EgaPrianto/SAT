/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.packet;

import java.util.List;

/**
 *
 * @author Ega Prianto
 */
public class PacketGetChatResponse extends Packet {

    public List<PacketChatSend> chats;
    public String timestamp;

    public PacketGetChatResponse(PacketType command, int serverLoad, SourceType sourceType, String timestamp, List<PacketChatSend> chats) {
        super(command, serverLoad, sourceType);
        this.chats = chats;
        this.timestamp = timestamp;
    }

    @Override
    public String getBodyData() {
        String hasil = "";
        for (PacketChatSend chat : chats) {
            hasil += chat.getBodyData();
            hasil += ";";
        }
        if (!chats.isEmpty()) {
            hasil = hasil.substring(0, hasil.length()-1);
        }
        return timestamp + ";" + hasil;
    }
}
