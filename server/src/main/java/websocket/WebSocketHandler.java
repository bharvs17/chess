package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(),session);
            case MAKE_MOVE -> makeMove()
        }
    }

    //may want to find way to easily use username over authToken
    //also probably supposed to use the error message if exception thrown

    private void connect(String authToken, Session session) throws IOException {
        connections.add(authToken, session);
        //need to fix this to be more specific (get username and specify if player or observer)
        String message = String.format("%s joined the game",authToken);
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notif = new NotificationMessage(type,message);
        connections.broadcast(authToken, notif);
        //may need to broadcast load game to joining player, but should client side
        //should already load game from db
    }

    private void makeMove(String authToken, Session session, ChessMove move) throws IOException {
        //update these to be letter number
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        String message = String.format("%s made move %d,%d to %d,%d",authToken,startRow,startCol,endRow,endCol);
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notif = new NotificationMessage(type,message);
        connections.broadcast(authToken,notif);
        //also need to broadcast load game
    }

    private void leaveGame(String authToken, Session session) throws IOException {
        connections.remove(authToken);
        String message = String.format("User %s has left the game",authToken);
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notif = new NotificationMessage(type,message);
        connections.broadcast(authToken,notif);
    }

    private void resign(String authToken, Session session) throws IOException {
        String message = String.format("User %s has resigned. The game is over.");
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notif = new NotificationMessage(type,message);
        connections.broadcast(authToken,notif);
        //probably also need to broadcast a load game message so other clients
        //update to know the game is over (if game isn't loaded after someone resigns then
        //other player should be able to make 1 more move and will screw things up)
    }
}
