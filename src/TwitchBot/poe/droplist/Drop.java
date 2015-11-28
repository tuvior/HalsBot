package TwitchBot.poe.droplist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Drop {

    public String name;
    public String wikiUrl;

    public Drop(String name) {
        this.name = toTitleCase(name);
        try {
            wikiUrl = "http://pathofexile.gamepedia.com/" + URLEncoder.encode(this.name.replace(" ", "_"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name + " (" + wikiUrl + ")";
    }

    private static String toTitleCase(String string) {
        string = string.toLowerCase();
        String[] arr = string.split(" ");
        StringBuffer sb = new StringBuffer();

        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0)))
                    .append(anArr.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
