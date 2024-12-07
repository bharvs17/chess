package clientfiles.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.DataAccessException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler notificationHandler;

    public WebSocketFacade(String url, ServerMessageHandler handler) throws DataAccessException {
        try {
            url = url.replace("http","ws");
            URI socketURI = new URI(url + "/ws");
            notificationHandler = handler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    NotificationMessage notif = new Gson().fromJson(message, NotificationMessage.class);
                    notificationHandler.notify(notif);
                }
            });
        } catch(DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    //methods to be called from client to server (user game commands)
    public void connectToGame(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.CONNECT;
            UserGameCommand command = new UserGameCommand(type,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.MAKE_MOVE;
            UserGameCommand command = new MakeMoveCommand(type, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.LEAVE;
            UserGameCommand command = new UserGameCommand(type,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void resignFromGame(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserGameCommand command = new UserGameCommand(type,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }
}
