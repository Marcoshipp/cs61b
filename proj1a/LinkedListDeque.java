public class LinkedListDeque<T> {
    private class IntNode {
        public IntNode prev;
        public T item;
        public IntNode next;

        public IntNode(IntNode p, T x, IntNode n) {
            this.prev = p;
            this.item = x;
            this.next = n;
        }
    }
    
    private IntNode sentinal;
    private int size;
    public LinkedListDeque() {
        this.sentinal = new IntNode(this.sentinal, null, this.sentinal);
        this.sentinal.next = this.sentinal;
        this.sentinal.prev = this.sentinal.next;
        this.size = 0;
    }

    public void addFirst(T item) {
        IntNode p = new IntNode(this.sentinal, item, this.sentinal.next);
        this.sentinal.next.prev = p;
        this.sentinal.next = p;
        this.size++;
    }

    public void addLast(T item) {
        IntNode p = new IntNode(this.sentinal.prev, item, this.sentinal);
        this.sentinal.prev.next = p;
        this.sentinal.prev = p;
        this.size++;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        IntNode p = this.sentinal.next;
        while (p != this.sentinal) {
            if (p.next == this.sentinal) {
                System.out.print(p.item + "\n");
            } else {
                System.out.print(p.item + " ");
            }
            p = p.next;
        }
    }

    public T removeFirst() {
        if (this.sentinal.next == this.sentinal) {
            return null;
        }
        IntNode r = this.sentinal.next;
        this.sentinal.next.next.prev = this.sentinal;
        this.sentinal.next = this.sentinal.next.next;
        this.size--;
        return r.item;
    }
    
    public T removeLast() {
        if (this.sentinal.prev == this.sentinal) {
            return null;
        }
        IntNode r = this.sentinal.prev;
        this.sentinal.prev.prev.next = this.sentinal;
        this.sentinal.prev = this.sentinal.prev.prev;
        this.size--;
        return r.item;
    }

    public T get(int index) {
        IntNode p = this.sentinal.next;
        while (p != this.sentinal && index != 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    private T getRecursive(int index, IntNode n) {
        if (index == 0) {
            return n.item;
        }
        return getRecursive(index - 1, n.next);
    }

    public T getRecursive(int index) {
        if (index >= this.size || index < 0) {
            return null;
        }
        return getRecursive(index, this.sentinal.next);
    }
}
