package com.qsspy.chesscommand.enums;

public enum PlayerColor {
    BLACK, WHITE;

    public PlayerColor inverse() {
        if(PlayerColor.BLACK == this) {
            return PlayerColor.WHITE;
        }
        return PlayerColor.BLACK;
    }
}
