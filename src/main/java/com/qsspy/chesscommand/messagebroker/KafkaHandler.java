package com.qsspy.chesscommand.messagebroker;

import com.qsspy.chesscommand.dto.kafka.GameStateMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class KafkaHandler implements MessageBrokerHandler<GameStateMessageDTO> {

    @Override
    public void sendBoardMessage(final GameStateMessageDTO messageBody) {
        //TODO to implement
    }
}
