package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.Board;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@Profile("!test")
public class BoardDaoImpl implements BoardDao{

    private final Map<UUID, Board> boards = new HashMap<>();

    @Override
    public Board save(final UUID gameTopicId, final Board board) {
        //TODO to implement
        return null;
    }

    @Override
    public Board delete(final UUID gameTopicId) {
        //TODO to implement
        return null;
    }
}
