package com.qsspy.chesscommand.messagebroker;

public interface MessageBrokerHandler<T> {

    void sendBoardMessage(T messageBody);
}
