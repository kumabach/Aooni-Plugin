package me.kuma.aooni.others;

import java.util.Objects;

public class Tuple<A, B, C, D> {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    public Tuple(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }
    public C getThird() { return third; }
    public D getFourth() { return fourth; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tuple<?, ?, ?, ?> that = (Tuple<?, ?, ?, ?>) obj;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second) &&
                Objects.equals(third, that.third) &&
                Objects.equals(fourth, that.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
    }
}
