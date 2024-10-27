package chess.move_calculators;

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
           //Can move up 2 if on starting row
           if(row == 2 && board.getPieceRC(row + 2, col) == null && board.getPieceRC(row + 1, col) == null) {
               validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col),null));
           }
           //Check if space right ahead is open (and not out of bounds, shouldn't be since a pawn on the end row would be promoted and not a pawn anymore)
           if(row < 8) {
               if (board.getPieceRC(row + 1, col) == null) {
                   //Check if pawn goes to back row so need to get promotion pieces
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
           //Check if diagonal left has enemy piece and not out of bounds
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
           //check if diagonal right has enemy piece and not out of bounds
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
       //checks for black pieces
       else {
           //check for initial move of 2 spaces
           if(row == 7 && board.getPieceRC(row - 2, col) == null && board.getPieceRC(row - 1, col) == null) {
               validMoves.add(new ChessMove(position, new ChessPosition(row - 2,col), null));
           }
           //check directly in front
           if(row > 1) {
               if(board.getPieceRC(row - 1, col) == null) {
                   //check if on bottom row
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
           //check left diagonal
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
           //check right diagonal
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
