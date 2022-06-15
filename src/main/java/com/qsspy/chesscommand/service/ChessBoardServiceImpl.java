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
import com.qsspy.chesscommand.exception.GameAlreadyExistsException;
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
public class ChessBoardServiceImpl implements ChessBoardService {

    private final MessageBrokerHandler<GameStateMessageDTO> brokerHandler;
    private final BoardDao boardDao;
    private final BoardEventDao boardEventDao;

    @Override
    public void calculateBoardStateAndSendMessage(final BoardMoveRequest request, final UUID gameTopicId) {
        Board board = boardDao.get(gameTopicId);
        if(board == null) {
            throw new ChessCommandException("Game for this room is not initialized.");
        }
        if(!canMakeMoveWithGivenColor(board, request)) {
            throw new ForbiddenMoveException("Player with this color can not make move right now!");
        }
        List<BoardEvent> boardEvents = boardEventDao.get(gameTopicId);

        GameStateMessageDTO gameStateMessageDTO = new GameStateMessageDTO();
        BoardEvent boardEvent = new BoardEvent();
        // create new BoardEvent
        if(request != null) {
            gameStateMessageDTO.setPlayerTurn(request.getColor() == PlayerColor.WHITE ? PlayerTurn.BLACK : PlayerTurn.WHITE);

            boardEvent.setFromPieceCode(request.getPieceCode());
            boardEvent.setToPieceCode(request.getSwitchPieceCode());

            List<Piece> ownPieces = request.getColor() == PlayerColor.WHITE ? board.getWhite() : board.getBlack();
            List<Piece> opponentPieces = request.getColor() == PlayerColor.WHITE ? board.getBlack() : board.getWhite();

            Piece selectedPiece = ownPieces.stream().filter(piece -> piece.getPieceCode().equals(request.getPieceCode())).findFirst().orElse(null);
            if(selectedPiece == null) throw new ChessCommandException("Selected piece not on board");
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
                ownPieces.set(selectedPieceIndex, selectedPiece);
                boardEvent.setEvent(EventType.MOVED);

                if(selectedPiece instanceof KingPiece || selectedPiece instanceof RookPiece) {
                    selectedPiece.setHasMoved(true);
                }

                String switchPieceCode = request.getSwitchPieceCode();
                if(switchPieceCode != null) {
                    switchPiece(selectedPiece, destination, switchPieceCode, request.getColor(), ownPieces);
                    boardEvent.setEvent(EventType.SWITCHED);
                }

            } else if(possibleAttacks.contains(destination)) {
                // attack
                int selectedPieceIndex = ownPieces.indexOf(selectedPiece);
                selectedPiece.setPosition(destination);
                ownPieces.set(selectedPieceIndex, selectedPiece);
                Piece opponentPieceAtDestination = opponentPieces.stream().filter(piece -> piece.getPosition().equals(destination)).findFirst().orElse(null);
                opponentPieces.remove(opponentPieceAtDestination);
                boardEvent.setEvent(EventType.KILLED);

                // king has been killed, game's finished
                if(opponentPieceAtDestination instanceof KingPiece) {
                    gameStateMessageDTO.setPlayerTurn(PlayerTurn.FINISHED);
                }

                String switchPieceCode = request.getSwitchPieceCode();
                if(switchPieceCode != null) {
                    switchPiece(selectedPiece, destination, switchPieceCode, request.getColor(), ownPieces);
                    boardEvent.setEvent(EventType.SWITCHED);
                }

            } else if(possibleSpecialMoves.contains(destination)) {
                // move pawns by two
                if(selectedPiece instanceof PawnPiece) {
                    int selectedPieceIndex = ownPieces.indexOf(selectedPiece);
                    selectedPiece.setPosition(destination);
                    ownPieces.set(selectedPieceIndex, selectedPiece);
                    boardEvent.setEvent(EventType.MOVED);
                }

                // castle
                if(selectedPiece instanceof KingPiece) {
                    int yCastlePosition = request.getColor() == PlayerColor.WHITE ? 1 : 8;
                    int selectedPieceIndex = ownPieces.indexOf(selectedPiece);
                    selectedPiece.setPosition(destination);
                    ownPieces.set(selectedPieceIndex, selectedPiece);

                    // castle left
                    if(destination.equals(new BoardPosition(AlphabeticPosition.C, yCastlePosition))) {
                        Piece leftRook = ownPieces.stream().filter(piece -> piece.getPieceCode().equals("R1")).findFirst().orElse(null);
                        int leftRookIndex = ownPieces.indexOf(leftRook);
                        if(leftRook == null) throw new ChessCommandException("Selected piece not on board");
                        leftRook.setPosition(new BoardPosition(AlphabeticPosition.D, yCastlePosition));
                        ownPieces.set(leftRookIndex, leftRook);
                    }

                    // castle right
                    if(destination.equals(new BoardPosition(AlphabeticPosition.G, yCastlePosition))) {
                        Piece rightRook = ownPieces.stream().filter(piece -> piece.getPieceCode().equals("R2")).findFirst().orElse(null);
                        int rightRookIndex = ownPieces.indexOf(rightRook);
                        if(rightRook == null) throw new ChessCommandException("Selected piece not on board");
                        rightRook.setPosition(new BoardPosition(AlphabeticPosition.F, yCastlePosition));
                        ownPieces.set(rightRookIndex, rightRook);
                    }

                    selectedPiece.setHasMoved(true);
                    boardEvent.setEvent(EventType.MOVED);
                }
            } else {
                throw new ForbiddenMoveException("Forbidden move");
            }

            boardEvent.setToPosition(destination);

            if(gameStateMessageDTO.getPlayerTurn() != PlayerTurn.FINISHED && checkIfChecked(board, ownPieces, opponentPieces)) {
                boardEvent.setEvent(EventType.CHECKED);
            }

            board.setBlack(request.getColor() == PlayerColor.BLACK ? ownPieces : opponentPieces);
            board.setWhite(request.getColor() == PlayerColor.WHITE ? ownPieces : opponentPieces);
            boardEvents.add(boardEvent);
        } else {
            gameStateMessageDTO.setPlayerTurn(PlayerTurn.WHITE);
        }


        board.setCurrentAllowedColorToMove(board.getCurrentAllowedColorToMove().inverse());
        boardDao.save(gameTopicId, board);
        boardEventDao.save(gameTopicId, boardEvents);

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
    public void initializeGame(final UUID gameTopicId) {
        Board checkIfBoardExist = boardDao.get(gameTopicId);
        if(checkIfBoardExist != null) {
            throw new GameAlreadyExistsException("Game on this topic already exists.");
        }

        List<Piece> blackPieces = getStartingPieces(PlayerColor.BLACK);
        List<Piece> whitePieces = getStartingPieces(PlayerColor.WHITE);
        Board board = new Board(PlayerColor.WHITE, blackPieces, whitePieces);
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

    private void switchPiece(Piece selectedPiece, BoardPosition destination, String switchPieceCode, PlayerColor color, List<Piece> playerPieces) {
        if(selectedPiece instanceof PawnPiece && (destination.getYPosition() == 1 || destination.getYPosition() == 8)) {
            int selectedPieceIndex = playerPieces.indexOf(selectedPiece);

            // TODO: oblicz numerek do wymienianej figury na podstawie ilości tych figur w playerPieces
            Piece switchedPiece;
            int pieceNumber = 3;
            switch(switchPieceCode) {
                case "Q":
                    switchedPiece = new QueenPiece(color, destination, "Q" + pieceNumber);
                    break;
                case "R":
                    switchedPiece = new RookPiece(color, destination, "R" + pieceNumber);
                    break;
                case "KN":
                    switchedPiece = new KnightPiece(color, destination, "KN" + pieceNumber);
                    break;
                case "B":
                    switchedPiece = new BishopPiece(color, destination, "B" + pieceNumber);
                    break;
                default:
                    throw new ChessCommandException("Error while switching piece");
            }
            playerPieces.set(selectedPieceIndex, switchedPiece);
        }
    }

    private boolean checkIfChecked(Board board, List<Piece> ownPieces, List<Piece> opponentPieces) {
        Piece opponentKing = opponentPieces.stream().filter(piece -> piece.getPieceCode().equals("K")).findFirst().orElse(null);
        if(opponentKing == null) throw new ChessCommandException("Error while checking check");
        for(Piece piece: ownPieces) {
            Set<BoardPosition> possibleAttacks = piece.getPossibleAttacks(board);
            if(possibleAttacks.contains(opponentKing.getPosition())) {
                return true;
            }
        }

        return false;
    }

    private boolean canMakeMoveWithGivenColor(final Board board, final BoardMoveRequest request) {
        return board.getCurrentAllowedColorToMove() == request.getColor();
    }
}
