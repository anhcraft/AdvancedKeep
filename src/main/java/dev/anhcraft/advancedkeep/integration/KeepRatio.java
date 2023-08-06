package dev.anhcraft.advancedkeep.integration;

public class KeepRatio {
    private final double item;
    private final double exp;

    public KeepRatio(double item, double exp) {
        this.item = item;
        this.exp = exp;
    }

    public double item() {
        return item;
    }

    public double exp() {
        return exp;
    }
}
