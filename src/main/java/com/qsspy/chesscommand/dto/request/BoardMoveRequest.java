package com.qsspy.chesscommand.dto.request;

import com.qsspy.chesscommand.enums.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMoveRequest {
    private PlayerColor color;
    private String pieceCode;
    private String destination;
    private String switchPieceCode; //opcjonalne
}
