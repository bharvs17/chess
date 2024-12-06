package clientfiles;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import exception.DataAccessException;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(RegisterReq req) throws DataAccessException {
        String path = "/user";
        AuthData data = this.makeRequest("POST", path, authToken, req, AuthData.class);
        authToken = data.authToken();
        return data;
    }

    public AuthData login(LoginReq req) throws DataAccessException {
        String path = "/session";
        AuthData data = this.makeRequest("POST", path, authToken, req, AuthData.class);
        authToken = data.authToken();
        return data;
    }

    public void logout() throws DataAccessException {
        String path = "/session";
        this.makeRequest("DELETE", path, authToken, null, null);
    }

    public ListGameRes listGames() throws DataAccessException {
        String path = "/game";
        return this.makeRequest("GET", path, authToken, null, ListGameRes.class);
    }

    public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
        String path = "/game";
        return this.makeRequest("POST", path, authToken, req, CreateGameRes.class);
    }

    public void joinGame(JoinGameReq req) throws DataAccessException {
        String path = "/game";
        this.makeRequest("PUT", path, authToken, req, null);
    }

    public void deleteAll() throws DataAccessException {
        String path = "/db";
        this.makeRequest("DELETE", path, authToken, null, null);
    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        String path = "/game/" + gameID;
        return this.makeRequest("GET",path,authToken,null, ChessGame.class);
    }

    public void leaveGame(int gameID, String color) throws DataAccessException {
        String path = "/game/" + gameID + "/" + color;
        this.makeRequest("PUT",path,authToken,null,null);
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        String path = "/game/" + gameID;
        this.makeRequest("PUT",path,authToken,game,null);
    }

    private <T> T makeRequest(String method, String path, String token, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("authorization", token);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch(Exception ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
