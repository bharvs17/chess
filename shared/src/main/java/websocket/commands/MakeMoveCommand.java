package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;
    private final String username;
    private final ChessGame.TeamColor color;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, String username, ChessMove move, ChessGame.TeamColor color) {
        super(commandType, authToken, gameID);
        this.move = move;
        this.username = username;
        this.color = color;
    }

    public ChessMove getChessMove() {
        return move;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

}
