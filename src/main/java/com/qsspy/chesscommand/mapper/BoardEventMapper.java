package com.qsspy.chesscommand.mapper;

import com.qsspy.chesscommand.domain.BoardEvent;
import com.qsspy.chesscommand.dto.kafka.BoardEventDTO;

public class BoardEventMapper {

    public static BoardEventDTO entityToDTO(BoardEvent boardEvent) {
        BoardEventDTO boardEventDTO = new BoardEventDTO();
        boardEventDTO.setFromPieceCode(boardEvent.getFromPieceCode());
        boardEventDTO.setToPieceCode(boardEvent.getToPieceCode());
        boardEventDTO.setFromPosition(boardEvent.getFromPosition().getXPosition().toString() + boardEvent.getFromPosition().getYPosition());
        boardEventDTO.setToPosition(boardEvent.getToPosition().getXPosition().toString() + boardEvent.getToPosition().getYPosition());
        boardEventDTO.setEvent(boardEvent.getEvent());

        return boardEventDTO;
    }
}
