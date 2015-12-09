package TwitchBot;

import TwitchBot.poe.PoE;
import TwitchBot.realm.Realm;
import TwitchBot.title.PageTitle;
import TwitchBot.userlist.UserList;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import static TwitchBot.jsonutil.JSONUtil.readJsonFromUrl;

public class TwitchBot extends PircBot {

    public Set<String> ignoredUsers;
    public String master;
    private String oauth;
    private String twitchChannel;
    private ScriptManager scripts;
    private boolean title = true;
    private UserList userList;
    public PoE poe;
    public Realm realm;

    /**
     * CONFIG
     */

    private String realmeyeLink = "http://www.realmeye.com/player/0J92LSv0w08";
    private String poeAccountName = "Hals";

    /**
     * @param name          the name of the bot
     * @param master        name of the user that will be allowed to use admin commands
     * @param oauth         authentication code for twitch connection, see https://dev.twitter.com/oauth/reference/get/oauth/authenticate
     * @param twitchChannel twitch channel the bot will operate in
     * @throws IOException
     */
    public TwitchBot(String name, String master, String oauth, String twitchChannel) throws IOException {
        setName(name);
        setEncoding("utf-8");
        scripts = new ScriptManager(this);
        poe = new PoE(this, "#" + twitchChannel, poeAccountName);
        realm = new Realm(this, "#" + twitchChannel, realmeyeLink);

        this.master = master;
        this.oauth = oauth;
        this.twitchChannel = twitchChannel;
        userList = new UserList("viewers.csv");
    }

    public void connectToTwitch() {
        try {
            connect("irc.twitch.tv", 6667, oauth);
            joinChannel("#" + twitchChannel);
            sendRawLine("CAP REQ :twitch.tv/membership");
        } catch (NickAlreadyInUseException n) {
            System.err.println("This should never happen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        echo("<" + sender + "> " + message);
        userList.updateUser(sender);
        if (message.toLowerCase().equals("!reload")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                this.sendMessage(channel, "User not authorized.");
                return;
            }
            this.sendMessage(channel, "Scripts reloaded.");
            scripts.reinit();
            return;
        }

        scripts.onMessage(channel, sender, login, hostname, message);
        try {
            log(channel, sender, message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Basic commands
        if (message.equalsIgnoreCase("!quit")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            this.quitServer("Goodbye");
        } else if (message.toLowerCase().startsWith("!join")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            if (!message.toLowerCase().contains("#")) {
                sendMessage(channel, "Channel not recognized");
                return;
            }
            String newChannel = message.substring(message.indexOf("#"));
            sendMessage(channel, "Channel " + newChannel + " joined.");
            joinChannel(newChannel);
        } else if (message.equalsIgnoreCase("!leave")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            this.partChannel(channel, "Bye.");
        } else if (message.toLowerCase().startsWith("!titlemode")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
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
        }

        // PoE Commands
        else if (message.equalsIgnoreCase("!rank")) {
            poe.rank();
        } else if (message.equalsIgnoreCase("!racerank")) {
            poe.getRaceRank();
        } else if (message.toLowerCase().startsWith("!rank")) {
            String target = message.substring(6);
            poe.rank(target);
        } else if (message.toLowerCase().startsWith("!racetime")) {
            poe.raceTimeLeft();
        } else if (message.toLowerCase().startsWith("!racerank")) {
            String target = message.substring(10);
            poe.getRaceRank(target);
        } else if (message.toLowerCase().startsWith("!track")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String[] command = message.split(" ");
            if (command.length != 2) {
                sendMessage(channel, "Invalid parameters.");
                return;
            }

            poe.track(command[1]);
        } else if (message.equalsIgnoreCase("!ladder")) {
            poe.getLadder();
        } else if (message.equalsIgnoreCase("!racemods")) {
            poe.getRaceMods();
        } else if (message.equalsIgnoreCase("!raceladder")) {
            poe.getRaceLadder();
        } else if (message.equalsIgnoreCase("!profile")) {
            poe.getProfilePage();
        } else if (message.equalsIgnoreCase("!tree")) {
            poe.tree();
        } else if (message.equalsIgnoreCase("!commands")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                String commands = "!server, !realmeye, !drops";
                sendMessage(channel, commands);
            } else {
                String commands = "!rank, !rank <accountname / charactername>, !profile, !tree, !ladder, !racerank, !racerank <accountname / charactername>, !racetime, !raceladder, !racemods, !drops";
                sendMessage(channel, commands);
            }
        }

        //Realm Commands
        else if (message.toLowerCase().startsWith("!setrealm")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String realm_ = message.substring(10);
            if (!realm_.equals("")) {
                realm.setRealm(realm_);
            }
        } else if (message.equalsIgnoreCase("!realm")) {
            realm.getRealm();
        } else if (message.equalsIgnoreCase("!server")) {
            realm.getServer();
        } else if (message.equalsIgnoreCase("!realmeye")) {
            realm.getRealmeye();
        } else if (message.equalsIgnoreCase("!drops")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                realm.getDrops();
            } else {
                poe.getDrops();
            }

        } else if (message.equalsIgnoreCase("!removedrop")) {
            if (getCurrentGame().equals("Realm of the Mad God")) {
                realm.removeDrop();
            } else {
                poe.removeDrop();
            }

        } else if (message.toLowerCase().startsWith("!adddrop")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
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
        }
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        if (!sender.equals(getName())) {
            userList.addUser(sender);
        }
        echo("Join in " + channel + " " + sender);
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        scripts.onPrivateMessage(sender, login, hostname, message);
        echo("<- <" + sender + "> " + message);
        if (message.contains("join")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                this.sendMessage(sender, "User not authorized.");
                return;
            }
            if (!message.contains("#")) {
                this.sendMessage(sender, "Channel not recognized");
                return;
            }
            String newChannel = message.substring(message.indexOf("#"));
            this.sendMessage(sender, "Channel " + newChannel + " joined.");
            this.joinChannel(newChannel);
        } else if (message.toLowerCase().contains("msg")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                this.sendMessage(sender, "User not authorized.");
                return;
            }
            String[] com = message.split(" ");
            if (com.length >= 3) {
                String messag = "";
                for (int i = 2; i < com.length; i++) {
                    messag = messag + com[i] + " ";
                }
                this.sendMessage(com[1], messag);
            }
        }
    }


    @Override
    protected void onUnknown(String line) {
        echo(line);
    }

    public void log(String channel, String nick, String message)
            throws IOException {
        File folder = new File("logs/");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File tempFile = new File("logs/" + channel + ".txt");

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile,
                true));

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());

        String log = timeStamp + " <" + nick + "> " + message;

        writer.write(log + System.getProperty("line.separator"));

        writer.close();

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

    public void echo(String message) {
        System.out.println("[" + getName() + "]: " + getTimeStamp() + ": " + message);
    }

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

}
