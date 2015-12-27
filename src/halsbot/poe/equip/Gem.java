package halsbot.poe.equip;

public class Gem {

    private String name;
    private int group;
    private String properties;

    public Gem(String name, int group, String properties) {
        this.name = name;
        this.group = group;
        this.properties = properties;
    }

    public int getGroup() {
        return group;
    }

    public boolean isCurse() {
        return properties.contains("Curse") && !name.equals("Curse On Hit");
    }

    public boolean isAura() {
        return properties.contains("Aura");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
