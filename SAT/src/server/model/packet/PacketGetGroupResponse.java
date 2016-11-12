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
public class PacketGetGroupResponse extends Packet {

    public ArrayList<String> listGroupID;

    public PacketGetGroupResponse(PacketType command, int serverLoad, SourceType sourceType, ArrayList<String> listGroupID) {
        super(command, serverLoad, sourceType);
        this.listGroupID = listGroupID;
    }

    @Override
    public String getBodyData() {
        String hasil = "";
        for (int i = 0; i < listGroupID.size(); i++) {
            hasil += listGroupID.get(i) + ";";
        }
        if (listGroupID.size() == 0) {
            return "";
        } else {
            return hasil.substring(0, hasil.length() - 1);
        }
    }

}
