package halsbot.userlist;

import halsbot.userlist.db.Database;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserList {
    private Database db;

    public UserList() {
        db = new Database();
    }


    public int getMessages(String name) {
        return db.getMessages(name);
    }

    public void addMessage(String name) {
        db.addMessage(name, System.currentTimeMillis(), false);
    }

    public void addUser(String name) {
        db.addMessage(name, System.currentTimeMillis(), true);
    }

    public String getTopViewers() {
        List<User> top = db.getTopViewers();
        if (top != null && top.size() > 0) {
            StringBuilder res = new StringBuilder();
            for (User user : top) {
                res.append(user.getName()).append(" ").append(user.getMessages()).append(", ");
            }

            String result = res.toString();
            return result.substring(0, result.length() - 2);
        }

        return "";
    }
}
