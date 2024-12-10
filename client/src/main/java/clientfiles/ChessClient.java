package clientfiles;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import clientfiles.websocket.ServerMessageHandler;
import clientfiles.websocket.WebSocketFacade;
import exception.DataAccessException;
import model.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.MessageHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient implements ServerMessageHandler {

    private final String url;
    private final ServerFacade server;
    private WebSocketFacade ws;
    private State state;
    private ChessGame currentGame;
    private ChessGame.TeamColor currentColor;
    int currID;
    String currAuth;
    String currUsername;
    boolean success;

    //instead of storing data locally, should have created private method in this class to get any necessary data from server
    //(maybe just store username here, and use that in method calls to get the ChessGame or auth token)

    public ChessClient(String serverUrl) {
        url = serverUrl;
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
        success = true;
        ws = null;
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state == State.SIGNEDOUT) {
                return switch(command) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "reset" -> resetDB(params);
                    case "quit" -> "quit";
                    case "help" -> help();
                    default -> "Unknown or invalid command. Here is a list of valid commands:\n" + help();
                };
            } else if(state == State.SIGNEDIN) {
                return switch(command) {
                    case "logout" -> logout(params);
                    case "list" -> listGames(params);
                    case "create" -> createGame(params);
                    case "play" -> playGame(params);
                    case "observe" -> observeGame(params);
                    case "help" -> help();
                    default -> "Unknown or invalid command. Here is a list of valid commands:\n" + help();
                };
            } else if(state == State.PLAYINGGAME) {
                return switch(command) {
                    case "redraw" -> redrawChessBoard(params);
                    case "leave" -> leave(params);
                    case "make" -> makeMove(params);
                    case "resign" -> resign(params);
                    case "highlight" -> highlightMoves(params);
                    case "help" -> help();
                    default -> "Unknown or invalid command. Here is a list of valid commands:\n" + help();
                };
            } else {
                return switch(command) {
                    case "redraw" -> redrawChessBoard(params);
                    case "leave" -> leave(params);
                    case "highlight" -> highlightMoves(params);
                    case "help" -> help();
                    default -> "Unknown or invalid command. Here is a list of valid commands:\n" + help();
                };
            }
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if(state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - help
                    - quit
                    """;
        } else if(state == State.SIGNEDIN) {
            return """
                    - logout
                    - list games
                    - create game <game name>
                    - play game <game number> <white/black>
                    - observe game <game number>
                    - help
                    """;
        } else if(state == State.PLAYINGGAME) {
            return """
                    - redraw board
                    - leave
                    - make move <row,col> <row,col>
                    - resign
                    - highlight legal moves <row,col>
                    - help
                    """;
        } else {
            return """
                    - redraw board
                    - leave
                    - highlight legal moves <row,col>
                    - help
                    """;
        }
    }
    //format list games to look nice (say empty instead of null for open spots)
    //also make sure when listing games to put them in order and number in order should match id
    public String register(String... params) throws DataAccessException {
        if(ValidInputChecker.checkRegister(params)) {
            try {
                currAuth = server.register(new RegisterReq(params[0], params[1], params[2])).authToken();
                state = State.SIGNEDIN;
                currUsername = params[0];
                ws = new WebSocketFacade(url, this);
                return String.format("Registered new user: %s\n", params[0]);
            } catch(DataAccessException ex) {
                throw new DataAccessException(400, "Error: username already taken\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected <username> <password> <email>\n");
        }
    }

    public String login(String... params) throws DataAccessException {
        if(ValidInputChecker.checkLogin(params)) {
            try {
                currAuth = server.login(new LoginReq(params[0],params[1])).authToken();
                state = State.SIGNEDIN;
                currUsername = params[0];
                if(ws == null) {
                    ws = new WebSocketFacade(url,this);
                }
                return String.format("Successfully logged in user: %s\n", params[0]);
            } catch(DataAccessException ex) {
                throw new DataAccessException(400, "Error: no such user in database or wrong password.\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected <username> <password>\n");
        }
    }

    public String logout(String... params) throws DataAccessException {
        if(ValidInputChecker.checkLogout(params)) {
            server.logout();
            currAuth = null;
            state = State.SIGNEDOUT;
            currUsername = null; //be aware of currID and other curr global vars
            return "Successfully logged out.\n";
        } else {
            throw new DataAccessException(400, "Error: expected logout\n");
        }
    }

    public String resetDB(String... params) throws DataAccessException {
        if(ValidInputChecker.checkReset(params)) {
            server.deleteAll();
            return "Successfully deleted all data.\n";
        } else {
            return help();
        }
    }

    public String listGames(String... params) throws DataAccessException {
        int count = 1;
        StringBuilder result = new StringBuilder("Games:\n");
        if(ValidInputChecker.checkListGames(params)) {
            ArrayList<GameInfo> games = (ArrayList<GameInfo>) server.listGames().games();
            for(GameInfo game : games) {
                String whiteUser = "empty";
                String blackUser = "empty";
                if(game.whiteUsername() != null) {
                    whiteUser = game.whiteUsername();
                }
                if(game.blackUsername() != null) {
                    blackUser = game.blackUsername();
                }
                result.append(count).append(": Game Name: ").append(game.gameName()).append(" | white: ");
                result.append(whiteUser).append(" | black: ").append(blackUser).append("\n");
                count++;
            }
            return result.toString();
        } else {
            throw new DataAccessException(400, "Error: expected list games\n");
        }
    }

    public String createGame(String... params) throws DataAccessException {
        if(ValidInputChecker.checkMakeGame(params)) {
            StringBuilder nameBuilder = new StringBuilder();
            for(int i = 1; i < params.length; i++) {
                nameBuilder.append(params[i]);
                if(i != params.length-1) {
                    nameBuilder.append(" ");
                }
            }
            String gameName = nameBuilder.toString();
            try {
                CreateGameRes res = server.createGame(new CreateGameReq(gameName));
                return String.format("Successfully made new game: %s\n", gameName);
            } catch(DataAccessException ex) {
                throw new DataAccessException(400, "Error: Game with that name already exists in database.\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected create game <game name>\n");
        }
    }

    private ArrayList<GameInfo> getListOfGames() throws DataAccessException {
        ArrayList<GameInfo> games = new ArrayList<>();
        games = (ArrayList<GameInfo>) server.listGames().games();
        return games;
    }

    public String playGame(String... params) throws DataAccessException {
        if(ValidInputChecker.checkPlayGame(params)) {
            ChessGame.TeamColor color;
            int reqNum = 0;
            if(params[2].equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if(params[2].equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new DataAccessException(400, "Error: expected play game <game number> <white/black>\n");
            }
            try {
                reqNum = Integer.parseInt(params[1]);
            } catch (Exception ex) {
                throw new DataAccessException(400, "Error: expected play game <game number> <white/black>\n");
            }
            try {
                ArrayList<GameInfo> games = getListOfGames();
                int gameID = games.get(reqNum-1).gameID();
                currID = gameID;
                server.joinGame(new JoinGameReq(color, gameID));
                currentGame = server.getGame(gameID);
                currentColor = color;
                //String result = BoardPrinter.boardString(currentGame, currentColor, false);
                String result = "\nSuccessfully joined game\n";
                state = State.PLAYINGGAME;
                ws.connectToGame(currAuth,currID,currUsername,currentColor); //issue here
                return result;
            } catch(Exception ex) {
                //throw new DataAccessException(400, "Error joining game: game may not exist or your color was taken by another player\n");
                ws.error(currAuth);
                return "";
            }
        } else {
            throw new DataAccessException(400, "Error: expected play game <game number> <white/black>\n");
        }
    }

    public String observeGame(String... params) throws DataAccessException {
        int reqNum = 0;
        if(ValidInputChecker.checkObserveGame(params)) {
            try {
                reqNum = Integer.parseInt(params[1]);
                ArrayList<GameInfo> games = getListOfGames();
                int gameID = games.get(reqNum - 1).gameID();
                currID = gameID;
                currentGame = server.getGame(gameID);
                currentColor = null;
                ws.connectToGame(currAuth, currID, currUsername, null);
                state = State.OBSERVINGGAME;
                //return BoardPrinter.boardString(currentGame, currentColor, false);
                return "";
            } catch(Exception ex) {
                throw new DataAccessException(400, "Error: enter a valid game number\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected observe game <game number>\n");
        }
    }

    public String redrawChessBoard(String... params) throws DataAccessException {
        if(ValidInputChecker.checkRedrawBoard(params)) {
            return BoardPrinter.boardString(currentGame,currentColor, false);
        } else {
            throw new DataAccessException(400, "Error: did you mean 'redraw board'?\n");
        }
    }

    public String leave(String... params) throws DataAccessException {
        if(state == State.OBSERVINGGAME) {
            state = State.SIGNEDIN; //ALSO shouldn't receive notification updates when just signed in
            ws.leaveGame(currAuth,currID,currUsername,null);
            currentGame = null;
            currentColor = null;
            currID = -1;
            return "Successfully left game\n";
        } else {
            state = State.SIGNEDIN;
            //using currentColor (and maybe id? idk) update the game in the db so the player at the currentColor is null
            if(currentColor == ChessGame.TeamColor.WHITE) {
                server.leaveGame(currID, "white");
            } else {
                server.leaveGame(currID, "black");
            }
            ws.leaveGame(currAuth,currID,currUsername,currentColor);
            currentColor = null;
            currentGame = null;
            currID = -1;
            //ws = null;
            return "Successfully left game\n";
        }
    }

    public String makeMove(String... params) throws DataAccessException {
        currentGame = server.getGame(currID); //should prevent other player from making a move after other player resigns
        ChessMove move = ValidInputChecker.checkMakeMove(currentGame,params);
        if(currentGame.isGameOver()) {
            throw new DataAccessException(400, "Error: the game is over. No moves can be made\n");
        }
        if(currentGame.getTeamTurn() != currentColor) {
            throw new DataAccessException(400, "Error: Invalid move- either it is not your turn OR you cannot move the other team's piece\n");
        }
        try {
            //currentGame.makeMove(move); //really should be sending the chess move to server facade and then having server/sqlgamedao deal with that
            //server.updateGame(currID, currentGame);
            ws.makeMove(currAuth,currID,currUsername,move,currentColor);
            /*String result = "Successfully made move " + move.getStartPosition().toString();
            result = result + " to " + move.getEndPosition().toString() + "\n";
            result = result + statusChecker(currentGame);*/
            return "";
        } catch(Exception ex) {
            throw new DataAccessException(400, ex.getMessage() + "\n");
        }

    }

    public String resign(String... params) throws DataAccessException {
        if(currentGame.isGameOver()) {
            return "The game is already over\n";
        }
        if(currentGame.getTeamTurn() != currentColor) {
            return "Wait until your turn to resign\n";
        }
        System.out.println("Are you sure you want to forfeit the match? Enter 'confirm' to do so, enter anything else to cancel");
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String line = scanner.nextLine();
        if(line.equalsIgnoreCase("confirm")) {
            try {
                //currentGame.resign();
                //server.updateGame(currID,currentGame);
                ws.resignFromGame(currAuth,currID,currUsername,currentColor);
                return "Successfully resigned.\n";
            } catch (Exception ex) {
                throw new DataAccessException(400, "Error: could not resign for some reason: " + ex.getMessage() + "\n");
            }
        } else {
            return "Did not resign\n";
        }
    }

    public String highlightMoves(String... params) throws DataAccessException {
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) ValidInputChecker.checkHighlightMoves(currentGame,params);
        int row = Integer.parseInt(String.valueOf(params[2].charAt(1)));
        int col = ValidInputChecker.colConverter(String.valueOf(params[2].charAt(0)));
        return BoardPrinter.highlightMoves(currentGame, currentColor, row, col, moves);
    }

    public void notify(ServerMessage msg) {
        NotificationMessage message = (NotificationMessage) msg;
        if(message.getNotification().contains("resign")) {
            currentGame.resign();
            System.out.println(BoardPrinter.boardString(currentGame,currentColor,false));
        }
        System.out.print(message.getNotification());
        System.out.print(">>> ");
    }

    public void loadGame(ServerMessage msg) {
        LoadGameMessage loadGame = (LoadGameMessage) msg;
        try {
            System.out.println("");
            updateBoard(loadGame.getGame());
            System.out.print(statusChecker(currentGame));
            System.out.print(">>> ");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void error(ServerMessage msg) {
        ErrorMessage err = (ErrorMessage) msg;
        System.out.println(err.getErrorMessage());
        System.out.print(">>> ");
    }

    private void updateBoard(ChessMove move) throws DataAccessException {
        currentGame = server.getGame(currID);
        System.out.println(redrawChessBoard("board"));
    }

    private String statusChecker(ChessGame game) {
        String statusMsg = "";
        if(game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            statusMsg = "White is in checkmate. Black wins\n";
        } else if(game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            statusMsg = "Black is in checkmate. White wins\n";
        } else if(game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            statusMsg = "White is in stalemate. The game ends in a draw\n";
        } else if(game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            statusMsg = "Black is in stalemate. The game ends in a draw\n";
        } else if(game.isInCheck(ChessGame.TeamColor.WHITE)) {
            statusMsg = "White is in check\n";
        } else if(game.isInCheck(ChessGame.TeamColor.BLACK)) {
            statusMsg = "Black is in check\n";
        }
        return statusMsg;
    }

}
