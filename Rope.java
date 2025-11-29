import java.util.HashMap;

public final class Rope {
    /**
     * These instance variables are declared final to make the Rope
     * data structure immutable, ie. they can't be changed after they've
     * been assigned once in the constructor.
     */
    public final String data;
    public final Rope left;
    public final Rope right;
    public final int weight;
    public HashMap<String, Rope> map = new HashMap<>();

    /**
     * Create leaf Rope from string data
     * Data cannot be null and cannot be the empty string
     */
    public Rope(String data) {
        // TODO - your code here
        // replace these initializers

        //set this Ropes data to be the inputed data
        this.data = data;
        //set the left and right to be null because all ropes with data have children of null
        this.left = null;
        this.right = null;

        //set the weight of the rope to be the length of the data
        this.weight = data.length();
    }

    /**
     * Create Rope from left and right ropes.
     * The Rope weight is the weight of the left rope.
     * Left and right ropes cannot be null.
     */
    public Rope(Rope left, Rope right) {
        // TODO - your code here
        // replace these initializers

        //set data to null because this rope is not a leaf node
        this.data = null;

        //set left to left Rope
        this.left = left;
        this.right = right;

        //weight of non leaf node is the combined weight of the left subtree
        /**
         * if left.right is not null, then we set this ropes weight to be the left
         * weight and left.right weight added together.
         */
        if (left.right != null) {
            this.weight = (left.right.weight + left.weight);
        //if the left.right is null that means that we are at the beginning of our rope
        } else {
            this.weight = left.weight;
        }
    }

    /**
     * Print the rope out. Useful for debugging
     */
    public String toString() {
        String leftString = "";
        if (left != null) {
            leftString = left.toString();
        }
        String rightString = "";
        if (right != null) {
            rightString = right.toString();
        }
        return "(" + weight + "," + data + ",(" + leftString + "),"
            + "(" + rightString + ")";
    }

    /**
     * Return the total weight of the rope from
     * both the left and right ropes
     */
    public int totalWeight() {
        if (left == null) {
            return weight;
        }
        return left.totalWeight() + right.totalWeight();
    }

    /**
     * Concatenate this rope with another by creating a parent
     * rope node and adding this and other as children.
     * Concatenating a null rope has no effect.
     */
    public Rope concat(Rope other) {
        if (other == null) {
            return this;
        }
        return new Rope(this, other);
    }

    /**
     * Add a word by creating a rope and concatenating with this rope.
     * This is a convenience wrapper over the concat function.
     */
    public Rope add(String data) {
        Rope newRope = new Rope(data);
        return concat(newRope);
    }

    /**
     * Return true if this node is a leaf node, false otherwise
     */
    public boolean isLeaf() {
        if (data != null) {
            return true;
        }
        return false;
    }

    /**
     * Find the char at a particular node
     */
    public char charAt(int i) {
        //base case; if this rope is a leaf
        if (this.isLeaf()) {
            return data.charAt(i);
        }
        /**
         * if the index is more than the weight of this.weight
         * then check the right subtree with the new index number
         */
        if (i >= weight) {
            return right.charAt(i - weight);
        } else {
            return left.charAt(i);
        }
    }

    /**
     * Return this node as a string by accumulating the data
     * from left and right ropes.
     */
    public String collect() {
        
        if (this.isLeaf()) {
            return data;
        }
        return left.collect() + right.collect();

    }

    /**
     * Return the tail of this rope at index i to the end of the rope.
     * E.g If the rope is "firefly", then the tail of the rope at
     * index 2 would return the rope "refly"
     */
    public Rope tail(int i) {
        //base case; if the index is 0, we return this rope
        if (i == 0) {
            return this;
        }
        //base case; if i is not 0 and this rope is a leaf, then we return a new rope with the correct subtring
        if (this.isLeaf()) {
            return new Rope(data.substring(i));
        }
        //if i >= weight, then we must search the right tree for the index
        if (i >= weight) {
            return right.tail(i - weight);
        }
        //else, the index is in the left subtree, so return a new Rope with all of the right tree and a recursive call of tail() 
        else {
            //kinda hard to explain but if the index is in the left tree, that means that all data in the right subtree will be apart of the tail
            return new Rope(left.tail(i), right);
        }
       
    }

    /**
     * head of rope from index 0 up to but not including i.
     * E.g. If the rope is "firefly", then head of the rope at
     * index 2 would return the rope "fi"
     */
    public Rope head(int i) {
        //base case; if i == 0, then we dont want any data in this rope so return null
        if (i == 0) {
            return null;
        }
        //base case; else if this rope is a leaf node then return the data with the appropriate substring
        if (this.isLeaf()) {
            return new Rope(data.substring(0, i));
        }
        /**
         * If the value of i is more than or equal to the weight
         * of the tree, the left subtree is unchanged. Hence we 
         * return a new rope node which combines the unchanged 
         * left subtree with the results of truncating in the right
         * subtree. Note that if the truncation of the right 
         * subtree returns null, then we need only return the 
         * left subtree.
         */
        if (i >= weight) {
            if (i - weight == 0) {
                return left;
            }
            return new Rope(left, right.head(i - weight));
        }
        //else, the index is in the left subtree
        else {
            //search the left subtree for the index
            return left.head(i);
        }
    }

    /**
     * Return the sub rope from index start up to but not including end.
     * This is a wrapper around the head and tail functions.
     */

    public Rope subrope(int start, int end) {
        return this.tail(start).head(end - start);
    }

    /**
     * Delete a character from this rope at index i.
     * E.g If the rope is "firefly", then deleting the rope at
     * index 2 would return the rope "fiefly".
     * Deleting can result in a null rope if there is only one character
     * and it gets deleted.
     */
    public Rope delete(int i) {
        if (this.isLeaf()) {
            if (i == 0 && data.length() == 1) {
                return null;
            }
            if (i == 0) {
                return new Rope(data.substring(1));
            }
            return new Rope(data.substring(0, i) + data.substring(i+1));
        }
        if (i >= weight) {
            Rope newRope = new Rope(left, right.delete(i - weight));
            if (newRope.weight == 1) {
                return new Rope(newRope.left.collect());
            } else {
                return newRope;
            }
        } else {
            Rope newRope = new Rope(left.delete(i), right);
            if (newRope.weight == 1) {
                return new Rope(newRope.left.collect());
            } else {
                return newRope;
            }
        }
    }

    /**
     * Insert a rope at index i
     */
    public Rope insert(Rope other, int i) {
        if (this.isLeaf()) {
            if (i == 0) {
                return new Rope(other, new Rope(data.substring(i)));
            }
            //left substring
            Rope newLeft = new Rope(data.substring(0, i));
            //right substring
            Rope newRight = new Rope(data.substring(i));
            //return new Rope with the new right and new Rope including new left and inputed other
            return new Rope(new Rope(newLeft, other), newRight);
        }
        //find the parent
        if (i >= weight) {
            return new Rope(left, right.insert(other, i - weight));
        } else {
            return new Rope(left.insert(other, i), right);
        }
    }

    /**
     * Reduce the rope to its most space efficient form
     */
    public Rope reduce() {
        //if the left and right are leaf nodes, check them both
        if (left.isLeaf() && right.isLeaf()) {
            if (map.containsKey(left.data)) {
                return map.get(data);
            }
            if (map.containsKey(right.data)) {
                return map.get(data);
            }
            //if the data doesnt exist in the map, put it in the map
            map.put(data, this);
        } 
        //else, just check if the left node is a leaf node
        else if (left.isLeaf()) {
            if (map.containsKey(left.data)) {
                return map.get(data);
            }
            //if the data doesnt exist in the map, put it in the map
            map.put(data, this);
        }
        
        return new Rope(left.reduce(), right.reduce());
        //left off here
    }

    public static void main(String[] args) {
        String testdata1 = "firefly";
        String testdata2 = "serenity";
        String testdata3 = "Whedon";


        Rope left = new Rope(testdata1);
        Rope right = new Rope(testdata2);
        Rope rope = new Rope(left, right);
        Rope moreRope = new Rope(testdata3);
        Rope mainRope = new Rope(rope, moreRope);
        

        String insertdata = "Zoom";
        Rope insertrope = new Rope(insertdata);
        Rope result = mainRope.insert(insertrope, testdata1.length());

        System.out.println(result);
        
    }
}
