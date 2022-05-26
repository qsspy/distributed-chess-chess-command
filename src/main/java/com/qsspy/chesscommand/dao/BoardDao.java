package com.qsspy.chesscommand.dao;

import com.qsspy.chesscommand.domain.Board;

import java.util.UUID;

public interface BoardDao {

    /** Creates board or modifies it if it already exists for given topic (game) id
     *
     * @param gameTopicId id of the game topic
     * @param board current state of the board
     * @return saved board
     */
    Board save(final UUID gameTopicId, final Board board);

    /** Deletes the board from data storage
     *
     * @param gameTopicId id of the game topic
     * @return returns deleted board
     */
    Board delete(final UUID gameTopicId);

    /** Gets board for given topic
     *
     * @param gameTopicId id of the game topic
     * @return returns board at given topic
     */
    Board get(final UUID gameTopicId);
}
