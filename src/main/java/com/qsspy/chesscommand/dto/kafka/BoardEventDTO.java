package com.qsspy.chesscommand.dto.kafka;

import com.qsspy.chesscommand.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardEventDTO {
    private String fromPieceCode;
    private String toPieceCode;
    private String fromPosition;
    private String toPosition;
    private EventType event;
}