package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int i = 1;
        //can about half the code needed by just seeing if piece at position has opposite color to piece it's trying to move to and then adding to validMoves
        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            //ROOK CODE
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
            //BISHOP CODE
            i = 1;
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
            if(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i, col + i).getTeamColor() == ChessGame.TeamColor.BLACK) {
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
            //ROOK CODE
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
            //BISHOP CODE
            i = 1;
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
                validMoves.add(new ChessMove(position, new ChessPosition(row + i, col - i), null));
            }
            //right down
            i = 1;
            while(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i, col + i) == null) {
                validMoves.add(new ChessMove(position, new ChessPosition(row - i, col + i), null));
                i++;
            }
            if(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i, col + i).getTeamColor() == ChessGame.TeamColor.WHITE) {
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
