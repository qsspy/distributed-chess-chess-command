package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.PlayerColor;

import java.util.Set;

public abstract class Piece {
    protected PlayerColor color;
    protected BoardPosition position;
    protected String pieceCode;

    protected abstract Set<BoardPosition> getPossibleMoves(final Board board);

    protected abstract Set<BoardPosition> getPossibleAttacks(final Board board);

    protected abstract Set<BoardPosition> getPossibleSpecialMoves(final Board board);
}
