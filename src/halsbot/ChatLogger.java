package halsbot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatLogger {
    private BufferedWriter logger;

    public ChatLogger(String fileName) {
        File folder = new File("logs/");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File tempFile = new File("logs/" + fileName + ".txt");
        try {
            logger = new BufferedWriter(new FileWriter(tempFile, true));
        } catch (IOException e) {
            System.err.println("Error while initializing log file");
        }
    }

    public void log(String name, String message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String log = timeStamp + " <" + name + "> " + message;
        try {
            logger.write(log + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
