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
Chess Web API Sequence Diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QqACYnE5Gt0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoEcDcvj3ZfF5vD7L9sgoU+YlqOtyfpOrYzM8+ivEsS5tsBgFFGqKDlAgR48rCh7Hqi6KxNiCaGC6YZumSFIGrSH6jLMP5LCaRLhhaHIwNyvIGoKwowKKowwOcMC0e8oYMW6naEeU7HaI6zoEiRLLlJ63pBrCsbBto9FMiJkbMdGMAqfGcpEcm5zAuU2E8tmuaYIhok3mmoEVhB1ZTCp87NlOfQHMWIKnNkPaVAOQ5vtxE7uU8LmNs2jltqu67eH4gReCg6AHkevjMKe6SZJgvlXshJS9tIACi+6FfUhXNC0T6qC+3Szq5aAAWyiHlHVEUNdZsooWhqW+lhPVgLhGIEQZGqyaSMDkmAKkBkG9XqWaEaFJaLE8jGQYcWErULkJGnSkm15dbp62SYRo3CXJMCwkgABmR1xtoMBoBAzCKsqmJGCg3CZNNelqTtC1MeU0ifRShi-fIUmJkZQIlthaUWQgebGR2+15SBMCebZnY5WAvYBcOvQY2YnCxZugSQra+7QjAADi46shl57ZZezCdfllQ06VFX2OOtWzW1jXQ6mLX89tHUHU68oTRSdOjKoWHQrLKCDfhkNETJ53jZN01beg82MVp5SsWt93yEKm2i+g-0G2z4J3apEOncRmvupdN324KT0vUqCDvcgsRK-L+uaUtzGRBYqA0Lxyq0+ODquntttSxUSuMo0auds1B6K+OCNIzD3kS+jfQ83L4yVP0pcoAAktI4xPGemQGhWpQADyxFAPL2PkTw6AgoANs3n6lE8VcAHLjqUewwI0mPUCjPks3jg4Ez0UxV6o5cVJX461+Xrc8mAPdTI3+pUfcvf9yAg-n-X6-jhPoxTzPK4k54cVbtgPhQNg3DwLqmRY48UZllHGbIvK9lqA0bmvNgiW1fL0ce4457HCFiZXi8DZg9CQaMAEyMkJiRgApTIStYRwAASgJWKssQZ2drtLWFIdbwODntUORtVoewepxXWDUE7mlRoQ8GwBaEa3oa7K6t0hGPWeo9H271iGUPHLCHBKBZhV1riw-hbDdI2iASgQUlExxyxgDyDISBgDKjCK9BAsw4CpBZErExrJgCPRQMkCa45NERiTqhDxoxa60LQSWchXoSG5zUJZDqbMQLqOkP8QWi8ci438ivEcsT4kxXfmTAIlhProXcQAKQgDyPRgQ+4D2Zkk8BtlIGUgfC0KufM6xtRHL-YAuSoBwAgOhKAajd7SBQSjIC+CRbNIXFgtpHSuk9L6f4gZVl8HVMOgAK2KWgUhRSeRUJQGiIaIiZBjVdtrJSPCvGAxWryKR3D4HW00j48oQj9l8PEe7KRXtZFvWlmAUhsSzmGwubaVOXCwixNuYnCWdsgWOxGnQha5QJF6M9jI6x70-BaDCaMZR-SaL9ymd06Asxgp-O0cbRFwK-E12kLMSk2B0WGFPjAaAQor43yMYYSAXFz6BOGQXUomz1nhJzIjBZBcbLzzskTLy2Ml4pMCoTV+a4snxQCF4dpXYvSwGANgX+hB4iJBSJlC8VTolpgqEVEqZUKrGEFjy4WMBNhgBAPY1kHLEAasQiK1MSzJa+JANwPAChlSMlhMNFCZ0xHlA4CDJu9ptAhrBVo9kQNo2GAsQgO0AoHoQB0MslA4ANBO1EXCj6X1FGjGDcSpNMgU3R3TVClx2bc35qeYcyNKahHxueec4Gpba2cLNo2vNYAC0wqCRKvBoqBE1NLJKrGqMcbLzlWvBVmAgA
