package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.BoardEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("!test")
public class BoardEventDaoImpl implements BoardEventDao {

    private final Map<UUID, List<BoardEvent>> boardEventLists = new HashMap<>();

    @Override
    public List<BoardEvent> save(UUID gameTopicId, List<BoardEvent> board) {
        boardEventLists.put(gameTopicId, board);
        return board;
    }

    @Override
    public List<BoardEvent> delete(UUID gameTopicId) {
        List<BoardEvent> boardEvents = boardEventLists.get(gameTopicId);
        boardEventLists.remove(gameTopicId);
        return boardEvents;
    }

    @Override
    public List<BoardEvent> get(UUID gameTopicId) {
        return boardEventLists.getOrDefault(gameTopicId, new ArrayList<>());
    }
}
