package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KingPiece extends Piece {

    public KingPiece(PlayerColor color, BoardPosition position, String pieceCode) {
        super();
        this.color = color;
        this.position = position;
        this.pieceCode = pieceCode;
    }

    @Override
    public Set<BoardPosition> getPossibleMoves(final Board board) {
        List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());
        Set<BoardPosition> possibleMoves = new HashSet<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int xPosition = this.getPosition().getXPosition().getNumericPosition() + i;
                AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                int yPos = this.getPosition().getYPosition() + j;
                checkIfTaken(allPieces, possibleMoves, xPos, yPos);
            }
        }

        return possibleMoves;
    }

    @Override
    public Set<BoardPosition> getPossibleAttacks(final Board board) {
        List<Piece> opponentPieces = this.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();
        List<Piece> ownPieces = this.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
        Set<BoardPosition> possibleAttacks = new HashSet<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int xPosition = this.getPosition().getXPosition().getNumericPosition() + i;
                AlphabeticPosition xPos = AlphabeticPosition.getValue(xPosition);
                int yPos = this.getPosition().getYPosition() + j;
                checkIfTakenByOpponent(ownPieces, opponentPieces, possibleAttacks, xPos, yPos);
            }
        }

        return possibleAttacks;
    }


    // roszada
    @Override
    public Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        //TODO to implement
        return new HashSet<>();
    }
}
