package halsbot;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {

    private final List<Invocable> scripts = new ArrayList<Invocable>();

    private TwitchBot bot;

    public ScriptManager(TwitchBot bot) {
        this.bot = bot;
        File folder = new File("scripts/");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                int lastIndex = name.lastIndexOf(".");
                if (lastIndex == -1) {
                    return false;
                }
                return name.substring(lastIndex).equalsIgnoreCase(".js");
            }

        };
        File[] files = folder.listFiles(filter);
        Object scriptEvent = new ScriptEvent(this.bot);
        for (File file : files) {
            ScriptEngine scriptEngine = new ScriptEngineManager()
                    .getEngineByName("JavaScript");
            try {
                scriptEngine.put("$", scriptEvent);
                scriptEngine.eval(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ScriptException e) {
            }
            this.scripts.add((Invocable) scriptEngine);
        }
    }

    public void reinit() {
        scripts.clear();
        File folder = new File("scripts/");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                int lastIndex = name.lastIndexOf(".");
                if (lastIndex == -1) {
                    return false;
                }
                return name.substring(lastIndex).equalsIgnoreCase(".js");
            }

        };
        File[] files = folder.listFiles(filter);
        Object scriptEvent = new ScriptEvent(this.bot);
        for (File file : files) {
            ScriptEngine scriptEngine = new ScriptEngineManager()
                    .getEngineByName("JavaScript");
            try {
                scriptEngine.put("$", scriptEvent);
                scriptEngine.eval(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ScriptException e) {
            }
            this.scripts.add((Invocable) scriptEngine);
        }
    }

    public void executeCommand(String command, String parameters, String sender, String channel) {
        ScriptEvent event = new ScriptEvent(this.bot);
        this.invoke("executeCommand", event, command, parameters, sender, channel);
    }

    public void onMessage(String channel, String sender, String login,
                          String hostname, String message) {
        ScriptEvent event = new ScriptEvent(this.bot);
        this.invoke("onMessage", event, channel, sender, login, hostname, message);
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        ScriptEvent event = new ScriptEvent(this.bot);
        this.invoke("onPrivateMessage", event, sender, login, hostname, message);
    }

    public void invoke(String method, Object... args) {
        for (Invocable script : this.scripts) {
            try {
                script.invokeFunction(method, args);
            } catch (NoSuchMethodException e) {
            } catch (ScriptException e) {
            }
        }
    }

    public void invoke(String method, ScriptEvent event, Object... args) {
        Object[] args0 = new Object[args.length + 1];
        args0[0] = event;
        for (int i = 0; i < args.length; i++) {
            args0[i + 1] = args[i];
        }
        for (Invocable script : this.scripts) {
            try {
                script.invokeFunction(method, args0);
            } catch (NoSuchMethodException e) {
                //
            } catch (ScriptException e) {
            }
        }
    }

}