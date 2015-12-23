package TwitchBot.poe.race;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import static TwitchBot.jsonutil.WebUtil.readJsonFromUrlArray;

public class Race {

    private Date start;
    private Date end;
    private Date register;
    private String name;
    private String description;
    private String url;
    private HashSet<RaceModifier> mods;

    public Race(String name, String description, String url, Date start, Date register, Date end, HashSet<RaceModifier> mods) {
        this.start = start;
        this.end = end;
        this.register = register;
        this.name = name;
        this.description = description;
        this.mods = mods;
        this.url = url;
    }

    public static Race getCurrentRace() throws IOException, ParseException, NoRaceException {
        JSONArray ladder = readJsonFromUrlArray("http://api.pathofexile.com/leagues?type=event");
        if (ladder.length() == 0) {
            throw new NoRaceException();
        }
        JSONObject race = ladder.getJSONObject(0);
        return fromJson(race);
    }

    private static Race fromJson(JSONObject obj) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMAN);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date end_date = format.parse(obj.getString("endAt"));
        Date start_date = format.parse(obj.getString("startAt"));
        Date register_date = format.parse(obj.getString("registerAt"));

        return new Race(obj.getString("id"), obj.getString("description"), obj.getString("url"), start_date, register_date, end_date, parseMods(obj.getString("id")));
    }

    private static HashSet<RaceModifier> parseMods(String name) {
        HashSet<RaceModifier> mods = new HashSet<>();
        if (name.toLowerCase().contains("ancestral")) {
            mods.add(RaceModifier.Ancestral);
        }
        if (name.toLowerCase().contains("blood grip")) {
            mods.add(RaceModifier.Blood_Grip);
        }
        if (name.toLowerCase().contains("blood magic")) {
            mods.add(RaceModifier.Blood_Magic);
        }
        if (name.toLowerCase().contains("kill")) {
            mods.add(RaceModifier.Boss_Kill);
        }
        if (name.toLowerCase().contains("brutal")) {
            mods.add(RaceModifier.Brutal);
        }
        if (name.toLowerCase().contains("burst")) {
            mods.add(RaceModifier.Burst);
        }
        if (name.toLowerCase().contains("cut-throat")) {
            mods.add(RaceModifier.Cut_throat);
        }
        if (name.toLowerCase().contains("emberwake")) {
            mods.add(RaceModifier.Emberwake);
        }
        if (name.toLowerCase().contains("eternal torment")) {
            mods.add(RaceModifier.Eternal_Torment);
        }
        if (name.toLowerCase().contains("exiles everywhere")) {
            mods.add(RaceModifier.Exiles_Everywhere);
        }
        if (name.toLowerCase().contains("famine")) {
            mods.add(RaceModifier.Famine);
        }
        if (name.toLowerCase().contains("fixed seed")) {
            mods.add(RaceModifier.Fixed_Seed);
        }
        if (name.toLowerCase().contains("fracturing")) {
            mods.add(RaceModifier.Fracturing);
        }
        if (name.toLowerCase().contains("headhunter")) {
            mods.add(RaceModifier.Headhunter);
        }
        if (name.toLowerCase().contains("immolation")) {
            mods.add(RaceModifier.Immolation);
        }
        if (name.toLowerCase().contains("inferno")) {
            mods.add(RaceModifier.Inferno);
        }
        if (name.toLowerCase().contains("lethal")) {
            mods.add(RaceModifier.Lethal);
        }
        if (name.toLowerCase().contains("multiple projectile")) {
            mods.add(RaceModifier.Multiple_Projectile);
        }
        if (name.toLowerCase().contains("no projectiles")) {
            mods.add(RaceModifier.No_Projectiles);
        }
        if (name.toLowerCase().contains("rogue")) {
            mods.add(RaceModifier.Rogue);
        }
        if (name.toLowerCase().contains("solo")) {
            mods.add(RaceModifier.Solo);
        }
        if (name.toLowerCase().contains("soulthirst")) {
            mods.add(RaceModifier.Soulthirst);
        }
        if (name.toLowerCase().contains("turbo")) {
            mods.add(RaceModifier.Turbo);
        }
        if (name.toLowerCase().contains("unwavering")) {
            mods.add(RaceModifier.Unwavering);
        }
        if (name.toLowerCase().contains("unwavering")) {
            mods.add(RaceModifier.Unwavering);
        }
        if (name.toLowerCase().contains("unwavering")) {
            mods.add(RaceModifier.Unwavering);
        }

        if (name.toLowerCase().contains("elblamt")) {
            mods.add(RaceModifier.Blood_Magic);
            mods.add(RaceModifier.Lethal);
            mods.add(RaceModifier.Ancestral);
            mods.add(RaceModifier.Multiple_Projectile);
            mods.add(RaceModifier.Turbo);
        } else if (name.toLowerCase().contains("fblamt")) {
            mods.add(RaceModifier.Blood_Magic);
            mods.add(RaceModifier.Lethal);
            mods.add(RaceModifier.Ancestral);
            mods.add(RaceModifier.Multiple_Projectile);
            mods.add(RaceModifier.Turbo);
            mods.add(RaceModifier.Fracturing);
        } else if (name.toLowerCase().contains("blamt")) {
            mods.add(RaceModifier.Blood_Magic);
            mods.add(RaceModifier.Lethal);
            mods.add(RaceModifier.Ancestral);
            mods.add(RaceModifier.Multiple_Projectile);
            mods.add(RaceModifier.Turbo);
        }
        return mods;
    }

    public boolean hasStarted() {
        return start.before(new Date());
    }

    public boolean isRunning() {
        return start.before(new Date()) && end.after(new Date());
    }

    public Date getEnd() {
        return end;
    }

    public Date getStart() {
        return start;
    }

    public Date getRegisterDate() {
        return register;
    }

    public String getDescription() {
        return description;
    }

    public HashSet<RaceModifier> getMods() {
        return mods;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long timeLeft() {
        return end.getTime() - new Date().getTime();
    }

    public long timeToStart() {
        return start.getTime() - new Date().getTime();
    }
}
