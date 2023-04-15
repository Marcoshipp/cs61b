// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import java.util.Iterator;

// TODO: Make sure to make this class and all of its methods public
// TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>{
    // index for the next dequeue or peek
    private int first;
    // index for the next dequeue or peek
    private int last;
    // Array for storing the buffer data.
    private T[] rb;


    // done setting up the fields;
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
        this.rb = (T[]) new Object[this.capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    private int addOne(int x) {
        if (x == capacity - 1) {
            return 0;
        }
        return x + 1;
    }

    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        this.rb[this.last] = x;
        this.last = addOne(this.last);
        this.fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T returnItem = this.rb[this.first];
        this.first = addOne(this.first);
        this.fillCount--;
        return returnItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return this.rb[this.first];
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
    private class RingBufferIterator implements Iterator<T> {
        public int pos;
        private boolean passed;

        public RingBufferIterator() {
            this.pos = first;
            this.passed = false;
        }
        public T next() {
            T item = rb[this.pos];
            int last = this.pos;
            this.pos = addOne(this.pos);
            if (this.pos != last + 1) {
                this.passed = true;
            }
            return item;
        }

        public boolean hasNext() {
            if (isEmpty()) {
                return false;
            }
            if (this.pos == last) {
                return !this.passed;
            }
            return true;
        }
    }

    public Iterator<T> iterator() {
        return new RingBufferIterator();
    }
}
