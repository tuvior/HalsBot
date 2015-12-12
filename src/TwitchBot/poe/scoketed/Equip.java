package TwitchBot.poe.scoketed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Equip {

    private String name;
    public HashMap<Integer, ArrayList<Gem>> gems;

    private Equip(String name, HashMap<Integer, ArrayList<Gem>> gems) {
        this.name = name;
        this.gems = gems;
    }

    public static Equip fromJSon(JSONObject obj) {
        HashMap<Integer, ArrayList<Gem>> builder = new HashMap<>();
        JSONArray sockets = obj.getJSONArray("sockets");
        JSONArray gems = obj.getJSONArray("socketedItems");
        int[] idsocket = new int[sockets.length()];
        int i = 0;
        for (Object socket : sockets) {
            JSONObject temp = (JSONObject) socket;
            idsocket[i] = temp.getInt("group");
            builder.put(temp.getInt("group"), new ArrayList<>());
            i++;
        }

        for (Object gem : gems) {
            JSONObject temp = (JSONObject) gem;
            int group = idsocket[temp.getInt("socket")];
            builder.get(group).add(new Gem(temp.getString("typeLine"), group));
        }

        return new Equip(obj.getString("typeLine"), builder);
    }

    @Override
    public String toString() {
        if(gems.isEmpty()) return "";
        String result = "";
        for (ArrayList<Gem> gem : gems.values()) {
            String line = "";
            for(Gem g : gem) {
                line = line + " + " + g;
            }
            line = line.substring(3);
            result = result + " | " + line;
        }
        return result.substring(3);
    }


}
