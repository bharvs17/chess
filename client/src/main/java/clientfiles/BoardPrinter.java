package clientfiles;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    public static String boardString(ChessGame game, ChessGame.TeamColor color) {
        ChessBoard board = game.getBoard();
        StringBuilder boardStr = new StringBuilder();
        boardStr.append(SET_TEXT_BOLD);
        if(color == ChessGame.TeamColor.WHITE) {
            boardStr.append(topBottomLine(0));
            for(int i = 8; i > 0; i--) {
                boardStr.append(rowString(board,i,0));
            }
            boardStr.append(topBottomLine(0));
        } else {
            boardStr.append(topBottomLine(1));
            for(int i = 1; i < 9; i++) {
                boardStr.append(rowString(board,i,1));
            }
            boardStr.append(topBottomLine(1));
        }
        boardStr.append(RESET_BG_COLOR);
        boardStr.append(RESET_TEXT_COLOR);
        return boardStr.toString();
    }

    public static String boardDefault() {
        ChessBoard startingBoard = new ChessBoard();
        startingBoard.resetBoard();
        StringBuilder board = new StringBuilder();
        board.append(SET_TEXT_BOLD);
        //white's perspective
        board.append(topBottomLine(0));
        for(int i = 8; i > 0; i--) {
            board.append(rowString(startingBoard,i,0));
        }
        board.append(topBottomLine(0));
        //black's perspective
        board.append(topBottomLine(1));
        for(int i = 1; i < 9; i++) {
            board.append(rowString(startingBoard,i,1));
        }
        board.append(topBottomLine(1));
        board.append(RESET_BG_COLOR);
        board.append(RESET_TEXT_COLOR);
        return board.toString();
    }

    private static String topBottomLine(int color) {
        String[] cols;
        if (color == 0) {
            cols = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        } else {
            cols = new String[]{"H", "G", "F", "E", "D", "C", "B", "A"};
        }
        StringBuilder topLine = new StringBuilder();
        topLine.append(SET_BG_COLOR_DARK_GREY);
        topLine.append(SET_TEXT_COLOR_MAGENTA);
        topLine.append("  ");
        for (int i = 0; i < 8; i++) {
            topLine.append(" ".repeat(3));
            topLine.append(cols[i]);
            topLine.append(" ".repeat(3));
        }
        topLine.append("  ");
        topLine.append("\n");
        return topLine.toString();
    }

    private static String rowString(ChessBoard board, int row, int co) {
        StringBuilder rowString = new StringBuilder();
        String initialColor;
        if((co == 0 && (row % 2 == 0)) || (co == 1 && (row % 2 == 1))) {
            initialColor = SET_BG_COLOR_LIGHT_GREY;
        } else {
            initialColor = SET_BG_COLOR_BLACK;
        }
        String currColor;
        for(int i = 0; i < 3; i++) {
            currColor = initialColor;
            if(i == 1) {
                rowString.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_COLOR_MAGENTA).append(row).append(" ");
                for(int h = 1; h < 9; h++) {
                    rowString.append(currColor).append(" ".repeat(3));
                    rowString.append(pieceString(board,row,h,co));
                    rowString.append(currColor).append(" ".repeat(3));
                    if(currColor.equals(SET_BG_COLOR_LIGHT_GREY)) {
                        currColor = SET_BG_COLOR_BLACK;
                    } else {
                        currColor = SET_BG_COLOR_LIGHT_GREY;
                    }
                }
                rowString.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_COLOR_MAGENTA).append(" ").append(row);
                rowString.append("\n");
            } else {
                rowString.append(SET_BG_COLOR_DARK_GREY).append("  ");
                for(int j = 0; j < 8; j++) {
                    rowString.append(currColor).append(" ".repeat(7));
                    if(currColor.equals(SET_BG_COLOR_LIGHT_GREY)) {
                        currColor = SET_BG_COLOR_BLACK;
                    } else {
                        currColor = SET_BG_COLOR_LIGHT_GREY;
                    }
                }
                rowString.append(SET_BG_COLOR_DARK_GREY).append("  ");
                rowString.append("\n");
            }
        }
        return rowString.toString();
    }

    private static String pieceString(ChessBoard board, int r, int c, int co) {
        StringBuilder pieceBuilder = new StringBuilder();
        ChessPiece piece;
        if(co == 0) {
            piece = board.getPieceRC(r,c);
        } else {
            piece = board.getPieceRC(r,9-c);
        }
        if(piece == null) {
            return " ";
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceBuilder.append(SET_TEXT_COLOR_RED);
        } else {
            pieceBuilder.append(SET_TEXT_COLOR_BLUE);
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING) {
            pieceBuilder.append("K");
        } else if(piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            pieceBuilder.append("Q");
        } else if(piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            pieceBuilder.append("B");
        } else if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            pieceBuilder.append("N");
        } else if(piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            pieceBuilder.append("R");
        } else {
            pieceBuilder.append("P");
        }
        return pieceBuilder.toString();
    }

}