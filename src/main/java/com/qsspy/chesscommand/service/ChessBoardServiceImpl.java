package com.qsspy.chesscommand.service;

import com.qsspy.chesscommand.dao.BoardDao;
import com.qsspy.chesscommand.dao.BoardEventDao;
import com.qsspy.chesscommand.domain.Board;
import com.qsspy.chesscommand.domain.BoardEvent;
import com.qsspy.chesscommand.domain.BoardPosition;
import com.qsspy.chesscommand.domain.piece.*;
import com.qsspy.chesscommand.dto.kafka.BoardEventDTO;
import com.qsspy.chesscommand.dto.kafka.GameStateMessageDTO;
import com.qsspy.chesscommand.dto.kafka.PieceStateDTO;
import com.qsspy.chesscommand.dto.request.BoardMoveRequest;
import com.qsspy.chesscommand.enums.AlphabeticPosition;
import com.qsspy.chesscommand.enums.EventType;
import com.qsspy.chesscommand.enums.PlayerColor;
import com.qsspy.chesscommand.enums.PlayerTurn;
import com.qsspy.chesscommand.exception.ChessCommandException;
import com.qsspy.chesscommand.exception.ForbiddenMoveException;
import com.qsspy.chesscommand.mapper.BoardEventMapper;
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

        Board board = boardDao.get(gameTopicId);
        List<BoardEvent> boardEvents = boardEventDao.get(gameTopicId);

        GameStateMessageDTO gameStateMessageDTO = new GameStateMessageDTO();
        BoardEvent boardEvent = new BoardEvent();
        // zrobienie nowego eventu
        if(request != null) {
            // TODO: sprawdz czy szach

            boardEvent.setFromPieceCode(request.getPieceCode());
            boardEvent.setToPieceCode(request.getSwitchPieceCode());
            List<Piece> ownPieces = request.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
            List<Piece> opponentPieces = request.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();

            Piece selectedPiece = ownPieces.stream().filter(piece -> piece.getPieceCode().equals(request.getPieceCode())).findFirst().orElse(null);
            if(selectedPiece == null) throw new ChessCommandException("Selected piece not found on board");
            boardEvent.setFromPosition(selectedPiece.getPosition());

            // move or attack
            BoardPosition destination = new BoardPosition(request.getDestination());
            Set<BoardPosition> possibleMoves = selectedPiece.getPossibleMoves(board);
            Set<BoardPosition> possibleAttacks = selectedPiece.getPossibleAttacks(board);
            Set<BoardPosition> possibleSpecialMoves = selectedPiece.getPossibleSpecialMoves(board);
            if(possibleMoves.contains(destination)) {
                // move
                int selectedPieceIndex = ownPieces.indexOf(selectedPiece);
                selectedPiece.setPosition(destination);
                ownPieces.add(selectedPieceIndex, selectedPiece);
                boardEvent.setEvent(EventType.MOVED);

            } else if(possibleAttacks.contains(destination)) {
                // attack
                int selectedPieceIndex = ownPieces.indexOf(selectedPiece);
                selectedPiece.setPosition(destination);
                ownPieces.add(selectedPieceIndex, selectedPiece);
                Piece opponentPieceAtDestination = opponentPieces.stream().filter(piece -> piece.getPosition().equals(destination)).findFirst().orElse(null);
                opponentPieces.remove(opponentPieceAtDestination);
                boardEvent.setEvent(EventType.KILLED);

            } else if(possibleSpecialMoves.contains(destination)) {
                // TODO: special move
                // move pawns by two

                // roszada

                // wymiana na królową
                throw new ForbiddenMoveException("Forbidden move");
            } else {
                throw new ForbiddenMoveException("Forbidden move");
            }

            boardEvent.setToPosition(destination);

            board.setBlack(request.getColor() == PlayerColor.BLACK ? ownPieces : opponentPieces);
            board.setWhite(request.getColor() == PlayerColor.WHITE ? ownPieces : opponentPieces);

            gameStateMessageDTO.setPlayerTurn(request.getColor() == PlayerColor.WHITE ? PlayerTurn.BLACK : PlayerTurn.WHITE);
        } else {
            gameStateMessageDTO.setPlayerTurn(PlayerTurn.WHITE);
        }

        boardEvents.add(boardEvent);

        boardDao.save(gameTopicId, board);
        boardEventDao.save(gameTopicId, boardEvents);

        // jezeli szach mat / stalemate - turn ustawiony na finished
        // ustawić na request startowy if null to board event pusty i turn white ;


        // convert to dto
        List<PieceStateDTO> black = new ArrayList<>();
        for(Piece piece: board.getBlack()) {
            black.add(PieceStateMapper.entityToDTO(piece, board));
        }
        gameStateMessageDTO.setBlack(black);

        List<PieceStateDTO> white = new ArrayList<>();
        for(Piece piece: board.getWhite()) {
            white.add(PieceStateMapper.entityToDTO(piece, board));
        }
        gameStateMessageDTO.setWhite(white);

        List<BoardEventDTO> events = new ArrayList<>();
        for(BoardEvent event: boardEvents) {
            events.add(BoardEventMapper.entityToDTO(event));
        }
        gameStateMessageDTO.setLastBoardEvents(events);

        brokerHandler.sendBoardMessage(TopicNameMapper.toTopicName(gameTopicId), gameStateMessageDTO);
    }

    @Override
    public void initialiseGame(final UUID gameTopicId) {
        List<Piece> blackPieces = getStartingPieces(PlayerColor.BLACK);
        List<Piece> whitePieces = getStartingPieces(PlayerColor.WHITE);
        Board board = new Board(blackPieces, whitePieces);
        boardDao.save(gameTopicId, board);
        calculateBoardStateAndSendMessage(null, gameTopicId);
    }

    @Override
    public void deleteGame(final UUID gameTopicId) {
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
