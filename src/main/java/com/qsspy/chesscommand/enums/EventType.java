package com.qsspy.chesscommand.enums;

public enum EventType {
    MOVED, //Piece has moved from one position to another
    KILLED, //Piece has been killed
    CHECKED, //Piece is checked (king only)
    SWITCHED, //Pawn is switched for another piece
}