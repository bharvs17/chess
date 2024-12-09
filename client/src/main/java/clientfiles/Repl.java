package clientfiles;

import websocket.messages.NotificationMessage;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
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

}
