package chess.movecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RadialMoveCalculator {

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.BISHOP) {
            moves.addAll(rightUpDiagonalMoves(board,position));
            moves.addAll(rightDownDiagonalMoves(board,position));
            return moves;
        } else if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.ROOK) {
            moves.addAll(upDownMoves(board,position));
            moves.addAll(leftRightMoves(board,position));
            return moves;
        } else {
            moves.addAll(upDownMoves(board,position));
            moves.addAll(leftRightMoves(board,position));
            moves.addAll(rightUpDiagonalMoves(board,position));
            moves.addAll(rightDownDiagonalMoves(board,position));
            return moves;
        }
    }

    private Collection<ChessMove> upDownMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> upDownMoves = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        int i = 1;
        while(row + i <= 8 && board.getPieceRC(row + i,col) == null) {
            upDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col),null));
            i++;
        }
        if(row + i <= 8 && board.getPieceRC(row+i,col).getTeamColor() != pieceColor) {
            upDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col),null));
        }
        i = 1;
        while(row - i >= 1 && board.getPieceRC(row - i,col) == null) {
            upDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col),null));
            i++;
        }
        if(row - i >= 1 && board.getPieceRC(row-i,col).getTeamColor() != pieceColor) {
            upDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col),null));
        }
        return upDownMoves;
    }

    private Collection<ChessMove> leftRightMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> leftRightMoves = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        int i = 1;
        while(col + i <= 8 && board.getPieceRC(row,col + i) == null) {
            leftRightMoves.add(new ChessMove(position, new ChessPosition(row,col + i),null));
            i++;
        }
        if(col + i <= 8 && board.getPieceRC(row,col+i).getTeamColor() != pieceColor) {
            leftRightMoves.add(new ChessMove(position, new ChessPosition(row,col + i),null));
        }
        i = 1;
        while(col - i >= 1 && board.getPieceRC(row,col - i) == null) {
            leftRightMoves.add(new ChessMove(position, new ChessPosition(row,col - i),null));
            i++;
        }
        if(col - i >= 1 && board.getPieceRC(row,col - i).getTeamColor() != pieceColor) {
            leftRightMoves.add(new ChessMove(position, new ChessPosition(row,col - i),null));
        }
        return leftRightMoves;
    }

    private Collection<ChessMove> rightUpDiagonalMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> rightUpDownMoves = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        int col = position.getColumn();
        int row = position.getRow();
        int i = 1;
        while(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i,col + i) == null) {
            rightUpDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col + i),null));
            i++;
        }
        if(row + i <= 8 && col + i <= 8 && board.getPieceRC(row + i,col + i).getTeamColor() != pieceColor) {
            rightUpDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col + i),null));
        }
        i = 1;
        while(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i,col - i) == null) {
            rightUpDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col - i),null));
            i++;
        }
        if(row - i >= 1 && col - i >= 1 && board.getPieceRC(row - i,col - i).getTeamColor() != pieceColor) {
            rightUpDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col - i),null));
        }
        return rightUpDownMoves;
    }

    private Collection<ChessMove> rightDownDiagonalMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> rightDownMoves = new ArrayList<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        int col = position.getColumn();
        int row = position.getRow();
        int i = 1;
        while(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i,col + i) == null) {
            rightDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col + i),null));
            i++;
        }
        if(row - i >= 1 && col + i <= 8 && board.getPieceRC(row - i,col + i).getTeamColor() != pieceColor) {
            rightDownMoves.add(new ChessMove(position, new ChessPosition(row - i,col + i),null));
        }
        i = 1;
        while(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i,col - i) == null) {
            rightDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col - i),null));
            i++;
        }
        if(row + i <= 8 && col - i >= 1 && board.getPieceRC(row + i,col - i).getTeamColor() != pieceColor) {
            rightDownMoves.add(new ChessMove(position, new ChessPosition(row + i,col - i),null));
        }
        return rightDownMoves;
    }

}
