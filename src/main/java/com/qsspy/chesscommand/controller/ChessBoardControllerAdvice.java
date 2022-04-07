package com.qsspy.chesscommand.controller;

import com.qsspy.chesscommand.dto.response.BaseErrorResponse;
import com.qsspy.chesscommand.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ChessBoardControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_ERROR_MESSAGE = "Internal Error occured.";

    //TODO obsługa błędu :
//    ERROR (ruch jest niemożliwy)
//    {
//        "status": 401, //integer
//            "message": "Forbidden move" //string
//    }

//    ERROR (gra dla tego id już istnieje)
//    {
//        "status": 401, //integer
//            "message": "This game already exists" //string
//    }

    @ExceptionHandler(Exception.class)
    public Response<BaseErrorResponse> baseErrorResponse(final Exception exception) {
        final BaseErrorResponse body = new BaseErrorResponse(INTERNAL_ERROR_MESSAGE, exception.getMessage());
        log.error(INTERNAL_ERROR_MESSAGE, exception);
        return Response.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), body);
    }
}
