package model;

public class Clan {
    private long id;
    private String name;
    private long gold;

    public Clan(long id, String name, long gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public Clan(String name, long gold) {
        this.name = name;
        this.gold = gold;
    }

    public Clan() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }
}
