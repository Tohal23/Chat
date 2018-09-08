package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory implements Serializable {
    private ArrayList<Message> history;

    public ChatHistory () {
        history = new ArrayList<>(Config.HISTORY_LENGHT);
    }

    public void addMessage(Message message){
        if (history.size() >= Config.HISTORY_LENGHT) {
            history.remove(0);
        }
        if (message.getType().equals(TypeOfMessage.Message)) {
            history.add(message);
        }
    }

    public List<Message> getHistory() {
        return history;
    }
}