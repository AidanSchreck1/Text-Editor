import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


/**
 * The Document represents text as an array list of ropes
 */
public class Document {
    /**
     * The file is represented by an array list of ropes
     */
    private ArrayList<Rope> ropes;

    /**
     * Create a document represented by an array list of ropes
     */
    public Document() {
        // TODO - your code here
    }

    /**
     * Read a file into the document
     */
    public boolean read(InputStream is) {
        // TODO - your code here
        return true;
    }

    /**
     * Create a rope made up of words from a string by concatenating
     * words from the string. Return null on an empty string.
     *
     * E.g a string "Fee fi" will result in a rope like this:
     *                    +
     *               ------------
     *               |           |
     *            -------       fi
     *            |     |
     *           Fee   (whitespace)
     *
     */
    public Rope makeWordRope(String line) {
        // TODO - your code here
        return null;
    }

    /**
     * Helpful function to print a document
     */
    public void print() {
        for (int i = 0; i < ropes.size(); i++) {
            String line = "";
            Rope rope = ropes.get(i);
            if (rope != null) {
                line = rope.collect();
            }
            System.out.println((i+1) + ":" + line);
        }
    }

    /**
     * Output the document to a file
     */
    public boolean write(OutputStream os) {
        // TODO - your code here
        return false;
    }

    /**
     * Collect the document as one big string
     */
    public String collect() {
        // TODO - your code here
        return "";
    }

    /**
     * Get the total number of lines
     */
    public int rows() {
        // TODO - your code here
        return 0;
    }

    /**
     * Get the rope at line i
     */
    public Rope get(int i) {
        // TODO - your code here
        return null;
    }

    /**
     * Set the rope at line i
     */
    public void set(int i, Rope rope) {
        // TODO - your code here
    }

    /**
     * Grow the ropes by 1
     */
    public void add(Rope rope) {
        // TODO - your code here
    }

    /**
     * Delete row i in the document
     */
    public void delete(int i) {
        // TODO - your code here
    }

    /**
     * Insert a rope at line i
     */
    public void add(int i, Rope rope) {
        // TODO - your code here
    }
}
