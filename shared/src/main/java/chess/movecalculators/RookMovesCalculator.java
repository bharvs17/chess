package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        RadialMoveCalculator rookMoves = new RadialMoveCalculator();
        return rookMoves.getMoves(board,position);

    }
}
