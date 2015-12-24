package halsbot.droplist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RealmDrop extends Drop {

    public RealmDrop(String name) {
        this.name = toTitleCase(name);
        try {
            wikiUrl = "http://www.realmeye.com/wiki/" + URLEncoder.encode(this.name.toLowerCase().replace(" ", "-"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
