package halsbot.poe.ladder;

import org.json.JSONObject;

public class LadderEntry {

    private boolean online;
    private int rank;
    private boolean dead;
    private Character character;
    private Account account;

    public LadderEntry(boolean online, int rank, boolean dead, Character character, Account account) {
        this.online = online;
        this.rank = rank;
        this.dead = dead;
        this.character = character;
        this.account = account;
    }

    public static LadderEntry fromJson(JSONObject obj) {
        Character character = Character.fromJson(obj.getJSONObject("character"));
        Account account = Account.fromJson(obj.getJSONObject("account"));

        return new LadderEntry(obj.getBoolean("online"), obj.getInt("rank"), obj.getBoolean("dead"), character, account);
    }

    public boolean getOnline() {
        return online;
    }

    public boolean getDead() {
        return dead;
    }

    public int getRank() {
        return rank;
    }

    public Character getCharacter() {
        return character;
    }

    public Account getAccount() {
        return account;
    }

}
