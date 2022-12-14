package model;

public class User {
    private long id;
    private String name;

    private long gold;



    public User(long id, String name, long gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public User(String name, long gold) {
        this.name = name;
        this.gold = gold;
    }

    public User() {
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
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
}
