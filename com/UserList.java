package com;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class UserList {
    private Map<String, Client> onlineUsers = new HashMap<String, Client>();
    private boolean flag;

    public void addUser(Socket socket, String login, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, SAXException, ParserConfigurationException {

        if (!UserRegistration.checkRegistration(login)) {
            oos.writeObject(new Message("Server", "Write new password...", TypeOfMessage.Registration));
            Message answerForLogin = (Message) ois.readObject();
            UserRegistration.registration(login, answerForLogin.getMessage());
            oos.writeObject(new Message("Server", TypeOfMessage.Success));
        } else {
            while (!flag) {
                oos.writeObject(new Message("Server", "Hello, " + login + " !", TypeOfMessage.Registration));
                oos.writeObject(new Message("Server", "Write your password...", TypeOfMessage.CheckPassword));
                Message answerForPassword = (Message) ois.readObject();
                flag = UserRegistration.checkPassword(login, answerForPassword.getMessage());
                if (flag) {
                    oos.writeObject(new Message("Server",  TypeOfMessage.Success ));
                }
            }
            flag = false;
        }


        if (onlineUsers.size() >= Config.SUMM_ONLINE) {
            return;
        }

        System.out.println(login + " Connected");

        if (!onlineUsers.containsKey(login)) {
            onlineUsers.put(login, new Client(socket, oos, ois));
        } else {
            int i = 1;
            while (onlineUsers.containsKey(login)) {
                login = login + i;
                i++;
            }

            onlineUsers.put(login, new Client(socket, oos, ois));
        }
    }

    public void deleteClient(String login) {
        onlineUsers.remove(login);
    }

    public String[] getUsers() {
        return onlineUsers.keySet().toArray(new String[0]);
    }

    public ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();

        for (Map.Entry<String, Client> client : onlineUsers.entrySet()) {
            clients.add(client.getValue());
        }

        return clients;
    }
}
