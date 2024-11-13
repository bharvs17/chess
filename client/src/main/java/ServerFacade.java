import model.*;
import

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(RegisterReq req) {
        String path = "/user";

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws  {

    }

}
