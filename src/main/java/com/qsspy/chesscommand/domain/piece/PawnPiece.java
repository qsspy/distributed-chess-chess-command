package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PawnPiece extends Piece {

    public PawnPiece(PlayerColor color, BoardPosition position, String pieceCode) {
        super();
        this.color = color;
        this.position = position;
        this.pieceCode = pieceCode;
    }

    @Override
    public Set<BoardPosition> getPossibleMoves(final Board board) {
        int moveDirection = this.getColor() == PlayerColor.WHITE ? 1 : -1;
        List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());
        Set<BoardPosition> possibleMoves = new HashSet<>();

        AlphabeticPosition xPos = this.getPosition().getXPosition();
        int yPos = this.getPosition().getYPosition() + moveDirection;
        checkIfPossibleMove(allPieces, possibleMoves, xPos, yPos);

        return possibleMoves;
    }

    @Override
    public Set<BoardPosition> getPossibleAttacks(final Board board) {
        List<Piece> opponentPieces = this.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();
        List<Piece> ownPieces = this.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
        int moveDirection = this.getColor() == PlayerColor.WHITE ? 1 : -1;
        Set<BoardPosition> possibleAttacks = new HashSet<>();

        AlphabeticPosition leftAttackX = AlphabeticPosition.getValue(this.getPosition().getXPosition().getNumericPosition() - 1);
        int yPos = this.getPosition().getYPosition() + moveDirection;
        checkIfPossibleAttack(ownPieces, opponentPieces, possibleAttacks, leftAttackX, yPos);

        AlphabeticPosition rightAttackX = AlphabeticPosition.getValue(this.getPosition().getXPosition().getNumericPosition() + 1);
        checkIfPossibleAttack(ownPieces, opponentPieces, possibleAttacks, rightAttackX, yPos);

        return possibleAttacks;
    }

    @Override
    public Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        Set<BoardPosition> possibleSpecialMoves = new HashSet<>();

        // pawn must be on starting position
        if(
                (this.getPosition().getYPosition() == 2 && this.getColor() == PlayerColor.WHITE) ||
                (this.getPosition().getYPosition() == 7 && this.getColor() == PlayerColor.BLACK)
        ) {
            int moveDirection = this.getColor() == PlayerColor.WHITE ? 1 : -1;
            List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());

            for(int i = 0; i < 2; i++) {
                BoardPosition toPosition = new BoardPosition(this.getPosition().getXPosition(), this.getPosition().getYPosition() + moveDirection);
                boolean isTakenPosition = allPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));

                if(isTakenPosition) {
                    break;
                }
                if(i == 1) {
                   possibleSpecialMoves.add(toPosition);
                }

                if(this.getColor() == PlayerColor.WHITE) {
                    moveDirection++;
                } else {
                    moveDirection--;
                }
            }

        }

        return possibleSpecialMoves;
    }
}
