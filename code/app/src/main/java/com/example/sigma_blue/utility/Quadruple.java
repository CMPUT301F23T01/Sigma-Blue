package com.example.sigma_blue.utility;

/**
 * Quick object that holds four things. Better than indexing with ArrayList
 * @param <E1> the type of the first element
 * @param <E2> the type of the second element
 * @param <E3> the type of the third element
 * @param <E4> the type of the fourth element
 */
public class Quadruple<E1, E2, E3, E4> {
    private E1 first;
    private E2 second;
    private E3 third;
    private E4 fourth;

    public Quadruple(E1 first, E2 second, E3 third, E4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public E1 getFirst() {
        return first;
    }

    public E2 getSecond() {
        return second;
    }

    public E3 getThird() {
        return third;
    }

    public E4 getFourth() {
        return fourth;
    }
}
