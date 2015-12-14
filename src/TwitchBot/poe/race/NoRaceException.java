package TwitchBot.poe.race;

public class NoRaceException extends Exception {
    public NoRaceException() {
        super("No race has been found");
    }

    public NoRaceException(String message)
    {
        super(message);
    }
}
