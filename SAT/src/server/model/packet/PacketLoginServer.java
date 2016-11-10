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
public class PacketLoginServer extends Packet {

    public String id;
    public String ipAddressPort;
    public String idServer;

    public PacketLoginServer(PacketType command, int serverLoad, SourceType sourceType, String id, String ipAddressPort, String idServer) {
        super(command, serverLoad, sourceType);
        this.id = id;
        this.ipAddressPort = ipAddressPort;
        this.idServer = idServer;
    }

    @Override
    public String getBodyData() {
        return id + ";" + this.ipAddressPort + ";" + this.idServer;
    }

}
