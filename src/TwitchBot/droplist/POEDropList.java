package TwitchBot.droplist;

import java.io.*;
import java.util.LinkedList;

public class POEDropList {

    public LinkedList<Drop> drops;

    public POEDropList() {
        drops = new LinkedList<>();
        loadFromFile();
    }

    public String addDrop(String name) {
        Drop drop = new POEDrop(name);
        drops.addFirst(drop);
        writeToFile();
        return drop.name;
    }

    public String getDrops () {
        String dropss = "";
        int i = 0;
        for (Drop drop : drops) {
            if(i > 10) break;

            dropss = dropss + " " + drop.toString();
            i++;
        }

        return dropss.trim();
    }

    public Drop removeLast(){
        Drop removed = drops.removeFirst();
        writeToFile();
        return removed;
    }

    private void loadFromFile() {
        File file = new File("poe_droplist.txt");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader("poe_droplist.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    Drop drop = new POEDrop(line);
                    drops.add(drop);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void writeToFile() {
        try {
            FileWriter writer = new FileWriter("poe_droplist.txt");
            for (Drop drop : drops) {
                writer.write(drop.name);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
