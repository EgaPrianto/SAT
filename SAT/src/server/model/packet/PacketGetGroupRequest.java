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
public class PacketGetGroupRequest extends Packet {
    
    public String id;
    public String ipAddressPort;
    
    public PacketGetGroupRequest(PacketType command, int serverLoad, SourceType sourceType,String id) {
        super(command, serverLoad, sourceType);
        this.id = id;
    }

    @Override
    public String getBodyData() {
        return id;
    }

}
