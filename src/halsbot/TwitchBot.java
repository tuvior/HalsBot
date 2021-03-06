package halsbot;

import halsbot.config.Config;
import halsbot.poe.PoE;
import halsbot.realm.Realm;
import halsbot.title.PageTitle;
import halsbot.userlist.UserList;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static halsbot.webutil.WebUtil.readJsonFromUrl;

public class TwitchBot extends PircBot {

    private static final String now_playing_url = "http://sub.fm/now-playing.php";

    private PoE poe;
    private Realm realm;
    private String oauth;
    private String twitchChannel;
    private ScriptManager scripts;
    private boolean title = true;
    private UserList userList;
    private CoffeeCounter coffeeCounter;
    private Set<String> mods;
    private String editorOAuth;
    private ChatLogger logger;

    public TwitchBot() throws IOException {
        Config configuration = Config.loadConfig();

        setMessageDelay(1300);
        setName(configuration.name);
        setEncoding("utf-8");
        scripts = new ScriptManager(this);
        poe = new PoE(this, "#" + configuration.twitch, configuration.poeAccount);
        realm = new Realm(this, "#" + configuration.twitch, configuration.realmeye);
        coffeeCounter = CoffeeCounter.loadCoffeeCounter();
        mods = configuration.mods;
        oauth = configuration.oauth;
        twitchChannel = configuration.twitch;
        editorOAuth = configuration.editorOauth;
        userList = UserList.loadUserList();
        logger = new ChatLogger("#" + twitchChannel);
    }

    private static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    public void connectToTwitch() {
        try {
            connect("irc.twitch.tv", 6667, oauth);
            joinChannel("#" + twitchChannel);
            sendRawLine("CAP REQ :twitch.tv/membership");
            dynamicTitleUpdateTask();
        } catch (NickAlreadyInUseException n) {
            System.err.println("This should never happen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        echo("<" + sender + "> " + message);
        if (isCommand(message, "!reload")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            sendMessage(channel, "Scripts reloaded.");
            scripts.reinit();
            return;
        }

        if (streambotCheck(message, sender)) {
            banUser(channel, sender);
            echo("banned " + sender + " for streambot advertising");
            return;
        }

        scripts.onMessage(channel, sender, login, hostname, message);
        logger.log(sender, message);

        // Basic commands
        if (isCommand(message, "!quit")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            quitServer("Goodbye");
        } else if (isCommandWithParams(message, "!join")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }

            String newChannel = "#" + message.substring(6).trim();
            sendMessage(channel, "Trying to join " + newChannel + ".");
            joinChannel(newChannel);
        } else if (isCommand(message, "!leave")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            partChannel(channel, "Bye.");
        } else if (isCommandWithParams(message, "!titlemode")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String[] command = message.split(" ");
            if (command.length != 2) {
                sendMessage(channel, "Invalid parameters.");
                return;
            }

            if (command[1].toLowerCase().equals("on")) {
                title = true;
                sendMessage(channel, "Title function enabled.");
            } else if (command[1].toLowerCase().equals("off")) {
                title = false;
                sendMessage(channel, "Title function disabled.");
            } else {
                sendMessage(channel, "Invalid parameters.");
            }
        } else if (isCommand(message, "!about")) {
            sendMessage(channel, "HalsBot by Tuvior, https://github.com/tuvior/HalsBot");
        } else if (isCommand(message, "!coffee+")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            coffeeCounter.addCoffee(1);
            sendMessage(channel, "Added a coffee");
        } else if (isCommand(message, "!coffee++")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            coffeeCounter.addCoffee(2);
            sendMessage(channel, "Added 2 coffees");
        } else if (isCommand(message, "!coffee")) {
            sendMessage(channel, "Drank " + coffeeCounter.getCoffee() + " coffees.");
        } else if (isCommand(message, "!music")) {
            sendMessage(channel, getCurrentlyPlaying());
        } else if (isCommand(message, "!uptime")) {
            sendMessage(channel, getUptime());
        } else if (isCommandWithParams(message, "!title")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            if (updateStream(message.substring(7))) {
                sendMessage(channel, "Title updated");
            } else {
                sendMessage(channel, "Error");
            }
        } else if (isCommand(message, "!top")) {
            sendMessage(channel, userList.getTopViewers());
        }

        // PoE Commands
        else if (isCommand(message, "!rank")) {
            poe.rank();
        } else if (isCommand(message, "!racerank")) {
            poe.getRaceRank();
        } else if (isCommandWithParams(message, "!rank")) {
            String target = message.substring(6);
            poe.rank(target, true);
        } else if (isCommand(message, "!racetime")) {
            poe.raceTimeLeft();
        } else if (isCommandWithParams(message, "!racerank")) {
            String target = message.substring(10);
            poe.getRaceRank(target);
        } else if (isCommandWithParams(message, "!track")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String[] command = message.split(" ");
            if (command.length != 2) {
                sendMessage(channel, "Invalid parameters.");
                return;
            }

            poe.track(command[1]);
        } else if (isCommand(message, "!ladder")) {
            poe.getLadder();
        } else if (isCommand(message, "!racemods")) {
            poe.getRaceMods();
        } else if (isCommand(message, "!raceladder")) {
            poe.getRaceLadder();
        } else if (isCommand(message, "!profile")) {
            poe.getProfilePage();
        } else if (isCommand(message, "!filter")) {
            poe.lootfilter();
        } else if (isCommand(message, "!tree")) {
            poe.getSkillTree();
        } else if (isCommand(message, "!gems")) {
            poe.getGems();
        } else if (isCommand(message, "!curses")) {
            poe.getCurses();
        } else if (isCommand(message, "!halschallenge")) {
            poe.halsChallenge();
        } else if (isCommand(message, "!rip")) {
            poe.getRips();
        } else if (isCommandWithParams(message, "!addrip")) {
            String params = message.substring(8);
            poe.addRip(params.substring(0, params.lastIndexOf(" ")), params.substring(params.lastIndexOf(" ") + 1));
        } else if (isCommand(message, "!commands")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                String commands = "!server, !realmeye, !drops, !uptime, !coffee, !music, !about";
                sendMessage(channel, commands);
            } else {
                String commands = "!rank, !rank <accountname / charactername>, !profile, !tree, !gems, !curses, !filter, !halschallenge, !ladder, !racerank, !racerank <accountname / charactername>, !racetime, !raceladder, !racemods, !drops, !uptime,!coffee, !music, !about";
                sendMessage(channel, commands);
            }
        }

        //Realm Commands
        else if (isCommandWithParams(message, "!setrealm")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String realm_ = message.substring(10);
            if (!realm_.equals("")) {
                realm.setRealm(realm_);
            }
        } else if (isCommand(message, "!realm")) {
            realm.getRealm();
        } else if (isCommand(message, "!server")) {
            realm.getServer();
        } else if (isCommand(message, "!realmeye")) {
            realm.getRealmeye();
        } else if (isCommand(message, "!drops")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                realm.getDrops();
            } else {
                poe.getDrops();
            }

        } else if (isCommand(message, "!removedrop")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                realm.removeDrop();
            } else {
                poe.removeDrop();
            }

        } else if (isCommandWithParams(message, "!adddrop")) {
            if (!isMod(sender)) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String drop = message.substring(9);
            if (!drop.equals("")) {
                if (getCurrentGame().equals("Realm of the Mad God")) {
                    realm.addDrop(drop);
                } else {
                    poe.addDrop(drop);
                }
            }
        }

        //title
        else if (title && PageTitle.checkForUrl(message) && !sender.toLowerCase().equals("nightbot")) {
            sendMessage(channel, PageTitle.getPageTitle(message));
            userList.addMessage(sender);
        } else {
            userList.addMessage(sender);
        }

    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        if (!sender.equalsIgnoreCase(getName())) {
            userList.addUser(sender);
            echo(sender + " joined");
        }
    }

    @Override
    protected void onUnknown(String line) {
        echo(line);
    }

    private void dynamicTitleUpdateTask() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            if (getStreamStart() != null && getCurrentGame().equals("Path of Exile")) {
                poe.updateTitleTags();
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public void banUser(String user, String channel) {
        sendMessage(channel, ".ban " + user);
    }

    public void unbanUser(String user, String channel) {
        sendMessage(channel, ".unban " + user);
    }

    public void timeoutUser(String user, String channel) {
        sendMessage(channel, ".timeout " + user);
    }

    public void timeoutUser(String user, String channel, long seconds) {
        sendMessage(channel, ".timeout " + user + " " + seconds);
    }

    public void slowMode(String channel, long seconds) {
        sendMessage(channel, ".slow " + seconds);
    }

    public void slowOff(String channel) {
        sendMessage(channel, ".slowoff");
    }

    public void subscribersOn(String channel) {
        sendMessage(channel, ".subscribers");
    }

    public void subscriversOff(String channel) {
        sendMessage(channel, ".subscribersoff");
    }

    private Date getStreamStart() {
        try {
            JSONObject t_stream = readJsonFromUrl("https://api.twitch.tv/kraken/streams/" + twitchChannel);
            if (!t_stream.isNull("stream")) {
                String start = t_stream.getJSONObject("stream").getString("created_at");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMAN);
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                return format.parse(start);
            } else {
                return null;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isMod(String user) {
        return mods.contains(user.toLowerCase());
    }

    private String getCurrentGame() {
        try {
            JSONObject t_channel = readJsonFromUrl("https://api.twitch.tv/kraken/channels/" + twitchChannel);
            String game;
            try {
                game = t_channel.getString("game");
            } catch (JSONException e) {
                game = "";
            }
            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private boolean isCommand(String text, String command) {
        return text.equalsIgnoreCase(command);
    }

    private boolean isCommandWithParams(String text, String command) {
        return text.toLowerCase().startsWith(command);
    }

    private boolean streambotCheck(String message, String sender) {
        return message.toLowerCase().contains("streambot") && userList.getMessages(sender) <= 1;
    }

    private String getCurrentlyPlaying() {
        try {
            InputStream is = new URL(now_playing_url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return rd.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean updateStream(String title) {
        try {
            URL url = new URL("https://api.twitch.tv/kraken/channels/" + twitchChannel);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Authorization", "OAuth " + editorOAuth);
            httpCon.setRequestProperty("content-type", "application/json");
            httpCon.setRequestMethod("PUT");
            JSONObject channel = new JSONObject();
            JSONObject update = new JSONObject();
            channel.put("status", title);
            update.put("channel", channel);
            httpCon.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
            wr.writeBytes(update.toString());
            wr.flush();
            wr.close();

            if (httpCon.getResponseCode() == 200) {
                httpCon.getInputStream();
                return true;
            } else {
                httpCon.getErrorStream();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStatus() {
        try {
            JSONObject t_channel = readJsonFromUrl("https://api.twitch.tv/kraken/channels/" + twitchChannel);
            String status;
            try {
                status = t_channel.getString("status");
            } catch (JSONException e) {
                status = "";
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getUptime() {
        Date start_date = getStreamStart();
        if (start_date != null) {
            long diffInSeconds = (new Date().getTime() - start_date.getTime()) / 1000;

            long diff[] = new long[]{0, 0, 0, 0};
            diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
            diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
            diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
            diff[0] = (diffInSeconds / 24);

            if (diff[0] > 0) {
                return "Stream has been up for " + diff[0] + " days " + diff[1] + " hours and " + diff[2] + "minutes.";
            } else if (diff[1] > 0) {
                return "Stream has been up for " + diff[1] + " hours and " + diff[2] + " minutes";
            } else if (diff[2] > 0) {
                return "Stream has been up for " + diff[2] + " minutes";
            } else {
                return "Stream just went online!";
            }
        } else {
            return "Stream is offline.";
        }
    }

    public void echo(String message) {
        System.out.println("[" + getTimeStamp() + "][" + getName() + "] " + message);
    }
}
