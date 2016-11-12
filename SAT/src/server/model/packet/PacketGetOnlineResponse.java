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
public class PacketGetOnlineResponse extends Packet {

    public ArrayList<String> listID;

    public PacketGetOnlineResponse(PacketType command, int serverLoad, SourceType sourceType, ArrayList<String> listID) {
        super(command, serverLoad, sourceType);
        this.listID = listID;
    }

    @Override
    public String getBodyData() {
        String hasil = "";
        for (int i = 0; i < listID.size(); i++) {
            hasil += listID.get(i) + ";";
        }
        if (listID.size() == 0) {
            return "";
        } else {
            return hasil.substring(0, hasil.length() - 1);
        }
    }

}
