package com.qsspy.chesscommand.service;

import com.qsspy.chesscommand.dao.BoardDao;
import com.qsspy.chesscommand.dao.BoardEventDao;
import com.qsspy.chesscommand.dto.kafka.GameStateMessageDTO;
import com.qsspy.chesscommand.dto.request.BoardMoveRequest;
import com.qsspy.chesscommand.messagebroker.MessageBrokerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class ChessBoardServiceImpl implements ChessBoardService{

    private final MessageBrokerHandler<GameStateMessageDTO> brokerHandler;
    private final BoardDao boardDao;
    private final BoardEventDao boardEventDao;

    @Override
    public void calculateBoardStateAndSendMessage(final BoardMoveRequest request, final UUID gameTopicId) {
        //TODO to implement
    }

    @Override
    public void initialiseGame(final UUID gameTopicId) {
        //TODO to implement
    }

    @Override
    public void deleteGame(final UUID gameTopicId) {
        //TODO to implement
    }
}
