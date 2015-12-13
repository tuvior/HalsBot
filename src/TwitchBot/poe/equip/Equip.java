package TwitchBot.poe.equip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Equip {

    public HashMap<Integer, ArrayList<Gem>> gems;
    private String name;

    private Equip(String name, HashMap<Integer, ArrayList<Gem>> gems) {
        this.name = name;
        this.gems = gems;
    }

    public static Equip fromJSon(JSONObject obj) {
        HashMap<Integer, ArrayList<Gem>> builder = new HashMap<>();
        JSONArray sockets = obj.getJSONArray("sockets");
        JSONArray gems = obj.getJSONArray("socketedItems");
        int[] socketToGroup = new int[sockets.length()];
        int i = 0;
        for (Object object : sockets) {
            JSONObject socket = (JSONObject) object;
            int group = socket.getInt("group");
            socketToGroup[i] = group;
            if (!builder.containsKey(group)) {
                builder.put(group, new ArrayList<>());
            }
            i++;
        }

        for (Object object : gems) {
            JSONObject gem = (JSONObject) object;
            int group = socketToGroup[gem.getInt("socket")];
            builder.get(group).add(new Gem(gem.getString("typeLine"), group));
        }

        return new Equip(obj.getString("typeLine"), builder);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (gems.isEmpty()) {
            return "";
        }
        String result = "";
        for (ArrayList<Gem> gemGroup : gems.values()) {
            String group = "";
            for (Gem gem : gemGroup) {
                group = group + " + " + gem;
            }
            group = group.substring(3);
            result = result + " | " + group;
        }
        return result.substring(3);
    }


}
