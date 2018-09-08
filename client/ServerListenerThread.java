package client;

import com.TypeOfMessage;
import com.Config;
import com.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerListenerThread implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String userName;
    private boolean finish = true;
    private static boolean flagRegistretion = false;

    public ServerListenerThread(ObjectOutputStream oos, ObjectInputStream ois, String userName) {
        this.ois = ois;
        this.oos = oos;
        this.userName = userName;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (finish) {
            try {
                Message message = (Message) ois.readObject();

                if (message.getType().equals(TypeOfMessage.Ping)) {
                    oos.writeObject(new Message("Client","Ping", TypeOfMessage.Ping));
                }

                else if (message.getType().equals(TypeOfMessage.Delete)) {
                    System.out.println("Your connection had been closed");
                    System.out.println("Do you want relogin? Yes/No");
                    finish = false;
                }
                else if (message.getType().equals(TypeOfMessage.Registration)) {
                    System.out.println("[" + message.getLogin() + "]: " + message.getMessage() + " (" + message.getTime() + ")");
                }
                else if (message.getType().equals(TypeOfMessage.CheckPassword)) {
                    System.out.println("[" + message.getLogin() + "]: " + message.getMessage() + " (" + message.getTime() + ")");
                }
                else if (message.getType().equals(TypeOfMessage.Success)) {
                    flagRegistretion = true;
                    oos.writeObject(new Message("Ant", Config.HELLO_TEXT, TypeOfMessage.Registration));
                    System.out.println("[" + message.getLogin() + "]: " + "login successful. Please, write your message: " + " (" + message.getTime() + ")");
                    System.out.println("If you want delete your username, write command 'Delete username'");
                }
                else {
                    if(!message.getLogin().equals(userName)) {
                        System.out.println("[" + message.getLogin() + "]: " + message.getMessage() + " (" + message.getTime() + ")");
                    }
                }
            } catch (IOException | ClassNotFoundException  e) {
                e.printStackTrace();
            }
        }
    }
}
