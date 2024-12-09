package clientfiles.websocket;

import websocket.messages.NotificationMessage;

public interface ServerMessageHandler {
    void notify(NotificationMessage msg);
    //may need to add another method to handle getting chess game?
    //(load game message) and maybe error message?
}
