package Logic;

public enum Emojis {
    HAPPY_FACE(":)");

    private final String str;

    Emojis(String s) {
        this.str=s;
    }
    public String getStr(){
        return str;
    }
}
