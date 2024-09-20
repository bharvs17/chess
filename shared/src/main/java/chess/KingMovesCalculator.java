package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            //check lower 3
            if(row - 1 >= 1) {
                //L
                if(board.getPieceRC(row - 1, col) == null || board.getPieceRC(row - 1, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col), null));
                }
                //LL
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row - 1, col - 1) == null || board.getPieceRC(row - 1, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                    }
                }
                //LR
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row - 1, col + 1) == null || board.getPieceRC(row - 1, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                    }
                }
            }
            //check upper 3
            if(row + 1 <= 8) {
                //U
                if(board.getPieceRC(row + 1, col) == null || board.getPieceRC(row + 1, col).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
                //UL
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row + 1, col - 1) == null || board.getPieceRC(row + 1, col - 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                    }
                }
                //UR
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row + 1, col + 1) == null || board.getPieceRC(row + 1, col + 1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                    }
                }
            }
            //check L/R
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
            //check lower 3
            if(row - 1 >= 1) {
                //L
                if(board.getPieceRC(row - 1, col) == null || board.getPieceRC(row - 1, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col), null));
                }
                //LL
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row - 1, col - 1) == null || board.getPieceRC(row - 1, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                    }
                }
                //LR
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row - 1, col + 1) == null || board.getPieceRC(row - 1, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                    }
                }
            }
            //check upper 3
            if(row + 1 <= 8) {
                //U
                if(board.getPieceRC(row + 1, col) == null || board.getPieceRC(row + 1, col).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
                //UL
                if(col - 1 >= 1) {
                    if(board.getPieceRC(row + 1, col - 1) == null || board.getPieceRC(row + 1, col - 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                    }
                }
                //UR
                if(col + 1 <= 8) {
                    if(board.getPieceRC(row + 1, col + 1) == null || board.getPieceRC(row + 1, col + 1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                    }
                }
            }
            //check L/R
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
