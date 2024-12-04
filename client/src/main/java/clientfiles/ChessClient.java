package clientfiles;

import chess.ChessGame;
import exception.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {

    private final String url;
    private final ServerFacade server;
    private State state;

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
            return switch(command) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "reset" -> resetDB(params);
                case "list" -> listGames(params);
                case "create" -> createGame(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help(); //connect to unknown command method and say it's an unknown command, then print out the help commands
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if(state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    """;
        } else if(state == State.SIGNEDIN) {
            return """
                    - logout
                    - list games
                    - create game <game name>
                    - play game <game number> <white/black>
                    - observe game <game number>
                    """;
        } else {
            return """
                    - redraw chess board
                    - leave
                    - make move <row,col> <row,col> <promotion piece>
                    - resign
                    - highlight legal moves
                    """;
        }
    }
    //format list games to look nice (say empty instead of null for open spots)
    //also make sure when listing games to put them in order and number in order should match id
    public String register(String... params) throws DataAccessException {
        if(params.length == 3) {
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
        if(params.length == 2) {
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
        if(params.length == 0) {
            server.logout();
            state = State.SIGNEDOUT;
            return "Successfully logged out.\n";
        } else {
            throw new DataAccessException(400, "Error: expected logout\n");
        }
    }

    public String resetDB(String... params) throws DataAccessException {
        if(state == State.SIGNEDOUT && params.length == 1 && params[0].equals("admin")) {
            server.deleteAll();
            return "Successfully deleted all data.\n";
        } else {
            return help();
        }
    }

    public String listGames(String... params) throws DataAccessException {
        assertSignedIn();
        int count = 1;
        StringBuilder result = new StringBuilder("Games:\n");
        if(params.length == 1 && params[0].equals("games")) {
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
        assertSignedIn();
        if(params.length >= 2 && params[0].equals("game")) {
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
        assertSignedIn();
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
                String result = "Successfully joined game\n";
                result = result + BoardPrinter.boardDefault();
                state = State.PLAYINGGAME;
                return result;
                //print out board (start with above message then append board to string and return that)
            } catch(Exception ex) {
                throw new DataAccessException(400, "Error joining game: game may not exist or your color was taken by another player\n");
            }
        } else {
            throw new DataAccessException(400, "Error: expected play game <game number> <white/black>\n");
        }
    }

    public String observeGame(String... params) throws DataAccessException {
        assertSignedIn();
        int reqNum = 0;
        if(params.length == 2 && params[0].equals("game")) {
            try {
                reqNum = Integer.parseInt(params[1]);
                ArrayList<GameInfo> games = getListOfGames();
                games.get(reqNum-1); //make sure valid number was entered
            } catch(Exception ex) {
                throw new DataAccessException(400, "Error: enter a valid game number\n");
            }
            state = State.OBSERVINGGAME;
            return BoardPrinter.boardDefault();
        } else {
            throw new DataAccessException(400, "Error: expected observe game <game number>\n");
        }
    }

    public String redrawChessBoard(String... params) throws DataAccessException {
        if(state != State.OBSERVINGGAME || state != State.PLAYINGGAME) {
            throw new DataAccessException(400, "Error: you must be playing or observing a game");
        }
    }

    public String leave(String... params) throws DataAccessException {
        if(state != State.OBSERVINGGAME || state != State.PLAYINGGAME) {
            throw new DataAccessException(400, "Error: you must be playing or observing a game");
        }
    }

    public String makeMove(String... params) throws DataAccessException {
        if(state != State.PLAYINGGAME) {
            throw new DataAccessException(400, "Error: you must be playing the game to do that");
        }
    }

    public String resign(String... params) throws DataAccessException {
        if(state != State.PLAYINGGAME) {
            throw new DataAccessException(400, "Error: you must be playing the game to do that");
        }
    }

    public String highlightMoves(String... params) throws DataAccessException {
        if(state != State.OBSERVINGGAME || state != State.PLAYINGGAME) {
            throw new DataAccessException(400, "Error: you must be playing or observing a game");
        }
    }

    private void assertSignedIn() throws DataAccessException {
        if(state != State.SIGNEDIN) {
            throw new DataAccessException(400, "You must be logged in and not in a game to do that.\n");
        }
    }

}
