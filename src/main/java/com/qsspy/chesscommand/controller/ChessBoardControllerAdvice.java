package com.qsspy.chesscommand.controller;

import com.qsspy.chesscommand.dto.response.BaseErrorResponse;
import com.qsspy.chesscommand.dto.response.Response;
import com.qsspy.chesscommand.exception.ForbiddenMoveException;
import com.qsspy.chesscommand.exception.GameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ChessBoardControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_ERROR_MESSAGE = "Internal Error occurred.";
    private static final String FORBIDDEN_MOVE_MESSAGE = "Forbidden move";
    private static final String GAME_ALREADY_EXISTS_MESSAGE = "Game for this room already exists";

    @ExceptionHandler(ForbiddenMoveException.class)
    public Response<BaseErrorResponse> forbiddenMoveResponse(final ForbiddenMoveException exception) {
        final BaseErrorResponse body = new BaseErrorResponse(FORBIDDEN_MOVE_MESSAGE, "Cannot move piece to this position");
        log.error(FORBIDDEN_MOVE_MESSAGE, exception);
        return Response.of(HttpStatus.UNAUTHORIZED.value(), body);
    }

    @ExceptionHandler(GameAlreadyExistsException.class)
    public Response<BaseErrorResponse> gameAlreadyExistsResponse(final GameAlreadyExistsException exception) {
        final BaseErrorResponse body = new BaseErrorResponse(GAME_ALREADY_EXISTS_MESSAGE, "Cannot create another game for this room");
        log.error(GAME_ALREADY_EXISTS_MESSAGE, exception);
        return Response.of(HttpStatus.UNAUTHORIZED.value(), body);
    }

    @ExceptionHandler(Exception.class)
    public Response<BaseErrorResponse> baseErrorResponse(final Exception exception) {
        final BaseErrorResponse body = new BaseErrorResponse(INTERNAL_ERROR_MESSAGE, exception.getMessage());
        log.error(INTERNAL_ERROR_MESSAGE, exception);
        return Response.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), body);
    }
}
