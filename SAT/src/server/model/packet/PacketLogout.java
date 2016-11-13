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
public class PacketLogout extends Packet {

    public String id;
    public String timestamp;

    public PacketLogout(PacketType command, int serverLoad, SourceType sourceType, String id,String timestamp) {
        super(command, serverLoad, sourceType);
        this.id=id;
        this.timestamp = timestamp;
    }

    @Override
    public String getBodyData() {
        return id+";"+timestamp;
    }

}
