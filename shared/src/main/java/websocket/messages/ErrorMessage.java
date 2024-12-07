package websocket.messages;

public class ErrorMessage extends ServerMessage {

    private final String errorMsg;

    public ErrorMessage(ServerMessageType type, String errorMsg) {
        super(type);
        this.errorMsg = errorMsg;
    }

    public String getErrorMessage() {
        return errorMsg;
    }
}
