package halsbot.droplist;

import java.io.*;
import java.util.LinkedList;

public class RealmDropList {

    public LinkedList<Drop> drops;

    public RealmDropList() {
        drops = new LinkedList<>();
        loadFromFile();
    }

    public String addDrop(String name) {
        Drop drop = new RealmDrop(name);
        drops.addFirst(drop);
        writeToFile();
        return drop.name;
    }

    public Drop removeLast() {
        Drop removed = drops.removeFirst();
        writeToFile();
        return removed;
    }

    public String getDrops() {
        String dropss = "";
        int i = 0;
        for (Drop drop : drops) {
            if (i > 10) break;

            dropss = dropss + " | " + drop.toString();
            i++;
        }

        if (dropss.contains("|")) {
            dropss = dropss.substring(3);
        }

        return dropss.trim();
    }

    private void loadFromFile() {
        File file = new File("realm_droplist.txt");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader("realm_droplist.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    Drop drop = new RealmDrop(line);
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
            FileWriter writer = new FileWriter("realm_droplist.txt");
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
