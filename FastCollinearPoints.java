import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();
    // private LineSegment[] lines = new LineSegment[100];
    private LineSegment[] linesResult;
    private final Point[] points;
    private int lineCount = 0;

    public FastCollinearPoints(
            Point[] points) {   // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException("Input Invalid: null points are given");
        }
        this.points = Arrays.copyOf(points, points.length);
        for (int i = 0; i < this.points.length; i++) {
            if (this.points[i] == null) {
                throw new IllegalArgumentException("Input Invalid: null points are given");
            }
        }
        for (int i = 0; i < this.points.length; i++) {
            for (int j = i + 1; j < this.points.length; j++) {
                if (this.points[i].slopeTo(this.points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("Input Invalid: Repeated points are given");
                }
            }
        }
        linesResult = findLines();
    }

    // private static void merge(Comparator comparator, Point[] a, Point[] aux, int low, int mid,
    //                           int high) {
    //     for (int i = low; i <= high; i++) {
    //         aux[i] = a[i];
    //     }
    //     int i = low;
    //     int j = mid + 1;
    //     for (int k = low; k <= high; k++) {
    //         if (i > mid) a[k] = aux[j++];
    //         else if (j > high) a[k] = aux[i++];
    //         else if (less(comparator, aux[i], aux[j])) {
    //             a[k] = aux[i++];
    //         }
    //         else {
    //             a[k] = aux[j++];
    //         }
    //     }
    // }
    //
    // private static void sort(Comparator comparator, Point[] a, Point[] aux, int low, int high) {
    //     if (high <= low) return;
    //     int mid = low + (high - low) / 2;
    //     sort(comparator, a, aux, low, mid);
    //     sort(comparator, a, aux, mid + 1, high);
    //     merge(comparator, a, aux, low, mid, high);
    // }
    //
    // private static boolean less(Comparator c, Point v, Point w) {
    //     if (c.compare(v, w) == 0) {
    //         if (v.compareTo(w) < 0) {
    //             return true;
    //         }
    //         else if (v.compareTo(w) >= 0) {
    //             return false;
    //         }
    //     }
    //     return c.compare(v, w) < 0;
    // }


    public int numberOfSegments() {    // the number of line segments
        return linesResult.length;
    }

    public LineSegment[] segments() {  // the line segments
        return Arrays.copyOf(linesResult, linesResult.length);
    }


    private LineSegment[] findLines() {
        // Point[] aux = new Point[points.length];
        Point[] temp;
        ArrayList<Point> starts = new ArrayList<>();
        ArrayList<Point> ends = new ArrayList<>();
        // Point[] starts = new Point[100];
        // Point[] ends = new Point[100];
        temp = Arrays.copyOf(points, points.length);
        // for (int i = 0; i < points.length;
        //      i++) {   // put points to temp for later editing points
        //     temp[i] = points[i];
        // }
        for (int i = 0; i < points.length;
             i++) { // traverse points and sort points according to their slope to temp[i]
            // sort(points[i].slopeOrder(), temp, aux, 0, points.length - 1);
            Arrays.sort(temp, points[i].slopeOrder());
            for (int j = 0; j < temp.length; j++) {
                double tempSlop = temp[j].slopeTo(points[i]);
                int count = 1;
                while (j + count < temp.length && temp[j + count].slopeTo(points[i])
                        == tempSlop) { // examine whether four dots followed are on the same line
                    count++;
                }
                if (count >= 3) {
                    int flag = 0;
                    Point least = points[i];
                    Point largest = points[i]; // signal of the beginning and end of the line
                    for (int k = j; k < j + count; k++) {
                        if (temp[k].compareTo(least) < 0) least = temp[k];
                        else if (temp[k].compareTo(largest) > 0) largest = temp[k];
                    }
                    for (int index = 0; index < lineCount; index++) {
                        // examine whether least -> largest exists.
                        if (starts.get(index).slopeTo(ends.get(index)) == least.slopeTo(largest)) {
                            if (starts.get(index) == least
                                    || starts.get(index).slopeTo(least) == starts.get(index)
                                                                                 .slopeTo(ends.get(
                                                                                         index))) {

                            }
                            flag = 1;
                        }
                        // if (starts.get(index).compareTo(least) == 0
                        //         && ends.get(index).compareTo(largest) == 0) {
                        //     flag = 1;
                        // }
                    }
                    if (flag == 0) {
                        lines.add(new LineSegment(least, largest));
                        starts.add(least);
                        ends.add(largest);
                        lineCount++;
                    }
                }
            }
        }
        LineSegment[] segments = new LineSegment[lines.size()];
        return lines.toArray(segments);
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

