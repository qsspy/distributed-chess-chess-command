package com.qsspy.chesscommand.domain;

import com.qsspy.chesscommand.enums.AlphabeticPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPosition {
    private AlphabeticPosition xPosition;
    private int yPosition;
}
