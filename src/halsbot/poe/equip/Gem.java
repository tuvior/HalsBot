package halsbot.poe.equip;

public class Gem {

    private String name;
    private int group;

    public Gem(String name, int group) {
        this.name = name;
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
