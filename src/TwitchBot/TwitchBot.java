package TwitchBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import TwitchBot.droplist.RealmDropList;
import TwitchBot.poe.PoE;
import TwitchBot.realm.Realm;
import TwitchBot.userlist.UserList;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONObject;

import static TwitchBot.jsonutil.JSONUtil.readJsonFromUrl;

public class TwitchBot extends PircBot {

    private static final Pattern url = Pattern.compile("(([a-zA-Z0-9]{1,6})://)?([_a-zA-Z\\d\\-]+(\\.[_a-zA-Z\\d\\-]+)+)(([_a-zA-Z\\d\\-\\\\\\./?=&#]+[_a-zA-Z\\d\\-\\\\/])+)*");
    private static final Pattern TITLE_TAG = Pattern.compile("<title>(.*)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    public Set<String> ignoredUsers;

    public String master;
    private String oauth;
    private String twitchChannel;

    private ScriptManager scripts;
    private boolean title = true;
    public PoE poe;
    public Realm realm;
    private UserList userList;

    /**
     * @param name the name of the bot
     * @param master name of the user that will be allowed to use admin commands
     * @param oauth authentication code for twitch connection, see https://dev.twitter.com/oauth/reference/get/oauth/authenticate
     * @param twitchChannel twitch channel the bot will operate in
     * @throws IOException
     */
    public TwitchBot(String name, String master, String oauth, String twitchChannel) throws IOException {
        setName(name);
        setEncoding("utf-8");
        scripts = new ScriptManager(this);
        poe = new PoE(this, "#" + twitchChannel, "Hals");
        realm = new Realm(this, "#" + twitchChannel, "https://www.realmeye.com/player/zQe50cWAgsb");

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
    protected void onUnknown(String line) {
        System.out.println(line);
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
        }  else if (message.equalsIgnoreCase("!commands")) {
            String commands = "!rank, !rank <accountname>, !profile, !ladder, !racerank, !racerank <accountname / charactername>, !racetime, !raceladder, !racemods";
            sendMessage(channel, commands);
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
        } else if (message.equalsIgnoreCase("!realmeye")) {
            realm.getRealmeye();
        }


        else if (message.equalsIgnoreCase("!drops")) {
            try {
                JSONObject t_channel = readJsonFromUrl("https://api.twitch.tv/kraken/channels/" + twitchChannel);
                if (t_channel.getString("game").equals("Realm of the Mad God")) {
                    realm.getDrops();
                } else {
                    poe.getDrops();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (message.toLowerCase().startsWith("!adddrop")) {
            if (!sender.equalsIgnoreCase(master) && !sender.equalsIgnoreCase("tuvior")) {
                sendMessage(channel, "User not authorized.");
                return;
            }
            String drop = message.substring(9);
            if (!drop.equals("")) {
                try {
                    JSONObject t_channel = readJsonFromUrl("https://api.twitch.tv/kraken/channels/" + twitchChannel);
                    if (t_channel.getString("game").equals("Realm of the Mad God")) {
                        realm.addDrop(drop);
                    } else {
                        poe.addDrop(drop);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //title
        else if (title && checkForUrl(message) && !sender.toLowerCase().equals("nightbot")) {
            sendMessage(channel, getPageTitle(message));
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
    public void onPrivateMessage(String sender, String login, String hostname,
                                 String message) {
        scripts.onPrivateMessage(sender, login, hostname, message);
        System.out.println(message);
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
            this.sendMessage(newChannel, "HalsBot™ at your service.");
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

    private boolean checkForUrl(String message) {
        return message
                .matches(".*(([a-zA-Z0-9]{1,6})://)?([_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)+)(([_a-z\\d\\-\\\\\\./]+[_a-z\\d\\-\\\\/])+)*.*");
    }

    public static String getPageTitle(String url) {
        try {
            Matcher m = TwitchBot.url.matcher(url);
            m.find();
            String site = m.group();

            if (!site.contains("http://") && !site.contains("https://")) {
                if (site.contains("youtube")) {
                    site = "https://" + site;
                } else {
                    site = "http://" + site;
                }

            }

            URL u = new URL(site);
            URLConnection conn = u.openConnection();

            String title = "";

            ContentType contentType = getContentTypeHeader(conn);
            assert contentType != null;
            if (!contentType.contentType.equals("text/html"))
                return "";
            else {
                Charset charset = getCharset(contentType);
                if (charset == null)
                    charset = Charset.defaultCharset();

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, charset));
                int n, totalRead = 0;
                char[] buf = new char[1024];
                StringBuilder content = new StringBuilder();

                while (totalRead < 8192
                        && (n = reader.read(buf, 0, buf.length)) != -1) {
                    content.append(buf, 0, n);
                    totalRead += n;
                }
                reader.close();

                String cont = content.toString();
                if (cont.contains("<title></title>")) {
                    cont = cont.substring(cont.indexOf("<title></title>") + 15);
                }

                Matcher matcher = TITLE_TAG.matcher(cont);
                if (matcher.find()) {
                    title = matcher.group(1).replaceAll("[\\s<>]+", " ")
                            .trim();
                    title = StringEscapeUtils.unescapeHtml4(title);

                }
            }
            return title;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void echo(String message) {
        System.out.println("[Hals Bot]: " + getTimeStamp() + ": " + message);
    }

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    private static ContentType getContentTypeHeader(URLConnection conn) {
        int i = 0;
        boolean moreHeaders;
        do {
            String headerName = conn.getHeaderFieldKey(i);
            String headerValue = conn.getHeaderField(i);
            if (headerName != null && headerName.equals("Content-Type"))
                return new ContentType(headerValue);

            i++;
            moreHeaders = headerName != null || headerValue != null;
        } while (moreHeaders);

        return null;
    }

    private static Charset getCharset(ContentType contentType) {
        if (contentType != null && contentType.charsetName != null
                && Charset.isSupported(contentType.charsetName))
            return Charset.forName(contentType.charsetName);
        else
            return null;
    }

    private static final class ContentType {
        private static final Pattern CHARSET_HEADER = Pattern.compile(
                "charset=([-_a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE
                        | Pattern.DOTALL);

        private String contentType;
        private String charsetName;

        private ContentType(String headerValue) {
            if (headerValue == null)
                throw new IllegalArgumentException(
                        "ContentType must be constructed with a not-null headerValue");
            int n = headerValue.indexOf(";");
            if (n != -1) {
                contentType = headerValue.substring(0, n);
                Matcher matcher = CHARSET_HEADER.matcher(headerValue);
                if (matcher.find())
                    charsetName = matcher.group(1);
            } else
                contentType = headerValue;
        }
    }
}
