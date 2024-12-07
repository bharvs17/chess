package websocket.messages;

public class NotificationMessage extends ServerMessage {

    private final String notificationMsg;

    public NotificationMessage(ServerMessageType type, String notificationMsg) {
        super(type);
        this.notificationMsg = notificationMsg;
    }

    public String getNotification() {
        return notificationMsg;
    }
}
