package websocket.commands;

import chess.ChessGame;

public class GameStatusCommand extends UserGameCommand {

    private ChessGame game;

    public GameStatusCommand(CommandType commandType, String authToken, Integer gameID, ChessGame game) {
        super(commandType, authToken, gameID);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
