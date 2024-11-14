import com.google.gson.Gson;
import model.*;
import exception.DataAccessException;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(RegisterReq req) throws DataAccessException {
        String path = "/user";
        return this.makeRequest("POST", path, req, AuthData.class);
    }

    public AuthData login(LoginReq req) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("POST", path, req, AuthData.class);
    }

    public void logout() throws DataAccessException {
        String path = "/session";
        //need to figure out how to do this
    }

    public ListGameRes listGames() throws DataAccessException {
        String path = "/game";
    }

    public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
        String path = "/game";
    }

    public void joinGame(JoinGameReq req) throws DataAccessException {
        String path = "/game";
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            //http.addRequestProperty("authorization", authToken); add header with authToken under name "authorization"
            //probably store authToken as a private final string and add it to header for all methods that need it
            //look more in depth at server class to see how it handles authTokens/headers, but probably do above
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
