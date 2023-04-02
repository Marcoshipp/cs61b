import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("bigdicknigga"));
        assertTrue(palindrome.isPalindrome("adccda"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("b"));
        assertTrue(palindrome.isPalindrome("bb"));
        assertFalse(palindrome.isPalindrome("ba"));
        assertFalse(palindrome.isPalindrome("bbc"));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        CharacterComparator cc = new OffByOne();
        assertFalse(palindrome.isPalindrome("cat", cc));
        assertFalse(palindrome.isPalindrome("bigdicknigga", cc));
        assertFalse(palindrome.isPalindrome("adccda", cc));
        assertFalse(palindrome.isPalindrome("racecar", cc));
        assertTrue(palindrome.isPalindrome("b", cc));
        assertFalse(palindrome.isPalindrome("bb", cc));
        assertTrue(palindrome.isPalindrome("ba", cc));
        assertTrue(palindrome.isPalindrome("bbc", cc));
    }
}
