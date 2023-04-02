import org.junit.Test;

import static org.junit.Assert.*;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        StudentArrayDeque<Integer> solutionDeque = new StudentArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            int randNum = StdRandom.uniform(10, 90);
            if (StdRandom.uniform() < 0.5) {
                studentDeque.addFirst(randNum);
                solutionDeque.addFirst(randNum);
                assertEquals("addFirst()", solutionDeque.removeFirst(), studentDeque.removeFirst());
                studentDeque.addLast(randNum);
                solutionDeque.addLast(randNum);
            }
            else {
                studentDeque.addLast(randNum);
                solutionDeque.addLast(randNum);
                assertEquals("addLast()", solutionDeque.removeLast(), studentDeque.removeLast());
                studentDeque.addLast(randNum);
                solutionDeque.addLast(randNum);
            }
        }
        // check if both deque's size are equal after randomized testing
        assertEquals("size()", solutionDeque.size(), studentDeque.size());
        while (solutionDeque.size() != 0) {
            if (StdRandom.uniform() < 0.5) {
                Integer given = studentDeque.removeFirst();
                Integer expected = solutionDeque.removeFirst();
                assertEquals("removeFirst()", expected, given);
            }
            else {
                Integer given = studentDeque.removeLast();
                Integer expected = solutionDeque.removeLast();
                assertEquals("removeLast()", expected, given);
            }
        }

    }
}
