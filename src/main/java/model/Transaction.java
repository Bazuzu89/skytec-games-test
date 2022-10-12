package model;

import utils.TransactionActor;

public class Transaction {

    private long id;
    private long clanId;
    private long actorId;
    private long gold;
    private TransactionActor actor;

    private String description;


    public Transaction(long clanId, long actorId, TransactionActor actor, long gold) {
        this.clanId = clanId;
        this.actorId = actorId;
        this.actor = actor;
        this.gold = gold;
    }



    public Transaction(long id, long clanId, long actorId, TransactionActor actor, long gold) {
        this.id = id;
        this.clanId = clanId;
        this.actorId = actorId;
        this.actor = actor;
        this.gold = gold;
    }
    public Transaction() {
    }



    public long getClanId() {
        return clanId;
    }

    public void setClanId(long clanId) {
        this.clanId = clanId;
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public TransactionActor getActor() {
        return actor;
    }

    public void setActor(TransactionActor actor) {
        this.actor = actor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
