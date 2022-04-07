package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.BoardEvent;

import java.util.List;
import java.util.UUID;

public interface BoardEventDao {

    /** Creates board events list or replaces them if already exist for given topic (game) id
     *
     * @param gameTopicId id of the game topic
     * @param board current state of the board
     * @return saved board
     */
    List<BoardEvent> save(final UUID gameTopicId, final List<BoardEvent> board);

    /** Deletes the board events list from data storage
     *
     * @param gameTopicId id of the game topic
     * @return returns deleted board
     */
    List<BoardEvent> delete(final UUID gameTopicId);
}
