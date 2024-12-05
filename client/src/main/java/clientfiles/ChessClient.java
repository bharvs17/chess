package clientfiles;

import chess.ChessGame;
import chess.ChessMove;
import exception.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {

    private final String url;
    private final ServerFacade server;
    private State state;
    private ChessGame currentGame;
    private ChessGame.TeamColor currentColor;

    public ChessClient(String serverUrl) {
        url = serverUrl;
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
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
                server.register(new RegisterReq(params[0], params[1], params[2]));
                state = State.SIGNEDIN;
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
                server.login(new LoginReq(params[0],params[1]));
                state = State.SIGNEDIN;
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
            state = State.SIGNEDOUT;
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
        if(params.length == 3 && params[0].equals("game")) {
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
                server.joinGame(new JoinGameReq(color, gameID));
                //now need to find a way to get the chess game json, deserialize, and update currentGame to that
                String result = "Successfully joined game\n";
                currentColor = color;
                result = result + BoardPrinter.boardString(currentGame, currentColor);
                state = State.PLAYINGGAME;
                return result;
            } catch(Exception ex) {
                throw new DataAccessException(400, "Error joining game: game may not exist or your color was taken by another player\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected play game <game number> <white/black>\n");
        }
    }

    public String observeGame(String... params) throws DataAccessException {
        int reqNum = 0;
        if(params.length == 2 && params[0].equals("game")) {
            try {
                reqNum = Integer.parseInt(params[1]);
                ArrayList<GameInfo> games = getListOfGames();
                games.get(reqNum-1); //makes sure a valid number was entered
                //MAKE SURE to get requested game from db and update currentGame
            } catch(Exception ex) {
                throw new DataAccessException(400, "Error: enter a valid game number\n");
            }
            state = State.OBSERVINGGAME;
            currentColor = ChessGame.TeamColor.WHITE;
            return BoardPrinter.boardString(currentGame, currentColor);
        } else {
            throw new DataAccessException(400, "Error: expected observe game <game number>\n");
        }
    }

    public String redrawChessBoard(String... params) throws DataAccessException {
        if(params[0].equals("board")) {
            return BoardPrinter.boardString(currentGame,currentColor);
        } else {
            throw new DataAccessException(400, "Error: did you mean 'redraw board'?");
        }
    }

    public String leave(String... params) throws DataAccessException {
        if(state == State.OBSERVINGGAME) {
            state = State.SIGNEDIN;
        } else {
            state = State.SIGNEDIN;
            //using currentColor (and maybe id? idk) update the game in the db so the player at the currentColor is null
        }
    }

    public String makeMove(String... params) throws DataAccessException {
        if(params.length < 3 || params.length > 4) {
            throw new DataAccessException(400, "Error: expected make move #,# #,#");
        }
        if(!params[0].equals("move")) {
            throw new DataAccessException(400, "Error: expected make move #,# #,#");
        }
        if(params[1].length() != 3 && params[2].length() != 3) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# where # is a single digit number");
        }
        int startRow;
        int startCol;
        int endRow;
        int endCol;
        try {
            startRow = Integer.parseInt(String.valueOf(params[1].charAt(0)));
            startCol = Integer.parseInt(String.valueOf(params[1].charAt(2)));
            endRow = Integer.parseInt(String.valueOf(params[2].charAt(0)));
            endCol = Integer.parseInt(String.valueOf(params[2].charAt(2)));
        } catch(Exception ex) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# where # is a single digit number");
        }
        if(startRow < 1 || startRow > 8 || startCol < 1 || startCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- row and column number must be between 1-8");
        }
        if(endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- row and column number must be between 1-8");
        }

        //if piece at start is a pawn AND moves to final row then get the promo piece (if none given then give error and tell player to type a piece type)
        //dont say it's an error- say since they are moving pawn to final row they need to provide a promotion piece type

    }

    public String resign(String... params) throws DataAccessException {

    }

    public String highlightMoves(String... params) throws DataAccessException {
        //make sure given position is valid and has a piece there
        //then use currentGame to get the list of valid moves
        //then use board printer and print board with the highlighted squares
    }

}
