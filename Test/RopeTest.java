import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class RopeTest {
    @Test
    public void testCreate() {
        String data = "firefly";
        Rope rope = new Rope(data);
        assertEquals(data, rope.data);

        String leftdata = "firefly";
        Rope left = new Rope(leftdata);

        String rightdata = "serenity";
        Rope right = new Rope(rightdata);

        rope = new Rope(left, right);
        assertEquals(left.weight, rope.weight);
    }

    static String testdata1 = "firefly";
    static String testdata2 = "serenity";
    static String testdata3 = "Whedon";

    /**
     * Test rope looks like this
     *        result
     *         |
     *      ---+-------------------
     *      |                     |
     *    ------------          Whedon
     *    |          |
     *  firefly    serenity
     */
    static Rope createTestRope() {
        Rope left = new Rope(testdata1);
        Rope right = new Rope(testdata2);
        Rope rope = new Rope(left, right);
        Rope moreRope = new Rope(testdata3);
        Rope result = new Rope(rope, moreRope);
        assertEquals(testdata1.length() + testdata2.length(), result.weight);

        return result;
    }

    @Test
    public void testIsLeaf() {
        Rope rope = createTestRope();
        assertEquals(false, rope.isLeaf());
        assertEquals(false, rope.left.isLeaf());
        assertEquals(true, rope.right.isLeaf());
    }

    @Test
    public void testTotalWeight() {
        Rope rope1 = new Rope(testdata1);
        assertEquals(testdata1.length(), rope1.totalWeight());

        Rope rope2 = new Rope(testdata2);
        Rope rope3 = new Rope(rope1, rope2);
        assertEquals(testdata1.length() + testdata2.length(), rope3.totalWeight());

        Rope rope = createTestRope();
        assertEquals(testdata1.length() + testdata2.length(), rope.weight);
        assertEquals( testdata1.length() + testdata2.length() + testdata3.length(), rope.totalWeight());
   }

    @Test
    public void testConcat() {
        Rope leftrope = createTestRope();
        String rightdata = "Josh";
        Rope rightrope = new Rope(rightdata);

        Rope rope = leftrope.concat(rightrope);
        assertEquals(leftrope, rope.left);
        assertEquals(rightrope, rope.right);
        assertEquals(leftrope.totalWeight(), rope.weight);

        // Test concat of null rope
        Rope result = rope.concat(null);
        assertEquals(rope, result);
    }

    @Test
    public void testCollect() {
        Rope rope = createTestRope();
        String testdata = testdata1 + testdata2 + testdata3;
        assertEquals(testdata, rope.collect());
    }

    @Test
    public void testCharAt() {
        Rope rope = createTestRope();
        String result = rope.collect();
        String testdata = testdata1 + testdata2 + testdata3;
        for (int i = 0; i < testdata.length(); i++) {
            assertEquals(testdata.charAt(i), rope.charAt(i));
        }
    }

    @Test
    public void testTail() {
        // Leaf tail
        {
            Rope rope = new Rope(testdata1);
            assertEquals(true, rope.isLeaf());

            // First tail is the whole word. No change.
            // Tail should return the unchanged Rope. 
            // Note: tail should not return a new Rope in this case.
            Rope tail1 = rope.tail(0);
            assertEquals(rope, tail1);

            // tail at the middle
            int index = 2;
            Rope tail2 = rope.tail(index);
            assertEquals(true, tail2.isLeaf());
            assertEquals(testdata1.length() - index, tail2.weight);
            assertEquals(testdata1.substring(index), tail2.data);
        }

        // General tail
        {
            Rope rope = createTestRope();
            String testdata = rope.collect();

            // First tail is the whole rope. No change
            int index1 = 0;
            Rope tail1 = rope.tail(index1);
            assertEquals(rope, tail1);

            // tail in the first leaf
            int index2 = 2;
            Rope tail2 = rope.tail(index2);
            assertEquals(testdata.substring(index2), tail2.collect());
            // Check structure, these nodes should be shared
            assertEquals(tail2.left.right, rope.left.right);
            assertEquals(tail2.right, rope.right);

            // tail by removing all of first leaf
            int index3 = testdata1.length();
            Rope tail3 = rope.tail(index3);
            assertEquals(testdata.substring(index3), tail3.collect());
            // Check structure, these nodes should be shared
            assertEquals(rope.right, tail2.right);

            // tail by removing all of first and second leaves
            int index4 = testdata1.length() + testdata2.length() + 2;
            Rope tail4 = rope.tail(index4);
            assertEquals(testdata.substring(index4), tail4.collect());
        }
    }

    @Test
    public void testHead() {
        {
            Rope rope1 = new Rope("a");
            Rope result1 = rope1.head(0);
            assertEquals(null, result1);
        }
        {
            Rope rope = createTestRope();
            String testdata = rope.collect();

            // head in first leaf - fully null
            int index1 = 0;
            Rope head1 = rope.head(index1);
            assertEquals(null, head1);

            // head in first leaf - non null
            int index2 = 2;
            Rope head2 = rope.head(index2);
            assertEquals(testdata1.substring(0, index2), head2.collect());
            assertTrue(head2.isLeaf());

            // head in second leaf - get back first leaf
            int index3 = testdata1.length();
            Rope head3 = rope.head(index3);
            assertEquals(testdata.substring(0, index3), head3.collect());
            assertTrue(head3.isLeaf());
            assertEquals(rope.left.left, head3);

            // head in second leaf - middle of second leaf
            int index4 = testdata1.length() + 2;
            Rope head4 = rope.head(index4);
            assertEquals(testdata.substring(0, index4), head4.collect());
            assertEquals(false, head4.isLeaf());
            assertEquals(rope.left.left, head4.left);

            // head in third leaf - middle of third leaf
            int index5 = testdata1.length() + testdata2.length() + 3;
            Rope head5 = rope.head(index5);
            assertEquals(testdata.substring(0, index5), head5.collect());
            assertEquals(rope.left.left, head5.left.left);
            assertEquals(rope.left.right, head5.left.right);
        }
    }


    @Test
    public void testSubrope() {
        Rope rope = createTestRope();
        String testdata = rope.collect();

        int start = 2;
        int end = testdata1.length() + testdata2.length() + 3;
        Rope sub = rope.subrope(start, end);
        System.out.println(sub.collect());
        assertEquals(testdata.substring(start, end), sub.collect());
    }

    /**
     * Helper function to delete a character from a string
     */
    String deleteAt(String s, int index) {
        return s.substring(0, index) + s.substring(index + 1);
    }

    @Test
    public void testDelete() {
        {
            Rope rope = new Rope("a");
            Rope result = rope.delete(0);
            assertEquals(null, result);

            Rope rope2 = new Rope(new Rope("a"), new Rope("b"));
            Rope result2 = rope2.delete(1);
            assertEquals("a", result2.data);
            assertTrue(result2.isLeaf());
        }

        {
            Rope rope = createTestRope();
            String testdata = rope.collect();

            // Delete in first leaf
            int index1 = 2;
            Rope delete1 = rope.delete(index1);
            assertEquals(deleteAt(testdata, index1), delete1.collect());
            // Check structure
            assertEquals(rope.left.right, delete1.left.right);
            assertEquals(rope.right, delete1.right);

            // Delete in second leaf
            int index2 = testdata1.length() + 1;
            Rope delete2 = rope.delete(index2);
            assertEquals(deleteAt(testdata, index2), delete2.collect());
            // Check structure
            assertEquals(rope.left.left, delete2.left.left);
            assertEquals(rope.right, delete2.right);

            // Delete in third leaf
            int index3 = testdata1.length() + testdata2.length();
            Rope delete3 = rope.delete(index3);
            assertEquals(deleteAt(testdata, index3), delete3.collect());
            // Check structure
            assertEquals(rope.left.left, delete3.left.left);
            assertEquals(rope.left.right, delete3.left.right);

            // Double delete spanning two leaves
            int index4 = testdata1.length() + testdata2.length();
            Rope delete4 = rope.delete(index3).delete(index4 - 1);
            assertEquals(deleteAt(deleteAt(testdata, index4), index4 - 1),
                delete4.collect());

            // Delete the first character
            int index5 = 0;
            Rope delete5 = rope.delete(index5);
            assertEquals(deleteAt(testdata, index5), delete5.collect());
        }
    }

    @Test
    public void testInsert() {
        Rope rope = createTestRope();
        String insertdata = "Zoom";
        Rope insertrope = new Rope(insertdata);
        Rope result = rope.insert(insertrope, testdata1.length());
        assertEquals(testdata1 + insertdata + testdata2 + testdata3,
            result.collect());
    }

    /**
     * Test tree reduction
     */

    /**
     * Create test tree that looks like this
     *
     *           ---------------------------
     *           |                         |
     *     --------------            ---------------
     *     |            |            |             |
     *   aardvark   antelope       aardvark        |
     *                                        ------------
     *                                        |           |
     *                                     giraffe       antelope
     */
    static String s1 = "aardvark";
    static String s2 = "antelope";
    static String s3 = "giraffe";
    static String s4 = "crocodile";

    private static Rope createTestReduceRope() {
        Rope rope = new Rope(
                new Rope(new Rope(s1),
                         new Rope(s2)),
                new Rope(new Rope(s1),
                         new Rope(new Rope(s3), new Rope(s2))));
        return rope;
    }


    @Test
    public void testReduce() {
        Rope rope = createTestReduceRope();

        // Check structure of result from reducing
        Rope result = rope.reduce();
        assertEquals(rope.collect(), result.collect());
        assertEquals(rope.left.left, result.right.left);
        assertEquals(rope.left.right, result.right.right.right);
    }
}
