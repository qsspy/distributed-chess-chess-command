package com.qsspy.chesscommand.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    public static <T> Response<T> success(final T body) {
        return new Response<T>(HttpStatus.OK.value(), body);
    }

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> of(final int status, final T body) {
        return new Response<T>(status, body);
    }

    private int status;
    @JsonUnwrapped
    private T body;
}
