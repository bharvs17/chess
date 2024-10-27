package chess.movecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();

       int row = position.getRow();
       int col = position.getColumn();

       if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
           if(row == 2 && board.getPieceRC(row + 2, col) == null && board.getPieceRC(row + 1, col) == null) {
               validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col),null));
           }
           if(row < 8) {
               if (board.getPieceRC(row + 1, col) == null) {
                   if (row + 1 == 8) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                   }
               }
           }
           if(row < 8 && col > 1) {
               if(board.getPieceRC(row + 1,col - 1) != null && board.getPieceRC(row + 1,col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                   if(row + 1 == 8) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                   }
               }
           }
           if(row < 8 && col < 8) {
               if(board.getPieceRC(row + 1, col + 1) != null && board.getPieceRC(row + 1, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                   if(row + 1 == 8) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                   }
               }
           }
       }
       else {
           if(row == 7 && board.getPieceRC(row - 2, col) == null && board.getPieceRC(row - 1, col) == null) {
               validMoves.add(new ChessMove(position, new ChessPosition(row - 2,col), null));
           }
           if(row > 1) {
               if(board.getPieceRC(row - 1, col) == null) {
                   if(row - 1 == 1) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col), null));
                   }
               }
           }
           if(row > 1 && col > 1) {
               if(board.getPieceRC(row - 1, col - 1) != null && board.getPieceRC(row - 1, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                   if(row - 1 == 1) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col - 1), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col - 1), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col - 1), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col - 1), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col - 1), null));
                   }
               }
           }
           if(row > 1 && col < 8) {
               if(board.getPieceRC(row - 1, col + 1) != null && board.getPieceRC(row - 1, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                   if(row - 1 == 1) {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col + 1), ChessPiece.PieceType.QUEEN));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col + 1), ChessPiece.PieceType.ROOK));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col + 1), ChessPiece.PieceType.KNIGHT));
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col + 1), ChessPiece.PieceType.BISHOP));
                   } else {
                       validMoves.add(new ChessMove(position, new ChessPosition(row - 1,col + 1), null));
                   }
               }
           }
       }

        return validMoves;
    }
}
