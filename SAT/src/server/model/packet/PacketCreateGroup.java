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
public class PacketCreateGroup extends Packet{
    public String id_group;
    public String nama;
    public ArrayList<String> listIDMember; 
    public String idCreator;
    
    public PacketCreateGroup(PacketType command, int serverLoad, SourceType sourceType, String id_group, String nama, String idCreator,ArrayList<String> listIDMember) {
        super(command, serverLoad, sourceType);
        this.id_group=id_group;
        this.nama=nama;
        this.listIDMember = listIDMember;
        this.idCreator = idCreator;
    }

    @Override
    public String getBodyData() {
        String hasil="";
        for (int i = 0; i < listIDMember.size()-1; i++) {
            hasil+=listIDMember.get(i)+";";
        }
        hasil+=listIDMember.get(listIDMember.size()-1);
        return id_group + ";" + nama+";"+idCreator+";"+hasil;
    }
}
