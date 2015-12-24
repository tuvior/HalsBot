package halsbot;

import java.io.IOException;

public class RunBot {

    public static void main(String[] args) throws IOException {
        TwitchBot bot = new TwitchBot();
        bot.connectToTwitch();
    }

}