package com.qsspy.chesscommand.domain;

import com.qsspy.chesscommand.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardEvent {
    private String fromPieceCode;
    private String toPieceCode;
    private BoardPosition fromPosition;
    private BoardPosition toPosition;
    private EventType event;
}
