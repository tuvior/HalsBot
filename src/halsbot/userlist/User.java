package halsbot.userlist;

public class User {

    private String name;
    private int messages;
    private long lastSeen;

    public User(String name, int messages, long lastSeen) {
        this.name = name;
        this.messages = messages;
        this.lastSeen = lastSeen;
    }

    public String getName() {
        return name;
    }

    public long getLastSeen() {
        return lastSeen;
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
