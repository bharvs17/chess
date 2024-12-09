package clientfiles.websocket;

import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notify(ServerMessage msg);
    void loadGame(ServerMessage msg);
    //may need to add another method to handle getting chess game?
    //(load game message) and maybe error message?
}
