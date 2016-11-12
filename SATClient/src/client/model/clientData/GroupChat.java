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
public class GroupChat extends Chat {

    /**
     * kalo misalnya groupchat attribut withId jadi id group chat
     *
     * @param withId
     */
    public GroupChat(String withId) {
        super(withId);
    }

    public void addGroupChat(String idPengirim, String newChat, long timestamp) {
        this.chats.append(idPengirim);
        addChat(newChat, timestamp);
    }

}
