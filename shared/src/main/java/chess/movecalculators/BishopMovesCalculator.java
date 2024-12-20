package chess.movecalculators;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        RadialMoveCalculator bishopMoves = new RadialMoveCalculator();
        return bishopMoves.getMoves(board,position);

    }
}
