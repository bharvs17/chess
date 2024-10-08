package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        int i = 1;

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            //up
            while (row + i <= 8 && board.getPieceRC(row + i, col) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col), null));
                i++;
            }
            if(row + i <= 8 && board.getPieceRC(row + i, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col), null));
            }
            //down
            i = 1;
            while (row - i >= 1 && board.getPieceRC(row - i, col) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col), null));
                i++;
            }
            if(row - i >= 1 && board.getPieceRC(row - i, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col), null));
            }
            //right
            i = 1;
            while (col + i <= 8 && board.getPieceRC(row, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col + i), null));
                i++;
            }
            if(col + i <= 8 && board.getPieceRC(row, col + i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col + i), null));
            }
            //left
            i = 1;
            while (col - i >= 1 && board.getPieceRC(row, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col - i), null));
                i++;
            }
            if(col - i >= 1 && board.getPieceRC(row, col - i).getTeamColor() == ChessGame.TeamColor.BLACK) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col - i), null));
            }
        } else {
            //up
            while (row + i <= 8 && board.getPieceRC(row + i, col) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col), null));
                i++;
            }
            if(row + i <= 8 && board.getPieceRC(row + i, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col), null));
            }
            //down
            i = 1;
            while (row - i >= 1 && board.getPieceRC(row - i, col) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col), null));
                i++;
            }
            if(row - i >= 1 && board.getPieceRC(row - i, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col), null));
            }
            //right
            i = 1;
            while (col + i <= 8 && board.getPieceRC(row, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col + i), null));
                i++;
            }
            if(col + i <= 8 && board.getPieceRC(row, col + i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col + i), null));
            }
            //left
            i = 1;
            while (col - i >= 1 && board.getPieceRC(row, col - i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col - i), null));
                i++;
            }
            if(col - i >= 1 && board.getPieceRC(row, col - i).getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.add(new ChessMove(position, new ChessPosition(row, col - i), null));
            }
        }

        return validMoves;
    }
}
