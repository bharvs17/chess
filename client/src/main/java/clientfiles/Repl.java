package clientfiles;

import clientfiles.websocket.ServerMessageHandler;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements ServerMessageHandler {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to chess. Register or login to start.");
        System.out.print(client.help());
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("quit")) {
            System.out.print(">>> "); //check codes for making this look nicer
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch(Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        System.out.println("Quitting chess application.");
    }

    public void notify(NotificationMessage msg) {
        System.out.println(msg.getNotification());
        //may need to fix text color after this
    }

}
