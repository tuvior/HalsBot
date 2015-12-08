package TwitchBot.realm;

import TwitchBot.TwitchBot;
import TwitchBot.droplist.Drop;
import TwitchBot.droplist.RealmDropList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static TwitchBot.jsonutil.JSONUtil.readJsonFromUrl;

public class Realm {

    private String channel;
    private TwitchBot bot;
    private RealmDropList droplist;
    private String realmeye;

    private String realm;

    public Realm(TwitchBot bot, String channel, String realmeye) {
        this.channel = channel;
        this.bot = bot;
        this.realmeye = realmeye;
        this.droplist = new RealmDropList();
        realm = "";

    }

    public void addDrop(String name) {
        String drop = droplist.addDrop(name);
        bot.sendMessage(channel, "Added: " + drop);
    }

    public void getDrops() {
        bot.sendMessage(channel, droplist.getDrops());
    }

    public void removeDrop() {
        Drop removed = droplist.removeLast();
        bot.sendMessage(channel, "Removed: " + removed);
    }

    public void getRealmeye() {
        bot.sendMessage(channel, realmeye);
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void getRealm(){
        bot.sendMessage(channel, "Currently in: " + realm);
    }

    public void getServer() {
        try {
            JSONObject realmeye = readJsonFromUrl("https://nightfirec.at/realmeye-api/?id=0J92LSv0w08");
            JSONArray characters = realmeye.getJSONArray("characters");
            String server = characters.getJSONObject(0).getString("last_server");
            bot.sendMessage(channel, "Currently in: " + server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
