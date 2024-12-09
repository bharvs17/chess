package websocket.messages;

import chess.ChessMove;

public class LoadGameMessage extends ServerMessage {

    private final ChessMove game;

    public LoadGameMessage(ServerMessageType type, ChessMove game) {
        super(type);
        this.game = game;
    }

    public ChessMove getGame() {
        return game;
    }
}
