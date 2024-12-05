package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor currentTeam;
    ChessBoard board;
    boolean isOver;

    public ChessGame() {
        currentTeam = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        isOver = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //get valid moves at startPosition from moves calculator then go through and find which moves are bad (would be check/checkmate) and remove
        if(board.getPiece(startPosition) == null) {
            return null;
        }
        //remove moves if they don't get you out of check
        /*algorithm: try making move, then starting from king check possible attack sites- directly around, diagonals, lines, knight
        if there's a piece from the other team in one of those spots, see if it's a piece that could move that way to attack king (ex diagonal would
        be pawns if 1 space, bishop or queen from any amount of spaces) and if so then remove that move
        */
        return removeBadMoves(board.getPiece(startPosition).pieceMoves(board,startPosition));
    }

    private Collection<ChessMove> removeBadMoves(Collection<ChessMove> moves) {
        ArrayList<ChessMove> goodMoves = (ArrayList<ChessMove>) moves;
        ArrayList<ChessMove> badMoves = new ArrayList<>();
        if(moves.isEmpty()) {
            return goodMoves;
        }
        ChessPosition startPos = goodMoves.getFirst().getStartPosition();
        ChessPiece testPiece = board.getPiece(startPos);
        ChessGame.TeamColor teamColor = testPiece.getTeamColor();
        board.addPiece(startPos,null);
        ChessPosition endPos;
        for(int i = 0; i < moves.size(); i++) {
            //make move as a test
            endPos = goodMoves.get(i).getEndPosition();
            ChessPiece temp = board.getPiece(endPos);
            board.addPiece(endPos, testPiece);
            if(isInCheck(teamColor)) {
                badMoves.add(goodMoves.get(i));
            }
            //reset piece that might've been taken before next test
            board.addPiece(endPos,temp);
        }
        //reset test piece
        board.addPiece(startPos, testPiece);
        //remove all bad moves
        for(int i = 0; i < badMoves.size(); i++) {
            goodMoves.remove(badMoves.get(i));
        }
        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid move error- no piece at given start position");
        }
        if(currentTeam != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Invalid Move error- not this team's turn");
        }
        if(!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move error- Either move not possible for selected piece or the move results in check.");
        }
        if(move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(),new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece()));
            board.addPiece(move.getStartPosition(),null);
        } else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.addPiece(move.getStartPosition(), null);
        }
        if(currentTeam == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKingPos(board,teamColor);
        if(kingPos == null) {
            return false;
        }
        Collection<ChessMove> otherTeamAllMoves = new ArrayList<>();
        for(int r = 1; r <= 8; r++) {
            for(int c = 1; c <= 8; c++) {
                if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() != teamColor) {
                    otherTeamAllMoves.addAll(board.getPieceRC(r,c).pieceMoves(board,new ChessPosition(r,c)));
                }
            }
        }
        for(ChessMove move : otherTeamAllMoves) {
            if (kingPos.equals(move.getEndPosition())) {
                return true;
            }
        }
        return false;
    }

    private ChessPosition getKingPos(ChessBoard board, TeamColor teamColor) {
        for(int r = 1; r <= 8; r++) {
            for(int c = 1; c <= 8; c++) {
                if(board.getPieceRC(r,c) != null) {
                    if(board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                            return new ChessPosition(r, c);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //same as isInCheck, but if teamColor has no moves that get out of check then return true, use validMoves function
        if(isInCheck(teamColor)) {
            return noMovesPossible(teamColor);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //check all pieces for teamColor and see if they have 0 validMoves and not in check or checkmate
        if(!isInCheck(teamColor)) {
            return noMovesPossible(teamColor);
        }
        return false;
    }

    private boolean noMovesPossible(TeamColor teamColor) {
        for(int r = 1; r <= 8; r++) {
            for(int c = 1; c <= 8; c++) {
                if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                    if(!validMoves(new ChessPosition(r,c)).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        isOver = true;
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public void resign() {
        isOver = true;
    }
}
