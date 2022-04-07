package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;

import java.util.Set;

public class QueenPiece extends Piece{
    @Override
    protected Set<BoardPosition> getPossibleMoves(final Board board) {
        //TODO to implement
        return null;
    }

    @Override
    protected Set<BoardPosition> getPossibleAttacks(final Board board) {
        //TODO to implement
        return null;
    }

    @Override
    protected Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        //TODO to implement
        return null;
    }
}
