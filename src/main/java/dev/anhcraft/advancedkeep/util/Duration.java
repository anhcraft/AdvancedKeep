package dev.anhcraft.advancedkeep.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Duration implements Comparable<Duration> {
    private final long begin;
    private final long end;

    public Duration(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration = (Duration) o;
        return begin == duration.begin && end == duration.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end);
    }

    @Override
    public int compareTo(@NotNull Duration o) {
        int i = Long.compare(this.begin, o.begin);
        return i == 0 ? Long.compare(this.end, o.end) : i;
    }
}
