package TwitchBot.droplist;

public class Drop {

    public String name;
    public String wikiUrl;


    @Override
    public String toString() {
        return name + " (" + wikiUrl + ")";
    }

    static String toTitleCase(String string) {
        string = string.toLowerCase();
        String[] arr = string.split(" ");
        StringBuffer sb = new StringBuffer();

        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0)))
                    .append(anArr.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
