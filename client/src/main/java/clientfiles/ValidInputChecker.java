package clientfiles;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.DataAccessException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

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

    public static int colConverter(String col) throws DataAccessException {
        HashMap<String, Integer> converter = new HashMap<>();
        converter.put("a",1);
        converter.put("b",2);
        converter.put("c",3);
        converter.put("d",4);
        converter.put("e",5);
        converter.put("f",6);
        converter.put("g",7);
        converter.put("h",8);
        if(converter.get(col) == null) {
            throw new DataAccessException(400, "Error: expected position to be @# where @ is a letter a-h and # is a number 1-8\n");
        }
        return converter.get(col);
    }

    public static ChessMove checkMakeMove(ChessGame game, String... params) throws DataAccessException {
        if(params.length != 3) {
            throw new DataAccessException(400, "Error: expected make move @# @# where @ is a letter a-h and # is a number 1-8\n");
        }
        if(!params[0].equals("move")) {
            throw new DataAccessException(400, "Error: expected make move @# @# where @ is a letter a-h and # is a number 1-8\n");
        }
        if(params[1].length() != 2 || params[2].length() != 2) {
            throw new DataAccessException(400, "Error: expected make move @# @# where @ is a letter a-h and # is a number 1-8\n");
        }
        String startColLet;
        int startCol;
        int startRow;
        String endColLet;
        int endCol;
        int endRow;
        try {
            startRow = Integer.parseInt(String.valueOf(params[1].charAt(1)));
            startColLet = String.valueOf(params[1].charAt(0));
            endRow = Integer.parseInt(String.valueOf(params[2].charAt(1)));
            endColLet = String.valueOf(params[2].charAt(0));
            startCol = colConverter(startColLet);
            endCol = colConverter(endColLet);
        } catch(Exception ex) {
            throw new DataAccessException(400, "Error: expected make move @# @# where @ is a letter a-h and # is a number 1-8\n");
        }
        if(startRow < 1 || startRow > 8 || startCol < 1 || startCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- column must be a-h, row 1-8\n");
        }
        if(endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
            throw new DataAccessException(400, "Error: enter a valid move- column must be a-h, row 1-8\n");
        }
        ChessPosition start = new ChessPosition(startRow,startCol);
        ChessPosition end = new ChessPosition(endRow,endCol);
        if(game.getBoard().getPiece(start) != null) {
            if(pawnMovingToLastRow(game,start,end)) {
                Scanner scanner = new Scanner(System.in);
                ChessPiece.PieceType promoPiece = null;
                System.out.println("You're trying to move a pawn to the final row. Please enter a valid promotion piece");
                while(promoPiece == null) {
                    System.out.print(">>> ");
                    promoPiece = evalInput(scanner.nextLine().toLowerCase());
                }
                return new ChessMove(start,end,promoPiece);
            }
        }
        return new ChessMove(start,end,null);
    }

    private static ChessPiece.PieceType evalInput(String input) {
        if(input.equals("queen")) {
            return ChessPiece.PieceType.QUEEN;
        } else if(input.equals("rook")) {
            return ChessPiece.PieceType.ROOK;
        } else if(input.equals("knight")) {
            return ChessPiece.PieceType.KNIGHT;
        } else if(input.equals("bishop")) {
            return ChessPiece.PieceType.BISHOP;
        } else {
            System.out.println("Please enter a valid promotion piece");
            return null;
        }
    }

    private static boolean pawnMovingToLastRow(ChessGame game, ChessPosition start, ChessPosition end) {
        if(game.getBoard().getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (game.getBoard().getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) {
                return end.getRow() == 8;
            } else {
                return end.getRow() == 1;
            }
        }
        return false;
    }

    public static Collection<ChessMove> checkHighlightMoves(ChessGame game, String... params) throws DataAccessException {
        if(params.length != 3 || !params[0].equals("legal") || !params[1].equals("moves")) {
            throw new DataAccessException(400, "Error: expected highlight legal moves @# where @ is a letter a-h and # is a number 1-8\n");
        }
        if(params[2].length() != 2) {
            throw new DataAccessException(400, "Error: expected highlight legal moves @# where @ is a letter a-h and # is a number 1-8\n");
        }
        int row;
        int col;
        try {
            row = Integer.parseInt(String.valueOf(params[2].charAt(1)));
            col = colConverter(String.valueOf(params[2].charAt(0)));
        } catch(Exception ex) {
            throw new DataAccessException(400, "Error: expected highlight legal moves @# where @ is a letter a-h and # is a number 1-8\n");
        }
        if(row < 1 || row > 8 || col < 1 || col > 8) {
            throw new DataAccessException(400, "Error: expected highlight legal moves @# where @ is a letter a-h and # is a number 1-8\n");
        }
        ChessPosition pos = new ChessPosition(row,col);
        if(game.getBoard().getPiece(pos) == null) {
            throw new DataAccessException(400, "Error: pick a location with a piece\n");
        }
        return game.validMoves(pos);
    }
}
