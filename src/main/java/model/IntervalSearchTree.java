package model;

/**
 * Created by Sougata on 4/30/2016.
 */
public class IntervalSearchTree {

    private IntervalNode root;

    private class IntervalNode {
        IntervalNode left;
        int start;
        int end;
        int maxEnd;
        IntervalNode right;

        public IntervalNode(IntervalNode left, int start, int end, int maxEnd, IntervalNode right) {
            this.left = left;
            this.start = start;
            this.end = end;
            this.maxEnd = maxEnd;
            this.right = right;
        }
    }

    /**
     * Adds an interval to the the calendar
     *
     * @param start the start of interval
     * @param end   the end of the interval.
     */
    public void add (int start, int end) {
        if (start >= end) throw new IllegalArgumentException("The end " + end + " should be greater than start " + start);

        IntervalNode inode = root;
        while (inode != null) {
            inode.maxEnd = (end > inode.maxEnd) ? end : inode.maxEnd;
            if (start < inode.start) {
                if (inode.left == null) {
                    inode.left = new IntervalNode(null, start, end, end, null);
                    return;
                }
                inode = inode.left;
            } else {
                if (inode.right == null) {
                    inode.right = new IntervalNode(null, start, end, end, null);
                    return;
                }
                inode = inode.right;
            }
        }
        root =  new IntervalNode(null, start, end, end, null);
    }

    /**
     * Tests if the input interval overlaps with the existing intervals.
     *
     * Rules:
     * 1.  If interval intersects return true. obvious.
     * 2.  if (leftsubtree == null || leftsubtree.max <=  low) go right
     * 3.  else go left
     *
     * @param start     the start of the interval
     * @param end       the end of the interval
     * return           true if overlap, else false.
     */
    public boolean overlap(int start, int end) {
        if (start >= end) throw new IllegalArgumentException("The end " + end + " should be greater than start " + start);

        IntervalNode intervalNode = root;

        while (intervalNode != null) {
            if (intersection(start, end, intervalNode.start, intervalNode.end)) return true;

            if (goLeft(start, end, intervalNode.left)) {
                intervalNode = intervalNode.left;
            } else {
                intervalNode = intervalNode.right;
            }
        }
        return false;
    }

    /**
     * Returns if there is an intersection in the two intervals
     * Two intervals such that one of the points coincide:
     * eg: [10, 20] and [20, 40] are NOT considered as intersecting.
     */
    private boolean intersection (int start, int end, int intervalStart, int intervalEnd) {
        return start < intervalEnd && end > intervalStart;
    }

    private boolean goLeft(int start, int end, IntervalNode intervalLeftSubtree) {
        return intervalLeftSubtree != null && intervalLeftSubtree.maxEnd > start;
    }

    public static void main(String[] args) {
        IntervalSearchTree intervalSearchTree = new IntervalSearchTree();
        intervalSearchTree.add(17, 19);
        intervalSearchTree.add(5, 8);
        intervalSearchTree.add(21, 24);
        intervalSearchTree.add(5, 8);
        intervalSearchTree.add(4, 8);
        intervalSearchTree.add(15, 18);
        intervalSearchTree.add(7, 10);
        intervalSearchTree.add(16, 22);

        System.out.println("Expected true,   Actual: " + intervalSearchTree.overlap(23, 25));
        System.out.println("Expected false,  Actual: " + intervalSearchTree.overlap(12, 14));
        System.out.println("Expected true,   Actual: " + intervalSearchTree.overlap(21, 23));
        // testing adjoint
        System.out.println("Expected false,  Actual: " + intervalSearchTree.overlap(10, 15));
        System.out.println("Expected false,  Actual: " + intervalSearchTree.overlap(10, 14));
        System.out.println("Expected false,  Actual: " + intervalSearchTree.overlap(11, 15));
    }
}