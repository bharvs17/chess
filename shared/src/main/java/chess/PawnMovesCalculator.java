package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

       int row = position.getRow();
       int col = position.getColumn();

       if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
           if(row == 2) { //can move up 2 if on starting row
               validMoves.add(new ChessMove(position, new ChessPosition(row+2,col),null));
           }

       }
       else {
           if(row == 7) {
               validMoves.add(new ChessMove(position, new ChessPosition(row-2,col),null));
           }
       }

        return validMoves;
    }
}
