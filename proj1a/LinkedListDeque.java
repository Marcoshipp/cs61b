public class LinkedListDeque<T> {
    private class DLList {
        private IntNode sentinal;
        private int size;
    
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
    
        public DLList() {
            this.sentinal = new IntNode(this.sentinal, null, this.sentinal);
            this.sentinal.next = this.sentinal;
            this.sentinal.prev = this.sentinal.next;
            this.size = 0;
        }
    
        public void insertFront(T x) {
            IntNode p = new IntNode(this.sentinal, x, this.sentinal.next);
            this.sentinal.next.prev = p;
            this.sentinal.next = p;
            this.size++;
        }
    
        public void insertBack(T x) {
            IntNode p = new IntNode(this.sentinal.prev, x, this.sentinal);
            this.sentinal.prev.next = p;
            this.sentinal.prev = p;
            this.size++;
        }
    
        public T get(int index) {
            IntNode p = this.sentinal.next;
            while (p != this.sentinal && index != 0) {
                p = p.next;
                index--;
            }
            return p.item;
        }

        private T getRecursive(int index, IntNode p) {
            if (index == 0) {
                if (p.prev != this.sentinal) {
                    return null;
                }
                return p.next.item;
            }
            return getRecursive(index - 1, p.next);
        }

        public T getRecursive(int index) {
            return getRecursive(index, this.sentinal);
        }
        
        public T removeFront() {
            if (this.sentinal.next == this.sentinal) {
                return null;
            }
            IntNode r = this.sentinal.next;
            this.sentinal.next.next.prev = this.sentinal;
            this.sentinal.next = this.sentinal.next.next;
            this.size--;
            return r.item;
        }

        public T removeBack() {
            if (this.sentinal.prev == this.sentinal) {
                return null;
            }
            IntNode r = this.sentinal.prev;
            this.sentinal.prev.prev.next = this.sentinal;
            this.sentinal.prev = this.sentinal.prev.prev;
            this.size--;
            return r.item;
        }

        public void printList() {
            IntNode p = this.sentinal.next;
            while (p != this.sentinal) {
                if (p.next == this.sentinal) {
                    System.out.print(p.item + "\n");
                }
                else {
                    System.out.print(p.item + " ");
                }
                p = p.next;
            }
        }
    
        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return size() == 0;
        }
    }

    private DLList deque;

    public LinkedListDeque() {
        this.deque = new DLList();
    }

    public void addFirst(T item) {
        this.deque.insertFront(item);
    }

    public void addLast(T item) {
        this.deque.insertBack(item);
    }

    public boolean isEmpty() {
        return this.deque.isEmpty();
    }

    public int size() {
        return this.deque.size();
    }

    public void printDeque() {
        this.deque.printList();
    }

    public T removeFirst() {
        return this.deque.removeFront();
    }

    public T removeLast() {
        return this.deque.removeBack();
    }

    public T get(int index) {
        return this.deque.get(index);
    }

    public T getRecursive(int index) {
        return this.deque.getRecursive(index);
    }
}
