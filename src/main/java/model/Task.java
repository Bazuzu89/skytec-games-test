package model;

public class Task {
    private long gold;
    private String description;

    public Task(long gold, String description) {
        this.gold = gold;
        this.description = description;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
