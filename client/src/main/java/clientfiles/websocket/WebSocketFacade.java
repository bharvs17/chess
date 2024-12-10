package clientfiles.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.DataAccessException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserColorGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler msgHandler;

    public WebSocketFacade(String url, ServerMessageHandler handler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            msgHandler = handler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    if(msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        msgHandler.notify(new Gson().fromJson(message,NotificationMessage.class));
                    } else if(msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        msgHandler.loadGame(new Gson().fromJson(message,LoadGameMessage.class));
                    } else {
                        msgHandler.error(new Gson().fromJson(message, ErrorMessage.class));
                    }
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
    public void connectToGame(String authToken, int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.CONNECT;
            //UserGameCommand command = new UserColorGameCommand(type,authToken,gameID,username,color);
            UserGameCommand command = new UserColorGameCommand(type,authToken,gameID,username,color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command)); //problem here
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, String username, ChessMove move) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.MAKE_MOVE;
            UserGameCommand command = new MakeMoveCommand(type, authToken, gameID, username, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.LEAVE;
            UserGameCommand command = new UserColorGameCommand(type,authToken,gameID,username,color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void resignFromGame(String authToken, int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserGameCommand command = new UserColorGameCommand(type,authToken,gameID,username,color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    public void error(String auth) throws DataAccessException {
        try {
            UserGameCommand.CommandType type = UserGameCommand.CommandType.RESIGN;
            UserGameCommand command = new UserColorGameCommand(type,auth,-99,"Error: game doesn't exist or selected color was already taken",null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

}
