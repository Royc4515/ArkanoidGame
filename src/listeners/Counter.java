package listeners;

/**
 * A simple counter utility class.
 */
public class Counter {
    private int value;

    /**
     * Constructs a new counter with initial value 0.
     */
    public Counter() {
        this.value = 0;
    }

    /**
     * Adds a given number to the current count.
     *
     * @param number the number to add
     */
    public void increase(int number) {
        this.value += number;
    }

    /**
     * Subtracts a given number from the current count.
     *
     * @param number the number to subtract
     */
    public void decrease(int number) {
        this.value -= number;
    }

    /**
     * @return the current count
     */
    public int getValue() {
        return this.value;
    }
}
