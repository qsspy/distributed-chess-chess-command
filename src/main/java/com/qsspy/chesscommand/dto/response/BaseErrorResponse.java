package com.qsspy.chesscommand.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseErrorResponse {
    private String message;
    private String description;
}
