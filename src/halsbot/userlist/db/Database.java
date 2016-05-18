package halsbot.userlist.db;

import halsbot.userlist.User;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.schema.SqlJetConflictAction;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private SqlJetDb db;

    public Database() {
        try {
            initDB();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
    }

    private void initDB() throws SqlJetException {
        File dbFile = new File("viewers.sqlite");
        boolean newTable = !dbFile.exists();
        db = SqlJetDb.open(dbFile, true);
        if (newTable) {
            db.getOptions().setAutovacuum(true);
            db.runWriteTransaction(db1 -> {
                db1.getOptions().setUserVersion(1);
                return true;
            });
            db.runWriteTransaction(db1 -> {
                db1.createTable("CREATE TABLE viewers " +
                        "(" +
                        "name TEXT NOT NULL UNIQUE PRIMARY KEY," +
                        "messages INT NOT NULL," +
                        "when BIGINT NOT NULL" +
                        ")");
                db1.createIndex("CREATE INDEX name_index ON viewers(name)");
                db1.createIndex("CREATE INDEX message_index ON viewers(messages DESC)");
                return true;
            });
        }
    }

    public void addMessage(String name, long time, boolean new_) {
        try {
            db.runWriteTransaction(db1 -> {

                ISqlJetTable table = db1.getTable("viewers");
                ISqlJetCursor name_index = table.lookup("name_index", name);
                int messages = 0;
                if (!name_index.eof()) {
                    messages = (int) name_index.getInteger("messages");
                }
                if (!new_) messages++;
                table.insertOr(SqlJetConflictAction.REPLACE, name, messages, time);
                return true;
            });
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
    }

    public String[] queryLastSeen(String name) {
        try {
            return (String[]) db.runReadTransaction(db1 -> {
                ISqlJetTable table = db.getTable("viewers");
                ISqlJetCursor name_index = table.lookup("name_index", name);
                if (!name_index.eof()) {
                    return new String[]{String.valueOf(name_index.getInteger("when")), name_index.getString("name")};
                } else {
                    return new String[0];
                }
            });
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public int getMessages(String name) {
        try {
            return (int) db.runReadTransaction(db1 -> {
                ISqlJetTable table = db.getTable("viewers");
                ISqlJetCursor name_index = table.lookup("name_index", name);
                if (!name_index.eof()) {
                    return name_index.getString("messages");
                } else {
                    return -1;
                }
            });
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public List<User> getTopViewers() {

        try {
            return (List<User>) db.runReadTransaction(db1 -> {
                List<User> top = new ArrayList<>();
                ISqlJetTable table = db.getTable("viewers");
                ISqlJetCursor msg_index = table.order("message_index");

                for (int i = 0; i < 10; i++) {
                    if (msg_index.eof()) break;

                    if (msg_index.getInteger("messages") <= 0) break;

                    top.add(new User(msg_index.getString("name"), (int) msg_index.getInteger("messages"), msg_index.getInteger("when")));
                    msg_index.next();
                }

                return top;
            });
        } catch (SqlJetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
