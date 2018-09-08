package com;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Integer password;
    private ObjectInputStream ois;
    private ObjectOutput oos;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public Client(Socket socket, ObjectOutput ThisOutputStream, ObjectInputStream ThisInputStream) {
        this.socket = socket;
        oos = ThisOutputStream;
        ois = ThisInputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getThisObjectInputStream() {
        return ois;
    }

    public ObjectOutput getThisObjectOutpudStream() {
        return oos;
    }

    public void setThisObjectOutpudStream(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void setThisObjectInputStream(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void setPassword(String pass) {
         password = pass.hashCode();
    }

    public boolean checkPassword (String pass) {
        return pass.hashCode() == password;
    }
}
