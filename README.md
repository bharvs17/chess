# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
Chess Web API Sequence Diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-MwEAA
