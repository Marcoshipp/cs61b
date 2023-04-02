public class ArrayDeque<T> implements Deque<T>{
    private int size;
    private int front;
    private int back;
    private T[] items;
    private int length;

    public ArrayDeque() {
        this.size = 0;
        this.front = this.size + 1;
        this.back = this.size + 2;
        this.length = 8;
        this.items = (T[]) new Object[this.length];
    }

    private int addOne(int x) {
        if (x == this.length - 1) {
            return 0;
        }
        return x + 1;
    }

    private int minusOne(int x) {
        if (x == 0) {
            return this.length - 1;
        }
        return x - 1;
    }

    private void resizeArr(int flag) {
        T[] a;
        if (flag == -1) {
            a = (T[]) new Object[this.length / 2];
        } else {
            a = (T[]) new Object[this.length * 2];
        }
        int arrIndex = 0;
        int p1 = addOne(this.front);
        int p2 = minusOne(this.back);
        // copying from items to new array
        while (p1 != p2) {
            a[arrIndex] = this.items[p1];
            p1 = addOne(p1);
            arrIndex++;
        }
        a[arrIndex] = this.items[p1];
        // resetting all pointers
        if (flag == -1) {
            this.length /= 2;
        } else {
            this.length *= 2;
        }
        this.items = a;
        this.front = this.length - 1;
        this.back = this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void printDeque() {
        int p1 = addOne(this.front);
        int p2 = minusOne(this.back);
        while (p1 != p2) {
            System.out.print(this.items[p1] + " ");
            p1 = addOne(p1);
        }
        System.out.println(this.items[p2]);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        int p = addOne(this.front);
        while (index != 0) {
            p = addOne(p);
            index--;
        }
        return this.items[p];
    }
    
    @Override
    public void addFirst(T item) {
        if (this.size == this.length) {
            resizeArr(1);
        }
        this.items[this.front] = item;
        this.front = minusOne(this.front);
        this.size++;
    }

    @Override
    public void addLast(T item) {
        if (this.size == this.length) {
            resizeArr(1);
        }
        this.items[this.back] = item;
        this.back = addOne(this.back);
        this.size++;
    }
    
    @Override
    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        this.front = addOne(this.front);
        T returnItem = this.items[this.front];
        this.items[this.front] = null;
        this.size--;
        if (this.length >= 16 && this.size / (double) this.length < 0.25) {
            resizeArr(-1);
        }
        return returnItem;
    }

    @Override
    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        this.back = minusOne(this.back);
        T returnItem = this.items[this.back];
        this.items[this.back] = null;
        this.size--;
        if (this.length >= 16 && this.size / (double) this.length < 0.25) {
            resizeArr(-1);
        }
        return returnItem;
    }
}
