package halsbot;

import org.jibble.pircbot.User;

public class ScriptEvent {

    private TwitchBot bot;

    public ScriptEvent(TwitchBot bot) {
        this.bot = bot;
    }

    public void sendMessage(String target, String message) {
        bot.sendMessage(target, message);
    }

    public void joinChannel(String newChannel) {
        bot.joinChannel(newChannel);
    }

    public void joinChannel(String newChannel, String key) {
        bot.joinChannel(newChannel, key);
    }

    public void changeNick(String name) {
        bot.changeNick(name);
    }

    public void sendAction(String target, String action) {
        bot.sendAction(target, action);
    }

    public void partChannel(String channel) {
        bot.partChannel(channel);
    }

    public void partChannel(String channel, String reason) {
        bot.partChannel(channel, reason);
    }

    public void quitServer(String reason) {
        bot.quitServer(reason);
    }

    public void quitServer() {
        bot.quitServer();
    }

    public User[] getUsers(String channel) {
        return bot.getUsers(channel);
    }

    public String getNick() {
        return bot.getNick();
    }

    public String getName() {
        return bot.getName();
    }

    public void sendNotice(String target, String notice) {
        bot.sendNotice(target, notice);
    }

    public void kick(String target, String channel) {
        bot.kick(channel, target);
    }

    public void setMode(String channel, String mode) {
        bot.setMode(channel, mode);
    }

    public void sendInvite(String nick, String channel) {
        bot.sendInvite(nick, channel);
    }

    public void ban(String channel, String hostmask) {
        bot.ban(channel, hostmask);
    }

    public void unBan(String channel, String hostmask) {
        bot.unBan(channel, hostmask);
    }

    public void op(String channel, String nick) {
        bot.op(channel, nick);
    }

    public void setTopic(String channel, String topic) {
        bot.setTopic(channel, topic);
    }


}
