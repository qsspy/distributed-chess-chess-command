package com.qsspy.chesscommand.mapper;

import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.domain.piece.Piece;
import com.qsspy.chesscommand.dto.kafka.PieceStateDTO;

import java.util.HashSet;
import java.util.Set;

public class PieceStateMapper {

    public static PieceStateDTO entityToDTO(Piece piece, Board board) {
        PieceStateDTO pieceStateDTO = new PieceStateDTO();
        pieceStateDTO.setPieceCode(piece.getPieceCode());
        pieceStateDTO.setPosition(piece.getPosition().toString());

        Set<String> possibleMoves = new HashSet<>();
        for(BoardPosition boardPosition: piece.getPossibleMoves(board)) {
            possibleMoves.add(boardPosition.getXPosition().toString() + boardPosition.getYPosition());
        }
        pieceStateDTO.setPossibleMoves(possibleMoves);

        Set<String> possibleAttacks = new HashSet<>();
        for(BoardPosition boardPosition: piece.getPossibleAttacks(board)) {
            possibleAttacks.add(boardPosition.getXPosition().toString() + boardPosition.getYPosition());
        }
        pieceStateDTO.setPossibleAttacks(possibleAttacks);

        Set<String> possibleSpecialMoves = new HashSet<>();
        for(BoardPosition boardPosition: piece.getPossibleSpecialMoves(board)) {
            possibleSpecialMoves.add(boardPosition.getXPosition().toString() + boardPosition.getYPosition());
        }

        pieceStateDTO.setPossibleSpecialMoves(possibleSpecialMoves);

        return pieceStateDTO;
    }

}
