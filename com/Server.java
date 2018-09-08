package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static ChatHistory chatHistory = new ChatHistory();
    private static UserList userList = new UserList();

    public static void main(String[] args) {
        try {
            ServerSocket socketWait = new ServerSocket(Config.PORT);

            while (true) {
                Socket client = null;
                while (client == null) {
                    client = socketWait.accept();
                }
                new ClientThread(client);
            }
        } catch (SocketException e) {
            System.out.println("Socket exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O exception");
            e.printStackTrace();
        }
    }

    public synchronized static ChatHistory getChatHistory() {
        return chatHistory;
    }

    public synchronized static UserList getUserList() {
        return userList;
    }
}