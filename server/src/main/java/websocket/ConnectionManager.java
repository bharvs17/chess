package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, int gameID, Session session) {
        var connection = new Connection(gameID, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuth, int givenGameID, String notif) throws IOException {
        var removeList = new ArrayList<String>();
        for(var c : connections.entrySet()) {
            if(c.getValue().session.isOpen()) {
                if(!c.getKey().equals(excludeAuth) && c.getValue().gameID == givenGameID) {
                    c.getValue().send(notif);
                }
            } else {
                removeList.add(c.getKey());
            }
        }
        for(var c : removeList) {
            connections.remove(c);
        }
    }
}
