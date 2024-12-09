package websocket.commands;

import chess.ChessGame;

public class UserColorGameCommand extends UserGameCommand {

    private final String username;
    private final ChessGame.TeamColor color;

    public UserColorGameCommand(CommandType commandType, String authToken, Integer gameID, String username, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

}
