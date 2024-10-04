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

    public ChessGame() {
        currentTeam = TeamColor.WHITE;
        board = new ChessBoard();
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
        ArrayList<ChessMove> maybeValidMoves = (ArrayList<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(currentTeam != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Invalid Move- wrong team color");
        }
        if() //check if given move is in list of validMoves for piece at that position, see above function, if not then throw exception for invalid move
            //for added detail check if in check and say that they must get out of check
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //check if king for given color is under attack by any pieces from the other color
        ChessPosition kingPos = getKingPos(board, teamColor);
        int row = kingPos.getRow();
        int col = kingPos.getColumn();
        //check knight spots
        //U LR
        if(row + 2 <= 8) {
            if(col + 1 <= 8) {
                if(board.getPieceRC(row+2,col+1) != null && board.getPieceRC(row+2,col+1).getTeamColor() != teamColor && board.getPieceRC(row+2,col+1).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if(col - 1 >= 1) {
                if(board.getPieceRC(row+2,col-1) != null && board.getPieceRC(row+2,col-1).getTeamColor() != teamColor && board.getPieceRC(row+2,col-1).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        //D LR
        if(row - 2 >= 1) {
            if(col + 1 <= 8) {
                if(board.getPieceRC(row-2,col+1) != null && board.getPieceRC(row-2,col+1).getTeamColor() != teamColor && board.getPieceRC(row-2,col+1).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if(col - 1 >= 1) {
                if(board.getPieceRC(row-2,col-1) != null && board.getPieceRC(row-2,col-1).getTeamColor() != teamColor && board.getPieceRC(row-2,col-1).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        //R UD
        if(col + 2 <= 8) {
            if(row + 1 <= 8) {
                if(board.getPieceRC(row+1,col+2) != null && board.getPieceRC(row+1,col+2).getTeamColor() != teamColor && board.getPieceRC(row+1,col+2).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if(row - 1 >= 1) {
                if(board.getPieceRC(row-1,col+2) != null && board.getPieceRC(row-1,col+2).getTeamColor() != teamColor && board.getPieceRC(row-1,col+2).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        //L UD
        if(col - 2 >= 1) {
            if(row + 1 <= 8) {
                if(board.getPieceRC(row+1,col-2) != null && board.getPieceRC(row+1,col-2).getTeamColor() != teamColor && board.getPieceRC(row+1,col-2).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if(row - 1 >= 1) {
                if(board.getPieceRC(row-1,col-2) != null && board.getPieceRC(row-1,col-2).getTeamColor() != teamColor && board.getPieceRC(row-1,col-2).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        //end knight checks
    }

    private ChessPosition getKingPos(ChessBoard board, TeamColor teamColor) {
        for(int r = 1; r <= 8; r++) {
            for(int c = 1; c <= 8; c++) {
                if(board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                    return new ChessPosition(r,c);
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
            //now check if no moves for all pieces
        }
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
}
