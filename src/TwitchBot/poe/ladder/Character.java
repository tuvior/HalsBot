package TwitchBot.poe.ladder;

import org.json.JSONObject;

public class Character {

    public enum Class {
        Duelist,
        Marauder,
        Ranger,
        Scion,
        Shadow,
        Templar,
        Witch
    }

    private String name;
    private int level;
    private Class charClass;
    private long exp;
    private long time;

    public Character(String name, int level, Class charClass, long exp, long time) {
        this.name = name;
        this.level = level;
        this.charClass = charClass;
        this.exp = exp;
        this.time = time;
    }

    public static Character fromJson(JSONObject obj) {
        Class charClass = null;
        String cl = obj.getString("class");

        long experience = -1;
        long time = -1;

        if (obj.has("experience")) {
            experience = obj.getLong("experience");
        }

        if (obj.has("time")) {
            time = obj.getLong("time");
        }

        switch (cl) {
            case ("Marauder"):
                charClass = Class.Marauder;
                break;
            case ("Duelist"):
                charClass = Class.Duelist;
                break;
            case ("Scion"):
                charClass = Class.Scion;
                break;
            case ("Shadow"):
                charClass = Class.Shadow;
                break;
            case ("Witch"):
                charClass = Class.Witch;
                break;
            case ("Templar"):
                charClass = Class.Templar;
                break;
            case ("Ranger"):
                charClass = Class.Ranger;
                break;
        }
        return new Character(obj.getString("name"), obj.getInt("level"), charClass, experience, time);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Class getCharClass() {
        return charClass;
    }

    public long getExp() {
        return exp;
    }

}
