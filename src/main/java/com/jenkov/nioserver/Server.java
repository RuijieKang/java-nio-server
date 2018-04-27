package com.jenkov.nioserver;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by jjenkov on 24-10-2015.
 */
public class Server {
    /*Acceptor configurations*/
    private int tcpPort;

    /*Processor configurations*/
    private IMessageReaderFactory messageReaderFactory;
    private IMessageProcessor     messageProcessor;

    //Exchange the connected sockets between acceptor and processor threads
    private Queue socketQueue;

    /*Configuration method*/
    public Server(int tcpPort, int capacity, IMessageReaderFactory messageReaderFactory, IMessageProcessor messageProcessor) {
        this.tcpPort = tcpPort;
        this.socketQueue = new ArrayBlockingQueue(capacity);
        this.messageReaderFactory = messageReaderFactory;
        this.messageProcessor = messageProcessor;
    }

    /*Functional (bootstrap) method*/
    public void start() throws IOException {
        startAcceptor();
        startProcessor();
    }

    private void startAcceptor() {
        SocketAccepter socketAccepter  = new SocketAccepter(tcpPort, socketQueue);
        Thread accepterThread  = new Thread(socketAccepter);
        accepterThread.start();
    }

    private void startProcessor() throws IOException {
        MessageBuffer readBuffer  = new MessageBuffer();
        MessageBuffer writeBuffer = new MessageBuffer();
        SocketProcessor socketProcessor = new SocketProcessor(socketQueue, readBuffer, writeBuffer,  this.messageReaderFactory, this.messageProcessor);
        Thread processorThread = new Thread(socketProcessor);
        processorThread.start();
    }
}
