package chess.move_calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            //upper
            if(row + 2 <= 8 && col + 1 <= 8) {
                if(board.getPieceRC(row + 2, col + 1) == null || board.getPieceRC(row + 2, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col + 1), null));
                }
            }
            if(row + 2 <= 8 && col - 1 >= 1) {
                if(board.getPieceRC(row + 2, col - 1) == null || board.getPieceRC(row + 2, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col - 1), null));
                }
            }
            //right
            if(row + 1 <= 8 && col + 2 <= 8) {
                if(board.getPieceRC(row + 1, col + 2) == null || board.getPieceRC(row + 1, col + 2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 2), null));
                }
            }
            if(row - 1 >= 1 && col + 2 <= 8) {
                if(board.getPieceRC(row - 1, col + 2) == null || board.getPieceRC(row - 1, col + 2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 2), null));
                }
            }
            //lower
            if(row - 2 >= 1 && col + 1 <= 8) {
                if(board.getPieceRC(row - 2, col + 1) == null || board.getPieceRC(row - 2, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 2, col + 1), null));
                }
            }
            if(row - 2 >= 1 && col - 1 >= 1) {
                if(board.getPieceRC(row - 2, col - 1) == null || board.getPieceRC(row - 2, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row -2 , col - 1), null));
                }
            }
            //left
            if(row + 1 <= 8 && col - 2 >= 1) {
                if(board.getPieceRC(row + 1, col - 2) == null || board.getPieceRC(row + 1, col - 2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 2), null));
                }
            }
            if(row - 1 >= 1 && col - 2 >= 1) {
                if(board.getPieceRC(row - 1, col - 2) == null || board.getPieceRC(row - 1, col - 2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 2), null));
                }
            }
        } else {
            //upper
            if(row + 2 <= 8 && col + 1 <= 8) {
                if(board.getPieceRC(row + 2, col + 1) == null || board.getPieceRC(row + 2, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col + 1), null));
                }
            }
            if(row + 2 <= 8 && col - 1 >= 1) {
                if(board.getPieceRC(row + 2, col - 1) == null || board.getPieceRC(row + 2, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 2, col - 1), null));
                }
            }
            //right
            if(row + 1 <= 8 && col + 2 <= 8) {
                if(board.getPieceRC(row + 1, col + 2) == null || board.getPieceRC(row + 1, col + 2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 2), null));
                }
            }
            if(row - 1 >= 1 && col + 2 <= 8) {
                if(board.getPieceRC(row - 1, col + 2) == null || board.getPieceRC(row - 1, col + 2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 2), null));
                }
            }
            //lower
            if(row - 2 >= 1 && col + 1 <= 8) {
                if(board.getPieceRC(row - 2, col + 1) == null || board.getPieceRC(row - 2, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 2, col + 1), null));
                }
            }
            if(row - 2 >= 1 && col - 1 >= 1) {
                if(board.getPieceRC(row - 2, col - 1) == null || board.getPieceRC(row - 2, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row -2 , col - 1), null));
                }
            }
            //left
            if(row + 1 <= 8 && col - 2 >= 1) {
                if(board.getPieceRC(row + 1, col - 2) == null || board.getPieceRC(row + 1, col - 2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 2), null));
                }
            }
            if(row - 1 >= 1 && col - 2 >= 1) {
                if(board.getPieceRC(row - 1, col - 2) == null || board.getPieceRC(row - 1, col - 2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 2), null));
                }
            }
        }

        return validMoves;
    }
}
