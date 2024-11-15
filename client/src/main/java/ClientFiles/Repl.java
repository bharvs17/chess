package ClientFiles;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess. Register or login to start.");
        //print out help
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("quit")) {
            System.out.print(">>> "); //check codes for making this look nicer
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result); //also make sure goes to next line and looks good
            } catch(Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

}
