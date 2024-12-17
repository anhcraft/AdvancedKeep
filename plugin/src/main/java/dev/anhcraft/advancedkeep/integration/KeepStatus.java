package dev.anhcraft.advancedkeep.integration;

public class KeepStatus {
    private final boolean item;
    private final boolean exp;

    public KeepStatus(boolean item, boolean exp) {
        this.item = item;
        this.exp = exp;
    }

    public boolean item() {
        return item;
    }

    public boolean exp() {
        return exp;
    }
}
