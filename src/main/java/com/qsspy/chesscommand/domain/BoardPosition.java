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

    public BoardPosition(String position) {
        this.xPosition = AlphabeticPosition.valueOf(String.valueOf(position.charAt(0)));
        this.yPosition = Character.getNumericValue(position.charAt(1));
    }

    public String getMergedPosition() {
        return xPosition.name() + yPosition;
    }
}
