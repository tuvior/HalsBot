package halsbot.poe.ladder;

import org.json.JSONObject;

public class Character {

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
            case ("Assassin"):
                charClass = Class.Assassin;
                break;
            case ("Juggernaut"):
                charClass = Class.Juggernaut;
                break;
            case ("Necromancer"):
                charClass = Class.Necromancer;
                break;
            case ("Deadeye"):
                charClass = Class.Deadeye;
                break;
            case ("Saboteur"):
                charClass = Class.Saboteur;
                break;
            case ("Berserker"):
                charClass = Class.Berserker;
                break;
            case ("Elementalist"):
                charClass = Class.Elementalist;
                break;
            case ("Raider"):
                charClass = Class.Raider;
                break;
            case ("Inquisitor"):
                charClass = Class.Inquisitor;
                break;
            case ("Hierophant"):
                charClass = Class.Hierophant;
                break;
            case ("Slayer"):
                charClass = Class.Slayer;
                break;
            case ("Gladiator"):
                charClass = Class.Gladiator;
                break;
            case ("Ascendant"):
                charClass = Class.Ascendant;
                break;
            case ("Champion"):
                charClass = Class.Champion;
                break;
            case ("Trickster"):
                charClass = Class.Trickster;
                break;
            case ("Chieftain"):
                charClass = Class.Chieftain;
                break;
            case ("Occultist"):
                charClass = Class.Occultist;
                break;
            case ("Pathfinder"):
                charClass = Class.Pathfinder;
                break;
            case ("Guardian"):
                charClass = Class.Guardian;
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

    public enum Class {
        Duelist,
        Marauder,
        Ranger,
        Scion,
        Shadow,
        Templar,
        Witch,
        Assassin,
        Juggernaut,
        Necromancer,
        Deadeye,
        Saboteur,
        Berserker,
        Elementalist,
        Raider,
        Inquisitor,
        Hierophant,
        Slayer,
        Gladiator,
        Ascendant,
        Champion,
        Trickster,
        Chieftain,
        Occultist,
        Pathfinder,
        Guardian
    }

}
