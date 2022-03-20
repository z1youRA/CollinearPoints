import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class FastCollinearPoints {
    private LineSegment[] lines = new LineSegment[100];
    private LineSegment[] linesResult;
    private int lineCount = 0;

    public FastCollinearPoints(
            Point[] points) {   // finds all line segments containing 4 or more points
        for (int i = 0; i < points.length; i++) {   // corner cases check
            if (points[i] == null) {
                throw new IllegalArgumentException("Input Invalid: null points are given");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Input Invalid: Repeated points are given");
                }
            }
        }
        Point[] aux = new Point[points.length];
        Point[] temp = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            temp[i] = points[i];
        }
        for (int i = 0; i < temp.length;
             i++) { // traverse points and sort points according to their slope to temp[i]
            sort(temp[i].slopeOrder(), points, aux, 0, points.length - 1);
            for (Point p : points) {
                System.out.println(p.getX());
                System.out.println(p.getY()); // for debug
            }
            System.out.println("++++++++++++++++++++++++++++");
            for (int j = 0; j < points.length; j++) {
                double tempSlop = points[j].slopeTo(temp[i]);
                int count = 1;

                while (j + count < points.length && points[j + count].slopeTo(temp[i])
                        == tempSlop) { // examine whether four dots followed are on the same line
                    count++;
                }
                if (count >= 3) {
                    Point least = temp[i];
                    Point largest = temp[i]; // signal of the beginning and end of the line
                    for (int k = j; k < j + count; k++) {
                        if (points[j].compareTo(least) < 0) least = points[k];
                        else if (points[k].compareTo(largest) > 0) largest = points[k];
                    }
                    lines[lineCount++] = new LineSegment(least, largest);
                    // lines[lineCount++] = new LineSegment(points[j], points[j + count - 1]);
                }
            }
        }
        linesResult = new LineSegment[lineCount];
        for (int i = 0; i < lineCount; i++) {
            linesResult[i] = lines[i];
        }
    }

    private static void merge(Comparator comparator, Point[] a, Point[] aux, int low, int mid,
                              int high) {
        for (int i = low; i <= high; i++) {
            aux[i] = a[i];
        }
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (less(comparator, aux[i], aux[j])) {
                a[k] = aux[i++];
            }
            else {
                a[k] = aux[j++];
            }
        }
    }

    private static void sort(Comparator comparator, Point[] a, Point[] aux, int low, int high) {
        if (high <= low) return;
        int mid = low + (high - low) / 2;
        sort(comparator, a, aux, low, mid);
        sort(comparator, a, aux, mid + 1, high);
        merge(comparator, a, aux, low, mid, high);
    }

    private static boolean less(Comparator c, Object v, Object w) {
        return c.compare(v, w) < 0;
    }

    public int numberOfSegments() {    // the number of line segments
        return lineCount;
    }

    public LineSegment[] segments() {  // the line segments
        return linesResult;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
