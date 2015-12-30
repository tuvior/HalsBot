package halsbot.poe.rip;

import java.io.*;
import java.util.LinkedList;

public class RipList {
    private LinkedList<Rip> rips;

    public RipList() {
        rips = new LinkedList<>();
        loadFromFile();
    }

    public void addRip(String info, String video) {
        Rip rip = new Rip(info, video);
        rips.addFirst(rip);
        writeToFile();
    }

    public String getRips() {
        String ripList = "";
        int i = -3;
        for (Rip rip : rips) {
            i = i + rip.toString().length() + 3;
            if (i > 490) break;

            ripList = ripList + " | " + rip.toString();
        }

        if (ripList.contains("|")) {
            ripList = ripList.substring(3);
        }

        return ripList.trim();
    }

    private void loadFromFile() {
        File file = new File("rips.txt");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader("rips.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String rip_data [] = line.split(",");
                    Rip rip = new Rip(rip_data[0], rip_data[1]);
                    rips.add(rip);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void writeToFile() {
        try {
            FileWriter writer = new FileWriter("rips.txt");
            for (Rip rip : rips) {
                writer.write(rip.toFile());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
