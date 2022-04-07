package com.qsspy.chesscommand.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PieceStateDTO {
    private String pieceCode;
    private String position;
    private Set<String> possibleMoves;
    private Set<String> possibleAttacks;
    private Set<String> possibleSpecialMoves;
}
