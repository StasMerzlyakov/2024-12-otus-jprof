package ru.otus.solid.model;

import java.util.Objects;

/**
 * Класс, представляющий собой банкноту конкретного номинала
 */
public record Banknote(int nominal) {

    public static final Banknote BANKNOTE_50 = new Banknote(50);
    public static final Banknote BANKNOTE_100 = new Banknote(100);
    public static final Banknote BANKNOTE_500 = new Banknote(500);
    public static final Banknote BANKNOTE_1000 = new Banknote(1000);
    public static final Banknote BANKNOTE_5000 = new Banknote(5000);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return nominal == banknote.nominal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominal);
    }

    @Override
    public String toString() {
        return "Banknote{" + "nominal=" + nominal + '}';
    }
}
