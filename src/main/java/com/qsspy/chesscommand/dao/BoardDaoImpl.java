package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.Board;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@Profile("!test")
public class BoardDaoImpl implements BoardDao {

    private final Map<UUID, Board> boards = new HashMap<>();

    @Override
    public Board save(final UUID gameTopicId, final Board board) {
        //TODO: to implement
        boards.put(gameTopicId, board);
        return board;
    }

    @Override
    public Board delete(final UUID gameTopicId) {
        //TODO: to implement
        Board deletedBoard = boards.get(gameTopicId);
        boards.remove(gameTopicId);
        return deletedBoard;
    }

    @Override
    public Board get(UUID gameTopicId) {
        return boards.get(gameTopicId);
    }
}
