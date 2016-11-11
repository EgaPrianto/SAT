/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model.clientData;

/**
 *
 * @author Ega Prianto
 */
public class BroadcastChat extends Chat
{
    
    public BroadcastChat(String broadcastId) {
        super(broadcastId);
    }
    
    public void addUserBroadcastChat(String idPengirim,String newChat,long timestamp){
        this.chats.append(idPengirim);
        addChat(newChat,timestamp);
    }
    
}
