package halsbot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Config {

    public String name;
    public String oauth;
    public String poeAccount;
    public String realmeye;
    public String twitch;
    public Set<String> mods;

    public Config() {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException {
        File conf = new File("config.properties");
        if (!conf.exists()) {
            throw new IOException("Missing config file");
        }
        Properties config = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        config.load(fis);

        name = config.getProperty("name");
        oauth = config.getProperty("oauth");
        poeAccount = config.getProperty("PoEAccount");
        realmeye = config.getProperty("realmeye");
        twitch = config.getProperty("twitchChannel");

        String mod_list [] = config.getProperty("mods").toLowerCase().split(",");
        mods = new HashSet<>(Arrays.asList(mod_list));

    }
}
