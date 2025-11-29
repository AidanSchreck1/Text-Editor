import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class DocumentTest {
    @Test
    public void testMakeWordRope() {
        Document document = new Document();

        /*
         * E.g a string "Fee fi" will result in a rope like this:
         *                    +
         *               ------------
         *               |           |
         *            -------       fi
         *            |     |
         *           Fee   (whitespace)
         */
        String testline = "Fee fi";
        Rope rope = document.makeWordRope(testline);
        assertEquals(testline, rope.collect());
        assertEquals("Fee", rope.left.left.data);
        assertEquals(" ", rope.left.right.data);
        assertEquals("fi", rope.right.data);

        rope = document.makeWordRope("");
        assertEquals(null, rope);
    }

    // Truncate a byte array
    byte[] truncateByteArray(byte[] b, int n) {
        byte[] out = new byte[n];
        for (int i = 0; i < n; i++) {
            out[i] = b[i];
        }
        return out;
    }

    // Create a test document with test data
    private static Document makeTestDocument(String testdata) {
        Document document = new Document();

        // Send data to the document
        try {
            PipedInputStream is = new PipedInputStream();
            PipedOutputStream os = new PipedOutputStream();
            os.connect(is);
            os.write(testdata.getBytes());
            os.close();
            Thread.sleep(1000);
            document.read(is);
        } catch (Exception e) {
            assert(false);
        }

        return document;
    }

    @Test
    public void testRead() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);

        assertEquals(data1, document.get(0).collect());
        assertEquals(null, document.get(1));
        assertEquals(data2, document.get(2).collect());
        assertEquals(null, document.get(3));
        assertEquals(4, document.rows());
    }


    @Test
    public void testWrite() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);


        // Read data back from the document
        try {
            PipedInputStream is = new PipedInputStream();
            PipedOutputStream os = new PipedOutputStream();
            os.connect(is);

            document.write(os);
            os.close();
            byte[] b = new byte[500];
            int n = is.read(b);

            byte[] out = truncateByteArray(b, n);
            String data = new String(out);
            assertEquals(testdata, data);
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    public void testCollect() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);
        String data = document.collect();
        assertEquals(testdata, data);
    }

    @Test
    public void testGetSet() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);
        assertEquals(data1, document.get(0).collect());
        assertEquals(null, document.get(1));
        assertEquals(data2, document.get(2).collect());
    }

    @Test
    public void testAdd() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);

        String data3 = "How now brown cow";
        document.add(new Rope(data3));
        assertEquals(5, document.rows());
        assertEquals(data3, document.get(4).collect());

        String data4 = "Take the red pill";
        document.add(1, new Rope(data4));
        assertEquals(6, document.rows());
        assertEquals(data4, document.get(1).collect());
    }

    @Test
    public void testDelete() {
        String data1 = "Fee fi fo fum";
        String data2 = "Lorem ipsum dolor";
        String testdata = data1 + "\n\n" + data2 + "\n";
        Document document = makeTestDocument(testdata);

        document.delete(1);
        assertEquals(3, document.rows());
        assertEquals(null, document.get(2));
        assertEquals(data1, document.get(0).collect());

        document.delete(0);
        assertEquals(data2, document.get(0).collect());
    }
}
