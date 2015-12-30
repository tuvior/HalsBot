package halsbot.poe.rip;

public class Rip {
    private String info;
    private String video;

    public Rip(String info, String video) {
        this.info = info;
        this.video = video;
    }

    public String toString() {
        return info + " " + video;
    }

    public String toFile() {
        return info + "," + video;
    }
}
