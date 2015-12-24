package halsbot;

import org.jibble.pircbot.User;

import java.util.HashSet;
import java.util.Set;

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

    public String[] getPingList(String channel) {
        Set<String> result = new HashSet<>();

        User[] us = bot.getUsers(channel);

        String[] newl = new String[0];


        for (User uss : us) {
            System.out.println(uss.toString());
            String nick = uss.toString();
            if (nick.contains("%") || nick.contains("@") || nick.contains("+")) {
                nick = nick.substring(1);
            }
            if (!bot.ignoredUsers.contains(nick)) {
                result.add(nick);
            }
        }
        return result.toArray(newl);
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
