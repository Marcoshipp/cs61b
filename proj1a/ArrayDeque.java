public class ArrayDeque<T> {
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;
    private static final int RFACTOR = 2;
    private double R;

    public ArrayDeque() {
        this.size = 0;
        this.nextFirst = 0;
        this.nextLast = 1;
        this.R = 0;
        this.items = (T[]) new Object[8];
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(this.items, 0, a, 0, this.size);
        this.items = a;
        this.nextFirst = this.size;
        this.nextLast = this.nextFirst + 1;
    }

    private void downSize() {

    }

    public void addFirst(T item) {
        if (this.size == this.items.length) {
            resize(size * RFACTOR);
        }
        this.items[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = size;
        }
        else {
            nextFirst--;
        }
        size++;
        this.R = (double) this.size / this.items.length;
    }

    public void addLast(T item) {
        if (this.size == this.items.length) {
            resize(size * RFACTOR);
        }
        this.items[nextLast] = item;
        if (nextLast + 1 == size) {
            nextLast = 0;
        }
        else {
            nextLast++;
        }
        size++;
        this.R = (double) this.size / this.items.length;
    }

    // done
    public boolean isEmpty() {
        return this.size == 0;
    }

    // done
    public void printDeque() {
        for (T t : this.items) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    private int first(int nextFirst) {
        if (nextFirst + 1 == this.size) {
            return 0;
        }
        return nextFirst + 1;
    }

    private int last(int nextLast) {
        if (nextLast + 1 == this.size) {
            return 0;
        }
        return nextLast + 1;
    }

    // do this
    public T removeFirst() {
        this.nextFirst = first(nextFirst);
        T r = this.items[nextFirst];
        this.items[nextFirst] = null;
        this.R = (double) this.size / this.items.length;
        return r;
    }

    // do this
    public T removeLast() {
        this.R = (double) this.size / this.items.length;
        return null;
    }

    // done
    public T get(int index) {
        if (index >= this.size) {
            return null;
        }
        return this.items[index];
    }
    
}