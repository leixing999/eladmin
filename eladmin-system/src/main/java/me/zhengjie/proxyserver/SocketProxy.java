package me.zhengjie.proxyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketProxy {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket =new ServerSocket(8888);
        while(true){
            Socket socket =null;
            try{
                socket = serverSocket.accept();
                new SocketThread(socket).start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
