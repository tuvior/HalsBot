package halsbot.userlist;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserList {

    private Map<String, User> users;
    private String path;

    public UserList(String path) {
        this.path = path;
        users = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(path);
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(path);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                bufferedReader.readLine();

                while ((line = bufferedReader.readLine()) != null) {
                    String[] user_data = line.split(",");
                    User user = new User(user_data[0], Integer.parseInt(user_data[1]));
                    users.put(user_data[0], user);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void writeToFile() {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write("Name,Messages");
            writer.write("\n");
            for (User user : users.values()) {
                writer.write(user.toString());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMessages(String name) {
        if (users.containsKey(name)) {
            return users.get(name).getMessages();
        } else {
            return 0;
        }
    }

    public void updateUser(String name) {
        if (users.containsKey(name)) {
            User temp = users.get(name);
            temp.addMessage();
            users.put(name, temp);
        } else {
            User user = new User(name, 1);
            users.put(name, user);
        }

        writeToFile();
    }

    public void addUser(String name) {
        if (!users.containsKey(name)) {
            User user = new User(name, 0);
            users.put(name, user);
            writeToFile();
        }
    }
}
