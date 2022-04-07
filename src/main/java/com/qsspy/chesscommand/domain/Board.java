package com.qsspy.chesscommand.domain;

import com.qsspy.chesscommand.domain.piece.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private List<Piece> black;
    private List<Piece> white;
}
