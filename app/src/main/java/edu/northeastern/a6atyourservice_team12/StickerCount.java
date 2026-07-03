package edu.northeastern.a6atyourservice_team12;

public class StickerCount {
    private String stringID;
    private int count;


    public StickerCount(String stringID, int count) {
        this.count = count;
        this.stringID = stringID;
    }

    public int getCount() {
        return count;
    }

    public String getStringID() {
        return stringID;
    }
}
