package model;

import utils.Randomizer;

public class Task {



    private long id;
    private long gold;
    private String description;

    public Task(long id, String description, long gold) {
        this.id = id;
        this.gold = gold;
        this.description = description;
    }

    public Task(long gold, String description) {
        this.gold = gold;
        this.description = description;
    }

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public static Task createTask() {
        long gold = Randomizer.generateGoldAmount();
        String description = String.format("Very interesting task for %d gold", gold);
        return new Task(gold, description);
    }
}
