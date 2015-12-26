package halsbot.userlist;

public class User {

    private String name;
    private int messages;

    public User(String name, int messages) {
        this.name = name;
        this.messages = messages;
    }

    public int getMessages() {
        return messages;
    }

    public void addMessage() {
        messages++;
    }

    @Override
    public String toString() {
        return name + "," + messages;
    }
}
