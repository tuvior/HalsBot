package TwitchBot.poe.ladder;

public class RankStatus {

    public String name;
    public int level;
    public int rank;
    public int classRank;
    public Character.Class charClass;

    public boolean notFound = false;

    public RankStatus(String name, int level, int rank, int classRank, Character.Class charClass) {
        this.name = name;
        this.rank = rank;
        this.classRank = classRank;
        this.charClass = charClass;
        this.level = level;

        if (rank == -1) notFound = true;
    }
}
