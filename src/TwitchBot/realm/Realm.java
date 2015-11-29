package TwitchBot.realm;

import TwitchBot.TwitchBot;
import TwitchBot.droplist.RealmDropList;

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

    public void getRealmeye() {
        bot.sendMessage(channel, realmeye);
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void getRealm(){
        bot.sendMessage(channel, "Currently in: " + realm);
    }

}
