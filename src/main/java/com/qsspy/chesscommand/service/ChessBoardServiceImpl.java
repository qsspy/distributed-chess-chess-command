package com.qsspy.chesscommand.service;

import com.qsspy.chesscommand.dao.BoardDao;
import com.qsspy.chesscommand.dao.BoardEventDao;
import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.domain.piece.*;
import com.qsspy.chesscommand.dto.kafka.GameStateMessageDTO;
import com.qsspy.chesscommand.dto.kafka.PieceStateDTO;
import com.qsspy.chesscommand.dto.request.BoardMoveRequest;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.PlayerColor;
import com.qsspy.chesscommand.enums.PlayerTurn;
import com.qsspy.chesscommand.mapper.PieceStateMapper;
import com.qsspy.chesscommand.mapper.TopicNameMapper;
import com.qsspy.chesscommand.messagebroker.MessageBrokerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

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

        List<Piece> blackPieces = getStartingPieces(PlayerColor.BLACK);
        List<Piece> whitePieces = getStartingPieces(PlayerColor.WHITE);
        Board board = new Board(blackPieces, whitePieces);

        GameStateMessageDTO gameStateMessageDTO = new GameStateMessageDTO();
        gameStateMessageDTO.setPlayerTurn(PlayerTurn.WHITE);

        List<PieceStateDTO> black = new ArrayList<>();
        for(Piece piece: blackPieces) {
            black.add(PieceStateMapper.entityToDTO(piece, board));
        }
        gameStateMessageDTO.setBlack(black);

        List<PieceStateDTO> white = new ArrayList<>();
        for(Piece piece: whitePieces) {
            white.add(PieceStateMapper.entityToDTO(piece, board));
        }
        gameStateMessageDTO.setWhite(white);

        gameStateMessageDTO.setLastBoardEvents(new ArrayList<>());
        boardDao.save(gameTopicId, board);

        brokerHandler.sendBoardMessage(TopicNameMapper.toTopicName(gameTopicId), gameStateMessageDTO);
    }

    @Override
    public void deleteGame(final UUID gameTopicId) {
        //TODO to implement
        boardDao.delete(gameTopicId);
    }

    private List<Piece> getStartingPieces(PlayerColor color) {
        List<Piece> pieces = new ArrayList<>();
        int yStartingPosition = color == PlayerColor.WHITE ? 1: 8;
        Piece leftRook = new RookPiece(color, new BoardPosition(AlphabeticPosition.A, yStartingPosition), "R1");
        Piece leftKnight = new KnightPiece(color, new BoardPosition(AlphabeticPosition.B, yStartingPosition), "KN1");
        Piece leftBishop = new BishopPiece(color, new BoardPosition(AlphabeticPosition.C, yStartingPosition), "B1");
        Piece queen = new QueenPiece(color, new BoardPosition(AlphabeticPosition.D, yStartingPosition), "Q1");
        Piece king = new KingPiece(color, new BoardPosition(AlphabeticPosition.E, yStartingPosition), "K");
        Piece rightBishop = new BishopPiece(color, new BoardPosition(AlphabeticPosition.F, yStartingPosition), "B2");
        Piece rightKnight = new KnightPiece(color, new BoardPosition(AlphabeticPosition.G, yStartingPosition), "KN2");
        Piece rightRook = new RookPiece(color, new BoardPosition(AlphabeticPosition.H, yStartingPosition), "R2");
        Collections.addAll(pieces, leftRook, leftKnight, leftBishop, queen, king, rightBishop, rightKnight, rightRook);

        yStartingPosition = color == PlayerColor.WHITE ? 2: 7;
        int pawnCode = 1;
        for(AlphabeticPosition position: AlphabeticPosition.values()) {
            pieces.add(new PawnPiece(color, new BoardPosition(position, yStartingPosition), "P" + pawnCode++));
        }

        return pieces;
    }
}
