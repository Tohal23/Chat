package client;


import com.Config;
import com.Message;
import com.TypeOfMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        String repeat;

        do {
            boolean finish = true;
            Scanner input = new Scanner(System.in);

            System.out.println("Hello, it's our chat! Write your nickname");
            System.out.println("Write your message and press \"Enter\"");

            String username = input.nextLine();

            Socket socket = new Socket(InetAddress.getLocalHost(), Config.PORT);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String message = null;

            new ServerListenerThread(oos, ois, username);

            oos.writeObject(new Message(username, TypeOfMessage.Message));
            while (finish) {
                message = input.nextLine();
                if (message.equals("Delete username")) {
                    oos.writeObject(new Message(username, message, TypeOfMessage.Delete ));
                    finish = false;
                }
                else {
                    oos.writeObject(new Message(username, message, TypeOfMessage.Message));
                }
            }

            repeat = input.nextLine();
            while (!(repeat.equals("Yes") || repeat.equals("Y") || repeat.equals("yes") || repeat.equals("y") ||
                    repeat.equals("No") || repeat.equals("N") || repeat.equals("no") || repeat.equals("n"))) {
                System.out.println("Do you want relogin? Yes/No");
                repeat = input.nextLine();
            }
        } while (repeat.equals("Yes") || repeat.equals("Y") || repeat.equals("yes") || repeat.equals("y"));
    }
}
