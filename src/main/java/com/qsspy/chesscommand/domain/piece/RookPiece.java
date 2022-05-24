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

public class RookPiece extends Piece {

    public RookPiece(PlayerColor color, BoardPosition position, String pieceCode) {
        super();
        this.color = color;
        this.position = position;
        this.pieceCode = pieceCode;
    }

    @Override
    public Set<BoardPosition> getPossibleMoves(final Board board) {
        List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());
        Set<BoardPosition> possibleMoves = new HashSet<>();

        // go left
        int yPos = this.getPosition().getYPosition();
        int xPosition = this.getPosition().getXPosition().getNumericPosition();
        for(int i = xPosition - 1; i >= 1; i--) {
            if(checkIfTaken(allPieces, possibleMoves, AlphabeticPosition.getValue(i), yPos)) break;
        }

        // go right
        for(int i = xPosition + 1; i <= 8; i++) {
            if(checkIfTaken(allPieces, possibleMoves, AlphabeticPosition.getValue(i), yPos)) break;
        }

        // go up
        AlphabeticPosition xPos = this.getPosition().getXPosition();
        for(int i = yPos + 1; i <= 8; i++) {
            if(checkIfTaken(allPieces, possibleMoves, xPos, i)) break;
        }

        // go down
        xPos = this.getPosition().getXPosition();
        for(int i = yPos - 1; i >= 1; i--) {
            if(checkIfTaken(allPieces, possibleMoves, xPos, i)) break;
        }
        
        return possibleMoves;
    }


    @Override
    public Set<BoardPosition> getPossibleAttacks(final Board board) {
        List<Piece> opponentPieces = this.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();
        List<Piece> ownPieces = this.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
        Set<BoardPosition> possibleAttacks = new HashSet<>();

        // go left
        int yPos = this.getPosition().getYPosition();
        int xPosition = this.getPosition().getXPosition().getNumericPosition();
        for(int i = xPosition - 1; i >= 1; i--) {
            if(checkIfTakenByOpponent(ownPieces, opponentPieces, possibleAttacks, AlphabeticPosition.getValue(i), yPos)) break;
        }

        // go right
        for(int i = xPosition + 1; i <= 8; i++) {
            if(checkIfTakenByOpponent(ownPieces, opponentPieces, possibleAttacks, AlphabeticPosition.getValue(i), yPos)) break;
        }

        // go up
        AlphabeticPosition xPos = this.getPosition().getXPosition();
        for(int i = yPos + 1; i <= 8; i++) {
            if(checkIfTakenByOpponent(ownPieces, opponentPieces, possibleAttacks, xPos, i)) break;
        }

        // go down -- check if path to opponent piece isn't blocked by own piece
        for(int i = yPos - 1; i >= 1; i--) {
            if(checkIfTakenByOpponent(ownPieces, opponentPieces, possibleAttacks, xPos, i)) break;
        }

        return possibleAttacks;
    }

    @Override
    public Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        return new HashSet<>();
    }

}
