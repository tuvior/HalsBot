package halsbot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatLogger {
    private final BufferedWriter logger;

    public ChatLogger(String fileName) throws IOException {
        File folder = new File("logs/");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        final File tempFile = new File("logs/" + fileName + ".txt");
        logger = new BufferedWriter(new FileWriter(tempFile, true));
    }

    public void log(String name, String message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String log = timeStamp + " <" + name + "> " + message;
        synchronized (logger) {
            try {
                logger.write(log + System.getProperty("line.separator"));
                logger.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        logger.close();
    }
}
