import chess.*;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLUserDAO;
import dataaccess.UserDAO;
import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import server.Server;
import service.UserService;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Server: " + piece);

        //Server chessServer = new Server();
        //chessServer.run(8080);

       /*SQLUserDAO dao = new SQLUserDAO();
       System.out.println(dao.loginUser(new LoginReq("bharvey","mypass")));*/

        /*SQLAuthDAO dao = new SQLAuthDAO();
        dao.deleteAllAuths();*/


    }
}