package com.qsspy.chesscommand.controller;

import com.qsspy.chesscommand.dto.request.BoardMoveRequest;
import com.qsspy.chesscommand.dto.response.Response;
import com.qsspy.chesscommand.service.ChessBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/board")
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class ChessBoardController {

    public final ChessBoardService boardService;

    @PostMapping()
    public Response<Void> makeMove(
            @RequestHeader("Game-Topic-Id") final UUID gameTopicId,
            @RequestBody final BoardMoveRequest boardMoveRequest
            ) {
        log.info("Received move request : {}", boardMoveRequest);
        boardService.calculateBoardStateAndSendMessage(boardMoveRequest, gameTopicId);
        log.info("Moved has been performed and message has been sent of Kafka topic.");
        return Response.success();
    }

    @GetMapping()
    public Response<Void> initializeGame(
            @RequestHeader("Game-Topic-Id") final UUID gameTopicId
    ) {
        log.info("Received init game request for id: {}", gameTopicId);
        boardService.initializeGame(gameTopicId);
        log.info("Game with id {} has been initialised successfully.", gameTopicId);
        return Response.success();
    }

    @DeleteMapping()
    public Response<Void> deleteGame(
            @RequestHeader("Game-Topic-Id") final UUID gameTopicId
    ) {
        log.info("Received delete game request for id: {}", gameTopicId);
        boardService.deleteGame(gameTopicId);
        log.info("Game with id {} has been deleted successfully.", gameTopicId);
        return Response.success();
    }
}
