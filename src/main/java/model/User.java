package model;

public class User {
    private long id;
    private String name;

    private long gold;

    public User() {
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
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
