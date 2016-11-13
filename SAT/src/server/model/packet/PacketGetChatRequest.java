/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.packet;

/**
 *
 * @author Ega Prianto
 */
public class PacketGetChatRequest extends Packet {

    public String id_pengirim;
    public String id_penerima;
    public ChatType chatType;
    public String timestamp;
    public String ipAddressPort;

    public PacketGetChatRequest(PacketType command, int serverLoad, SourceType sourceType, ChatType chatType, String id_pengirim, String id_penerima, String timestamp) {
        super(command, serverLoad, sourceType);
        this.chatType = chatType;
        this.id_penerima = id_penerima;
        this.id_pengirim = id_pengirim;
        this.timestamp = timestamp;
    }

    @Override
    public String getBodyData() {
        return chatType + ";" + id_pengirim + ";" + id_penerima + ";" + timestamp;
    }
}
