package ClientFiles;

import exception.DataAccessException;

import java.util.Arrays;
import java.util.Locale;

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
                default -> help();
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
                    - implement this next phase
                    """;
        }
    }

    public String register(String... params) throws DataAccessException {

    }

    private void assertSignedIn() throws DataAccessException {
        if(state != State.SIGNEDIN) {
            throw new DataAccessException(400, "You must login");
        }
    }

    private void assertInGame() throws DataAccessException {
        if(state != State.INGAME) {
            throw new DataAccessException(400, "You must be in a game for this command");
        }
    }

}
