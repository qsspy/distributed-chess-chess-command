package com.qsspy.chesscommand.service;

import com.qsspy.chesscommand.dto.request.BoardMoveRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Profile("!test")
public interface ChessBoardService {

    void calculateBoardStateAndSendMessage(final BoardMoveRequest request, final UUID gameTopicId);

    void initialiseGame(final UUID gameTopicId);

    void deleteGame(final UUID gameTopicId);
}
