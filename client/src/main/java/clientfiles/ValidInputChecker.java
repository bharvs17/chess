package clientfiles;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.DataAccessException;

import java.util.Arrays;
import java.util.Collection;

public class ValidInputChecker {

    public static boolean checkRegister(String... params) {
        return params.length == 3;
    }

    public static boolean checkLogin(String... params) {
        return params.length == 2;
    }

    public static boolean checkLogout(String... params) {
        return params.length == 0;
    }

    public static boolean checkReset(String... params) {
        return (params.length == 1 && params[0].equals("admin"));
    }

    public static boolean checkListGames(String... params) {
        return (params.length == 1 && params[0].equals("games"));
    }

    public static boolean checkMakeGame(String... params) {
        return (params.length >= 2 && params[0].equals("game"));
    }

    public static boolean checkPlayGame(String... params) {
        return (params.length == 3 && params[0].equals("game"));
    }

    public static boolean checkObserveGame(String... params) {
        return (params.length == 2 && params[0].equals("game"));
    }

    public static boolean checkRedrawBoard(String... params) {
        return params[0].equals("board");
    }

    public static ChessMove checkMakeMove(ChessGame game, String... params) throws DataAccessException {
        if(params.length < 3 || params.length > 4) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# *(promotion piece) *if moving pawn to last row");
        }
        if(!params[0].equals("move")) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# *(promotion piece) *if moving pawn to last row");
        }
        if(params[1].length() != 3 || params[2].length() != 3) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# *(promotion piece) where # is a single digit number");
        }
        int startRow;
        int startCol;
        int endRow;
        int endCol;
        try {
            startRow = Integer.parseInt(String.valueOf(params[1].charAt(0)));
            startCol = Integer.parseInt(String.valueOf(params[1].charAt(2)));
            endRow = Integer.parseInt(String.valueOf(params[2].charAt(0)));
            endCol = Integer.parseInt(String.valueOf(params[2].charAt(2)));
        } catch(Exception ex) {
            throw new DataAccessException(400, "Error: expected make move #,# #,# where # is a number 1-8");
        }
        if(startRow < 1 || startRow > 8 || startCol < 1 || startCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- row and column number must be between 1-8");
        }
        if(endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- row and column number must be between 1-8");
        }
        ChessPosition start = new ChessPosition(startRow,startCol);
        ChessPosition end = new ChessPosition(endRow,endCol);
        if(game.getBoard().getPiece(start) != null) {
            if (game.getBoard().getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN) {
                if (movingToLastRow(game, start, end)) {
                    if (params.length < 4) {
                        throw new DataAccessException(400, "It seems like you're trying to move a pawn to the last row- you need to provide a promotion piece");
                    }
                    if (params[3].equals("rook")) {
                        return new ChessMove(start, end, ChessPiece.PieceType.ROOK);
                    } else if (params[3].equals("knight")) {
                        return new ChessMove(start, end, ChessPiece.PieceType.KNIGHT);
                    } else if (params[3].equals("bishop")) {
                        return new ChessMove(start, end, ChessPiece.PieceType.BISHOP);
                    } else if (params[3].equals("queen")) {
                        return new ChessMove(start, end, ChessPiece.PieceType.QUEEN);
                    } else {
                        throw new DataAccessException(400, "Please enter a valid promotion piece");
                    }
                }
            }
        }
        return new ChessMove(start,end,null);
    }

    private static boolean movingToLastRow(ChessGame game, ChessPosition start, ChessPosition end) {
        if(game.getBoard().getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) {
            return end.getRow() == 8;
        } else {
            return end.getRow() == 1;
        }
    }

    public static Collection<ChessMove> checkHighlightMoves(ChessGame game, String... params) throws DataAccessException {
        if(params.length != 3 || !params[0].equals("legal") || !params[1].equals("moves")) {
            throw new DataAccessException(400, "Error: expected highlight legal moves #,# where # is a number 1-8");
        }
        if(params[2].length() != 3) {
            throw new DataAccessException(400, "Error: expected highlight legal moves #,# where # is a number 1-8");
        }
        int row;
        int col;
        try {
            row = Integer.parseInt(String.valueOf(params[2].charAt(0)));
            col = Integer.parseInt(String.valueOf(params[2].charAt(2)));
        } catch(Exception ex) {
            throw new DataAccessException(400, "Error: expected highlight legal moves #,# where # is a number 1-8");
        }
        if(row < 1 || row > 8 || col < 1 || col > 8) {
            throw new DataAccessException(400, "Error: expected highlight legal moves #,# where # is a number 1-8");
        }
        ChessPosition pos = new ChessPosition(row,col);
        if(game.getBoard().getPiece(pos) == null) {
            throw new DataAccessException(400, "Error: pick a location with a piece");
        }
        return game.validMoves(pos);
    }
}
