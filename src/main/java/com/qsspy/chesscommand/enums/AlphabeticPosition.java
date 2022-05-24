package com.qsspy.chesscommand.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlphabeticPosition {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8);

    private final int numericPosition;

    public static AlphabeticPosition getValue(int numericPosition) {
        for(AlphabeticPosition value: values()) {
            if(value.getNumericPosition() == numericPosition) {
                return value;
            }
        }
        return null;
    }
}
