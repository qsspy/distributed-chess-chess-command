package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.BoardEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Profile("!test")
public class BoardEventDaoImpl implements BoardEventDao {

    private final Map<UUID, List<BoardEvent>> boardEventLists = new HashMap<>();

    @Override
    public List<BoardEvent> save(UUID gameTopicId, List<BoardEvent> board) {
        //TODO to implement
        boardEventLists.put(gameTopicId, board);
        return board;
    }

    @Override
    public List<BoardEvent> delete(UUID gameTopicId) {
        //TODO to implement
        List<BoardEvent> boardEvents = boardEventLists.get(gameTopicId);
        boardEventLists.remove(gameTopicId);
        return boardEvents;
    }
}
