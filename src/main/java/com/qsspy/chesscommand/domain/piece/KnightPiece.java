package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KnightPiece extends Piece {

    public KnightPiece(PlayerColor color, BoardPosition position, String pieceCode) {
        super();
        this.color = color;
        this.position = position;
        this.pieceCode = pieceCode;
    }

    @Override
    public Set<BoardPosition> getPossibleMoves(final Board board) {
        List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());
        Set<BoardPosition> possibleMoves = new HashSet<>();
        for(int i = -2; i <= 2; i++) {
            if(i == 0) {
                continue;
            }

            int xPosition = this.getPosition().getXPosition().getNumericPosition() + i;
            if(i == -1 || i == 1) {
                int moveY = -2;
                for(int j = 0; j < 2; j++) {
                    AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                    int yPos = this.getPosition().getYPosition() + moveY;
                    checkIfPossibleMove(allPieces, possibleMoves, xPos, yPos);
                    moveY = 2;
                }
            }

            if(i == -2 || i == 2) {
                int moveY = -1;
                for(int j = 0; j < 2; j++) {
                    AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                    int yPos = this.getPosition().getYPosition() + moveY;
                    checkIfPossibleMove(allPieces, possibleMoves, xPos, yPos);
                    moveY = 1;
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public Set<BoardPosition> getPossibleAttacks(final Board board) {
        List<Piece> opponentPieces = this.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();
        List<Piece> ownPieces = this.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
        Set<BoardPosition> possibleAttacks = new HashSet<>();
        for(int i = -2; i <= 2; i++) {
            if(i == 0) {
                continue;
            }

            int xPosition = this.getPosition().getXPosition().getNumericPosition() + i;
            if(i == -1 || i == 1) {
                int moveY = -2;
                for(int j = 0; j < 2; j++) {
                    AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                    int yPos = this.getPosition().getYPosition() + moveY;
                    checkIfPossibleAttack(ownPieces, opponentPieces, possibleAttacks, xPos, yPos);
                    moveY = 2;
                }
            }

            if(i == -2 || i == 2) {
                int moveY = -1;
                for(int j = 0; j < 2; j++) {
                    AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                    int yPos = this.getPosition().getYPosition() + moveY;
                    checkIfPossibleAttack(ownPieces, opponentPieces, possibleAttacks, xPos, yPos);
                    moveY = 1;
                }
            }
        }

        return possibleAttacks;
    }

    @Override
    public Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        return new HashSet<>();
    }


}
