package com.qsspy.chesscommand.messagebroker;

import com.qsspy.chesscommand.dto.kafka.GameStateMessageDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class KafkaHandler implements MessageBrokerHandler<GameStateMessageDTO> {

    private KafkaTemplate<String, GameStateMessageDTO> kafkaTemplate;

    @Autowired
    public KafkaHandler(KafkaTemplate<String, GameStateMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendBoardMessage(final String gameTopicId, final GameStateMessageDTO messageBody) {
        //TODO to implement
        kafkaTemplate.send(gameTopicId, messageBody);


    }
}
