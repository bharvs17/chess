package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.DataAccessException;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.helpers.NOPLogger;
import service.AuthService;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserColorGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final AuthService authService;

    public WebSocketHandler(GameService gameService, AuthService authService) {
        this.gameService = gameService;
        this.authService = authService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand comm = new Gson().fromJson(message, UserGameCommand.class);
        if(comm.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            connect(command,session);
        } else if(comm.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            if(command.getGameID() == -99) {
                sendError(command,session);
            } else {
                resign(command, session);
            }
        } else if(comm.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            UserColorGameCommand command = new Gson().fromJson(message, UserColorGameCommand.class);
            leaveGame(command,session);
        } else { //make move
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            makeMove(command,session);
        }
    }

    private void connect(UserColorGameCommand command, Session session) throws IOException {
        try {
            gameService.getGame(command.getGameID());
            authService.checkAuth(command.getAuthToken());
        } catch(Exception ex) {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserColorGameCommand comm = new UserColorGameCommand(null,null,0,"Error: game doesn't exist or color already taken",null);
            sendError(comm,session);
            return;
        }
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
        ServerMessage.ServerMessageType loadType = ServerMessage.ServerMessageType.LOAD_GAME;
        ServerMessage loadGame = new LoadGameMessage(loadType,new ChessMove(new ChessPosition(1,1),new ChessPosition(2,2),null));
        session.getRemote().sendString(new Gson().toJson(loadGame));
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException {
        try {
            authService.checkAuth(command.getAuthToken());
            ChessGame.TeamColor color = gameService.getPlayerColor(command.getGameID(), authService.getUsername(command.getAuthToken()));
            ChessGame game = gameService.getGame(command.getGameID());
            System.out.println(game.getBoard().getPiece(command.getChessMove().getStartPosition()).getTeamColor());
            if(color != game.getBoard().getPiece(command.getChessMove().getStartPosition()).getTeamColor()) {
                throw new Exception("bad move");
            }
            game.makeMove(command.getChessMove());
            gameService.updateGame(command.getGameID(),game);
        } catch(Exception ex) {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserColorGameCommand comm = new UserColorGameCommand(null,null,0,"Error: invalid move",null);
            sendError(comm,session);
            return;
        }
        ChessPosition start = command.getChessMove().getStartPosition();
        ChessPosition end = command.getChessMove().getEndPosition();
        String user = command.getUsername();
        String message = String.format("Player %s made move %s to %s%n",user,start.toString(),end.toString());
        ServerMessage.ServerMessageType moveType = ServerMessage.ServerMessageType.LOAD_GAME;
        ServerMessage loadGame = new LoadGameMessage(moveType,command.getChessMove());
        connections.broadcast("",command.getGameID(),new Gson().toJson(loadGame));
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast(command.getAuthToken(), command.getGameID(),new Gson().toJson(notification));
    }

    private void leaveGame(UserColorGameCommand command, Session session) throws IOException {
        try {
            String username = authService.getUsername(command.getAuthToken());
            ChessGame.TeamColor color = gameService.getPlayerColor(command.getGameID(),username);
            if(color == ChessGame.TeamColor.WHITE) {
                gameService.removeUser(command.getGameID(),"white");
            } else if(color == ChessGame.TeamColor.BLACK) {
                gameService.removeUser(command.getGameID(),"black");
            }
        } catch (DataAccessException ex) {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserColorGameCommand comm = new UserColorGameCommand(null,null,0,"Error: invalid move",null);
            sendError(comm,session);
            return;
        }
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
        try {
            ChessGame game = gameService.getGame(command.getGameID());
            if(game.isGameOver()) {
                throw new DataAccessException(500, "Error: game already over");
            }
            String username = authService.getUsername(command.getAuthToken());
            ChessGame.TeamColor color = gameService.getPlayerColor(command.getGameID(), username);
            if(color == null) {
                throw new DataAccessException(500, "Error: observer tried resigning");
            }
            game.resign();
            gameService.updateGame(command.getGameID(), game);
        } catch (DataAccessException e) {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserColorGameCommand comm = new UserColorGameCommand(null,null,0,"Error: invalid move",null);
            sendError(comm,session);
            return;
        }
        String color = (command.getColor() == ChessGame.TeamColor.WHITE) ? "White" : "Black";
        String message = String.format("%s player %s has resigned. The game is now over\n",color,command.getUsername());
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;
        ServerMessage notification = new NotificationMessage(type,message);
        connections.broadcast("",command.getGameID(),new Gson().toJson(notification));
    }

    private void sendError(UserColorGameCommand command, Session session) throws IOException {
        //System.out.println("err message (username): " + command.getUsername());
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.ERROR;
        ErrorMessage err = new ErrorMessage(type,command.getUsername());
        session.getRemote().sendString(new Gson().toJson(err));
    }
}
