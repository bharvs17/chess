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
        if(currentTeam != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Invalid Move- wrong team color");
        }
        if() //check if given move is in list of validMoves for piece at that position, see above function, if not then throw exception for invalid move
            //for added detail check if in check and say that they must get out of check
            //if good move then update board and currentTeam accordingly
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
        //now check lines UDLR, and diagonal
        //U
        int r = row+1;
        while(r <= 8) {
            if(board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getTeamColor() == teamColor) {
                break; //piece from same team would block anything else above it, don't need to check up anymore
            }
            if(board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getTeamColor() != teamColor && (board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.ROOK)) {
                return true;
            }
            if(r == row+1 && board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.KING) {
                //should only be 2 kings on board, so if one encountered then it's the enemy king
                return true;
            }
            r++;
        }
        //D
        r = row-1;
        while(r >= 1) {
            if(board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getTeamColor() != teamColor && (board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.ROOK)) {
                return true;
            }
            if(r == row-1 && board.getPieceRC(r,col) != null && board.getPieceRC(r,col).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
            r--;
        }
        //R
        int c = col + 1;
        while(c <= 8) {
            if(board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getTeamColor() != teamColor && (board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.ROOK)) {
                return true;
            }
            if(c == col+1 && board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
            c++;
        }
        //L
        c = col - 1;
        while(c >= 8) {
            if(board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getTeamColor() != teamColor && (board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.ROOK)) {
                return true;
            }
            if(c == col-1 && board.getPieceRC(row,c) != null && board.getPieceRC(row,c).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
            c--;
        }
        //UR
        r = row+1;
        c = col+1;
        while(r <= 8 && c <= 8) {
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() != teamColor && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.BISHOP)) {
                return true;
            }
            if(r == row+1 && c == col+1 && board.getPieceRC(r,c) != null && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.PAWN)) {
                //if directly UR and is king then check, or if pawn then check (if it was a friendly pawn then would break in first if statement)
                return true;
            }
            r++;
            c++;
        }
        //UL
        r = row+1;
        c = col-1;
        while(r <= 8 && c >= 1) {
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() != teamColor && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.BISHOP)) {
                return true;
            }
            if(r == row+1 && c == col-1 && board.getPieceRC(r,c) != null && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.PAWN)) {
                return true;
            }
            r++;
            c--;
        }
        //DR
        r = row-1;
        c = col+1;
        while(r >= 1 && c <= 8) {
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() != teamColor && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.BISHOP)) {
                return true;
            }
            if(r == row-1 && c == col+1 && board.getPieceRC(r,c) != null && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.PAWN)) {
                return true;
            }
            r--;
            c++;
        }
        //DL
        r = row-1;
        c = col-1;
        while(r >= 1 && c >= 1) {
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() == teamColor) {
                break;
            }
            if(board.getPieceRC(r,c) != null && board.getPieceRC(r,c).getTeamColor() != teamColor && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.BISHOP)) {
                return true;
            }
            if(r == row-1 && c == col-1 && board.getPieceRC(r,c) != null && (board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.KING || board.getPieceRC(r,c).getPieceType() == ChessPiece.PieceType.PAWN)) {
                return true;
            }
            r--;
            c--;
        }
        //if nothing returned true then not in check; return false
        return false;
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
}
