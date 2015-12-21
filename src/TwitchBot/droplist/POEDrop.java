package TwitchBot.droplist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class POEDrop extends Drop {

    public POEDrop(String name) {
        this.name = toTitleCase(name);
        this.name = this.name.replace(" And ", " and ").replace(" Of ", " of ").replace(" At ", " at ").replace(" To ", " to ").replace(" The ", " the ");
        try {
            wikiUrl = "http://pathofexile.gamepedia.com/" + URLEncoder.encode(this.name.replace(" ", "_"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
