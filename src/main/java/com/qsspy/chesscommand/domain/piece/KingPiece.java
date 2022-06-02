package com.qsspy.chesscommand.domain.piece;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class KingPiece extends Piece {

    public KingPiece(PlayerColor color, BoardPosition position, String pieceCode) {
        super();
        this.color = color;
        this.position = position;
        this.pieceCode = pieceCode;
        this.hasMoved = false;
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
                checkIfPossibleMove(allPieces, possibleMoves, xPos, yPos);
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
                checkIfPossibleAttack(ownPieces, opponentPieces, possibleAttacks, xPos, yPos);
            }
        }

        return possibleAttacks;
    }


    // castle
    @Override
    public Set<BoardPosition> getPossibleSpecialMoves(final Board board) {
        Set<BoardPosition> possibleSpecialMoves = new HashSet<>();
        List<Piece> allPieces = Stream.concat(board.getBlack().stream(), board.getWhite().stream()).collect(Collectors.toList());
        List<Piece> ownPieces = this.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();

        Piece leftRook = ownPieces.stream().filter(piece -> piece.getPieceCode().equals("R1")).findFirst().orElse(null);
        Piece rightRook = ownPieces.stream().filter(piece -> piece.getPieceCode().equals("R2")).findFirst().orElse(null);
        if(!this.isHasMoved()) {
            if(leftRook != null && !leftRook.isHasMoved()) {
                for(int i = getPosition().getXPosition().getNumericPosition() - 1; i >= 2; i--) {
                    BoardPosition toPosition = new BoardPosition(AlphabeticPosition.getValue(i), getPosition().getYPosition());
                    boolean isTakenPosition = allPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));
                    if(isTakenPosition) break;
                    if(i == 2) possibleSpecialMoves.add(new BoardPosition(AlphabeticPosition.C, getPosition().getYPosition()));
                }
            }
            if(rightRook != null && !rightRook.isHasMoved()) {
                for(int i = getPosition().getXPosition().getNumericPosition() + 1; i <= 7; i++) {
                    BoardPosition toPosition = new BoardPosition(AlphabeticPosition.getValue(i), getPosition().getYPosition());
                    boolean isTakenPosition = allPieces.stream().anyMatch(piece -> piece.getPosition().equals(toPosition));
                    if(isTakenPosition) break;
                    if(i == 7) possibleSpecialMoves.add(new BoardPosition(AlphabeticPosition.G, getPosition().getYPosition()));
                }
            }
        }

        return possibleSpecialMoves;
    }

}
