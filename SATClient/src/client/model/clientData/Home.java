/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model.clientData;

import java.util.Collection;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Ega Prianto
 */
public class Home extends Observable {
    
    private CopyOnWriteArrayList<String> onlineIds;
    private CopyOnWriteArrayList<String> groupIds;
    
    public Home() {
        onlineIds = new CopyOnWriteArrayList<>();
        groupIds = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<String> getOnlineIds() {
        return onlineIds;
    }

    public void removeOnlineId(String id){
//        onlineIds.remove(onlineIds.indexOf(id));
        onlineIds.remove(id);
        setChanged();
        notifyObservers();
    }
    public void addOnlineId(String id){
        onlineIds.add(id);
        setChanged();
        notifyObservers();
    }
    
    public void clearOnlineId(){
        onlineIds.clear();
        setChanged();
        notifyObservers();
    }
    
    public void addAllOnlineId(Collection <? extends String> c){
        onlineIds.addAll(c);
        setChanged();
        notifyObservers();
    }
    
    
}
