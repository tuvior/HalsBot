package TwitchBot.poe.ladder;

import org.json.JSONObject;

public class Account {

    private String name;
    private String twitchName;

    public Account (String name, String twitchName) {
        this.name = name;
        this.twitchName = twitchName;
    }

    public static Account fromJson(JSONObject obj) {
        String twitch = "";
        if(obj.has("twitch")) {
            twitch = obj.getJSONObject("twitch").getString("name");
        }
        return new Account(obj.getString("name"), twitch);
    }

    public String getName() {
        return name;
    }

    public String getTwitchName() {
        return twitchName;
    }
}