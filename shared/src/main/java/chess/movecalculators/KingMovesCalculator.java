package chess.movecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(row - 1 >= 1) {
                if(board.getPieceRC(row - 1, col) == null || board.getPieceRC(row - 1, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col), null));
                }
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row - 1, col - 1) == null || board.getPieceRC(row - 1, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                    }
                }
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row - 1, col + 1) == null || board.getPieceRC(row - 1, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                    }
                }
            }
            if(row + 1 <= 8) {
                if(board.getPieceRC(row + 1, col) == null || board.getPieceRC(row + 1, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row + 1, col - 1) == null || board.getPieceRC(row + 1, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                    }
                }
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row + 1, col + 1) == null || board.getPieceRC(row + 1, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                    }
                }
            }
            if(col - 1 >= 1) {
                if(board.getPieceRC(row, col - 1) == null || board.getPieceRC(row, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row, col - 1), null));
                }
            }
            if(col + 1 <= 8) {
                if(board.getPieceRC(row, col+ 1) == null || board.getPieceRC(row, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row, col + 1), null));
                }
            }
        } else {
            if(row - 1 >= 1) {
                if(board.getPieceRC(row - 1, col) == null || board.getPieceRC(row - 1, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col), null));
                }
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row - 1, col - 1) == null || board.getPieceRC(row - 1, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                    }
                }
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row - 1, col + 1) == null || board.getPieceRC(row - 1, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                    }
                }
            }
            if(row + 1 <= 8) {
                if(board.getPieceRC(row + 1, col) == null || board.getPieceRC(row + 1, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row + 1, col - 1) == null || board.getPieceRC(row + 1, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                    }
                }
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row + 1, col + 1) == null || board.getPieceRC(row + 1, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                    }
                }
            }
            if(col - 1 >= 1) {
                if(board.getPieceRC(row, col - 1) == null || board.getPieceRC(row, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row, col - 1), null));
                }
            }
            if(col + 1 <= 8) {
                if(board.getPieceRC(row, col+ 1) == null || board.getPieceRC(row, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row, col + 1), null));
                }
            }
        }

        return validMoves;
    }

}
