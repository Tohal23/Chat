package com;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread{

    private final static int DELAY = 30000;

    private Socket socket;
    private Message c;
    private String login;
    private int inPacks = 0;
    private int outPacks = 0;
    private Timer timer;
    private boolean flag = false;

    public ClientThread(Socket client) {
        socket = client;
        this.start();
    }

    @Override
    public void run() {
        try {
            final ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            final ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream());

            this.c = (Message) inputStream.readObject();
            login = c.getLogin();

            if (c.getType().equals(TypeOfMessage.Message)) {
                System.out.println("[" + c.getLogin() + "]: " + c.getMessage());
                Server.getChatHistory().addMessage(c);

            } else if (c.getMessage().equals(Config.HELLO_TEXT)){
                List<Message> history = Server.getChatHistory().getHistory();
                for (Message message : history) {
                    outputStream.writeObject(message);
                }
                broadcast(Server.getUserList().getClients(), new Message("Server", login + "has been connected", TypeOfMessage.Message));
            }

            Server.getUserList().addUser(socket, login, outputStream, inputStream);

            c.setUsers(Server.getUserList().getUsers());


            timer = new Timer(DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (inPacks == outPacks) {
                            outputStream.writeObject(new Message("Server","Ping", TypeOfMessage.Ping));
                            outPacks++;
                            System.out.println("Outpacks: " + outPacks);
                        }
                        else {
                            throw new SocketException();
                        }
                    } catch (SocketException se) {
                        System.out.println(login + " disconnect");
                        Server.getUserList().deleteClient(login);
                        broadcast(Server.getUserList().getClients(), new Message("Server", login + " has been disconnected", TypeOfMessage.Message));
                        flag = true;
                        timer.stop();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            timer.start();

            while (true) {
                if (flag) {
                    flag = false;
                    break;
                }

                c = (Message) inputStream.readObject();

                if (c.getType().equals(TypeOfMessage.Ping)) {
                    inPacks++;
                    System.out.println("InputPacks: " + inPacks);
                }

                else if (c.getType().equals(TypeOfMessage.Delete)) {
                    System.out.println(c.getLogin() + " delete himself");
                    boolean deleteAnswer = UserRegistration.deleteUsername(c.getLogin());

                    if (deleteAnswer) {
                        outputStream.writeObject(new Message("Server", "Your user name had been delete", TypeOfMessage.Message));
                        outputStream.writeObject(new Message("Server", "You will be disconnect", TypeOfMessage.Message));
                        outputStream.writeObject(new Message("Server", TypeOfMessage.Delete));
                        timer.stop();
                    }
                }

                else if (c.getType().equals(TypeOfMessage.Message)) {
                    Server.getChatHistory().addMessage(c);
                    System.out.println("Send message: [" + c.getLogin() + "]: " + c.getMessage() );
                    System.out.println("Send all users: " + c.getMessage());
                    broadcast(Server.getUserList().getClients(), c);
                }

                else if (c.getMessage().equals(Config.HELLO_TEXT)) {
                    List<Message> history = Server.getChatHistory().getHistory();
                    for (Message message : history) {
                        outputStream.writeObject(message);
                    }
                    broadcast(Server.getUserList().getClients(), new Message("Server",login + " has been connected", TypeOfMessage.Message));
                }
            }
        }
        catch (SocketException | EOFException e) {
            System.out.println(c.getLogin() + " disconnected");
            Server.getUserList().deleteClient(login);
            broadcast(Server.getUserList().getClients(), new Message("Server", c.getLogin() + " has been disconnected", TypeOfMessage.Message));
            timer.stop();
        }
        catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(ArrayList<Client> clients, Message message) {
        try {
            for (Client client : clients) {
                client.getThisObjectOutpudStream().writeObject(message);
            }
        }
        catch (SocketException e) {
            System.out.println(" Disconnect " + login);
            Server.getUserList().deleteClient(login);
            broadcast(Server.getUserList().getClients(), new Message("Server", login + " disconnect", TypeOfMessage.Message));
            timer.stop();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
