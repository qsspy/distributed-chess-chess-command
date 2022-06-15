package com.qsspy.chesscommand.domain;

import com.qsspy.chesscommand.domain.piece.Piece;
import com.qsspy.chesscommand.enums.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private PlayerColor currentAllowedColorToMove;
    private List<Piece> black;
    private List<Piece> white;
}
