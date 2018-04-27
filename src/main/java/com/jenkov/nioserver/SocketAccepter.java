package com.jenkov.nioserver;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Queue;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class SocketAccepter implements Runnable{
    private int tcpPort = 0;
    private Queue dataSocketQueue = null;

    public SocketAccepter(int tcpPort, Queue dataSocketQueue)  {
        this.tcpPort     = tcpPort;
        this.dataSocketQueue = dataSocketQueue;
    }

    public void run() {
        ServerSocketChannel listeningChannel;
        try{
            listeningChannel = ServerSocketChannel.open();
            listeningChannel.bind(new InetSocketAddress(tcpPort));
        } catch(IOException e){
            e.printStackTrace();
            return;
        }

        while(true){
            try{
                SocketChannel dataChannel = listeningChannel.accept();
                System.out.println("Socket accepted: " + dataChannel);
                //todo check if the queue can even accept more sockets.
                this.dataSocketQueue.add(new Socket(dataChannel));
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
