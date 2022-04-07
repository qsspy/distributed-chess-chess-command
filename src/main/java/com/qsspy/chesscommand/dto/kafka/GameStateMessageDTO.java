package com.qsspy.chesscommand.dto.kafka;

import com.qsspy.chesscommand.enums.PlayerTurn;
import com.qsspy.chesscommand.enums.WinnerCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateMessageDTO {
    private PlayerTurn playerTurn;
    private WinnerCode winner; //opcjonalne
    private List<PieceStateDTO> black;
    private List<PieceStateDTO> white;
    private List<BoardEventDTO> lastBoardEvents;
}