package TwitchBot.poe.scoketed;

public class Gem {

    private String name;
    private int group;

    public Gem (String name, int group) {
        this.name = name;
        this.group = group;
    }

    @Override
    public String toString() {
        return name;
    }
}
