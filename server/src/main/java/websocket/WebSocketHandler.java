package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.helpers.NOPLogger;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserColorGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand comm = new Gson().fromJson(message, UserGameCommand.class);
        if(comm.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            connect(command,session);
        } else if(comm.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            resign(command,session);
        } else if(comm.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            leaveGame(command,session);
        } else { //make move
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            makeMove(command,session);
        }
    }

    private void connect(UserColorGameCommand command, Session session) throws IOException {
        connections.add(command.getAuthToken(),command.getGameID(),session);
        String message;
        if(command.getColor() != null) {
            String color = (command.getColor() == ChessGame.TeamColor.WHITE) ? "white" : "black";
            message = String.format("Player %s has joined as %s%n",command.getUsername(),color);
        } else {
            message = String.format("User %s started observing the game%n",command.getUsername());
        }
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast(command.getAuthToken(),command.getGameID(),new Gson().toJson(notification));
        //this should be good (should get the game status in ChessClient), now implement for make move, leave, resign
        //also need to make sure Repl class prints server messages properly
        //and then need to make sure load game messages make client send request to server to get latest chess game data
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException {
        ChessPosition start = command.getChessMove().getStartPosition();
        ChessPosition end = command.getChessMove().getEndPosition();
        String user = command.getUsername();
        String message = String.format("Player %s made move %s to %s%n",user,start.toString(),end.toString());
        ServerMessage.ServerMessageType moveType = ServerMessage.ServerMessageType.LOAD_GAME;
        ServerMessage loadGame = new LoadGameMessage(moveType,command.getChessMove());
        connections.broadcast(command.getAuthToken(),command.getGameID(),new Gson().toJson(loadGame));
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast(command.getAuthToken(), command.getGameID(),new Gson().toJson(notification));
    }

    private void leaveGame(UserColorGameCommand command, Session session) throws IOException {
        connections.remove(command.getAuthToken());
        String message;
        if(command.getColor() == ChessGame.TeamColor.BLACK || command.getColor() == ChessGame.TeamColor.WHITE) {
            String color = (command.getColor() == ChessGame.TeamColor.WHITE) ? "White" : "Black";
            message = String.format("%s player %s has left the game\n",color,command.getUsername());
        } else {
            message = String.format("Observer %s has left the game\n",command.getUsername());
        }
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast(command.getAuthToken(),command.getGameID(),new Gson().toJson(notification));
    }

    private void resign(UserColorGameCommand command, Session session) throws IOException {
        String color = (command.getColor() == ChessGame.TeamColor.WHITE) ? "White" : "Black";
        String message = String.format("%s player %s has resigned. The game is now over\n",color,command.getUsername());
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast(command.getAuthToken(),command.getGameID(),new Gson().toJson(notification));
    }
}
