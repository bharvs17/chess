import dataaccess.*;
import server.Server;
import exception.DataAccessException;

public class ServerMain {
    public static void main(String[] args) throws DataAccessException {
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Server: " + piece);

        Server chessServer = new Server();
        chessServer.run(8080);


    }
}