package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class Piece {
    protected PlayerColor color;
    protected BoardPosition position;
    protected String pieceCode;
    protected boolean hasMoved;

    public abstract Set<BoardPosition> getPossibleMoves(final Board board);

    public abstract Set<BoardPosition> getPossibleAttacks(final Board board);

    public abstract Set<BoardPosition> getPossibleSpecialMoves(final Board board);

    protected static boolean checkIfPossibleMove(List<Piece> allPieces, Set<BoardPosition> possibleMoves, AlphabeticPosition xPos, int yPos) {
        if(xPos != null && yPos >= 1 && yPos <= 8) {
            BoardPosition toPosition = new BoardPosition(xPos, yPos);
            boolean isTakenPosition = allPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));
            if(isTakenPosition) {
                return true;
            }
            possibleMoves.add(toPosition);
            return false;
        }
        return true;
    }

    protected static boolean checkIfPossibleAttack(List<Piece> ownPieces, List<Piece> opponentPieces, Set<BoardPosition> possibleAttacks, AlphabeticPosition xPos, int yPos) {
        if(xPos != null && yPos >= 1 && yPos <= 8) {
            BoardPosition toPosition = new BoardPosition(xPos, yPos);
            boolean isOwnPosition = ownPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));
            if(isOwnPosition) {
                return true;
            }
            boolean isOpponentPosition = opponentPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));
            if(isOpponentPosition) {
                possibleAttacks.add(toPosition);
                return true;
            }
            return false;
        }
        return true;
    }
}
