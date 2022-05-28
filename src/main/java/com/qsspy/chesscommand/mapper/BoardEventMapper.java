package com.qsspy.chesscommand.mapper;

import com.qsspy.chesscommand.domain.BoardEvent;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.dto.kafka.BoardEventDTO;

public class BoardEventMapper {

    public static BoardEventDTO entityToDTO(BoardEvent boardEvent) {
        BoardEventDTO boardEventDTO = new BoardEventDTO();
        boardEventDTO.setFromPieceCode(boardEvent.getFromPieceCode());
        boardEventDTO.setToPieceCode(boardEvent.getToPieceCode());

        BoardPosition fromPosition = boardEvent.getFromPosition();
        if(fromPosition != null) {
            boardEventDTO.setFromPosition(fromPosition.getXPosition().toString() + fromPosition.getYPosition());
        }

        BoardPosition toPosition = boardEvent.getToPosition();
        if(toPosition != null) {
            boardEventDTO.setToPosition(toPosition.getXPosition().toString() + toPosition.getYPosition());
        }

        boardEventDTO.setEvent(boardEvent.getEvent());
        return boardEventDTO;
    }
}
