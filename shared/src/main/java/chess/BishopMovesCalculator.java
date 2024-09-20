package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        int i = 1;

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            //check right up
            while(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col + i), null));
                i++;
            }
            if(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i, col + i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col + i), null));
            }
            //left up
            i = 1;
            while(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col - i), null));
                i++;
            }
            if(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i, col - i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col - i), null));
            }
            //right down
            i = 1;
            while(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col + i), null));
                i++;
            }
            if(row - i >= 1 && col + 1 <= 8 && board.getPieceRC(row - i, col + i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col + i), null));
            }
            //right left
            i = 1;
            while(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col - i), null));
                i++;
            }
            if(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i, col - i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col - i), null));
            }
        } else {
            //check right up
            while(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col + i), null));
                i++;
            }
            if(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i, col + i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col + i), null));
            }
            //left up
            i = 1;
            while(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col - i), null));
                i++;
            }
            if(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i, col - i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col - 1), null));
            }
            //right down
            i = 1;
            while(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col + i), null));
                i++;
            }
            if(row - i >= 1 && col + 1 <= 8 && board.getPieceRC(row - i, col + i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col + i), null));
            }
            //right left
            i = 1;
            while(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col - i), null));
                i++;
            }
            if(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i, col - i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col - i), null));
            }
        }

        return validMoves;
    }
}
