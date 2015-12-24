package TwitchBot.poe.equip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static TwitchBot.webutil.WebUtil.readJsonFromUrl;

public class Equipment {

    private ArrayList<Equip> equips;

    private Equipment(ArrayList<Equip> equips) {
        this.equips = equips;
    }

    public static Equipment loadFromJSon(String name, String account) throws IOException {
        ArrayList<Equip> equipl = new ArrayList<>();
        JSONObject json = readJsonFromUrl("https://www.pathofexile.com/character-window/get-items?character=" + name + "&accountName=" + account);
        JSONArray items = json.getJSONArray("items");
        for (Object i : items) {
            JSONObject item = (JSONObject) i;
            if (item.getString("inventoryId").equals("Weapon2") || item.getString("inventoryId").equals("Offhand2")) {
                continue;
            }
            Equip equip = Equip.fromJSon(item);
            equipl.add(equip);
        }

        return new Equipment(equipl);
    }

    @Override
    public String toString() {
        String result = "";
        for (Equip equip : equips) {
            if (!equip.toString().equals("")) {
                result = result + " | " + equip.toString();
            }
        }

        return result.substring(3);
    }
}
