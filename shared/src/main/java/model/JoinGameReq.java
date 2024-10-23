package model;

import chess.ChessGame;

public record JoinGameReq(ChessGame.TeamColor playerColor, int gameID) {
}
