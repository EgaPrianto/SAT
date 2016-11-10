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
public class PacketLoginClient extends Packet {

    public String id;
    public String password;
    public String ipAddressPort;

    public PacketLoginClient(PacketType command, int serverLoad, SourceType sourceType, String id, String password) {
        super(command, serverLoad, sourceType);
        this.id=id;
        this.password=password;
    }

    @Override
    public String getBodyData() {
        return id+ ";"+password;
    }

}
