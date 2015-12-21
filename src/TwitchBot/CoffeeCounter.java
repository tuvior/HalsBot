package TwitchBot;

import java.io.*;

public class CoffeeCounter {
    private int coffee;

    public CoffeeCounter() {
        coffee = 0;
        loadCoffee();
    }

    public int getCoffee() {
        return coffee;
    }

    public void addCoffee() {
        coffee++;
        writeToFile();
    }

    private void writeToFile() {
        try {
            FileWriter writer = new FileWriter("coffee.txt");
            writer.write(coffee + "");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCoffee() {
        File file = new File("coffee.txt");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader("coffee.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = bufferedReader.readLine();

                reader.close();

                coffee = Integer.parseInt(line);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
