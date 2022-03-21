import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();
    private LineSegment[] linesResult;
    private final Point[] points;

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

    public int numberOfSegments() {    // the number of line segments
        return linesResult.length;
    }

    public LineSegment[] segments() {  // the line segments
        return Arrays.copyOf(linesResult, linesResult.length);
    }


    private LineSegment[] findLines() {
        Point[] temp, origin;
        origin = Arrays.copyOf(points, points.length);
        Arrays.sort(origin);  // !!! save the origin version of sorted points array for later copy.
        for (Point p : points) { // traverse points and sort points according to their slope to temp[i]
            temp = Arrays
                    .copyOf(origin, origin.length);    // make sure every time temp is sorted before
            Arrays.sort(temp, p.slopeOrder());
            int begin = 1;
            int end = 1;
            while (end < temp.length) {
                double slopeEnd = temp[end].slopeTo(p);
                while (end + 1 < temp.length && temp[end + 1].slopeTo(p) == slopeEnd) {
                    end++;
                }
                if (end - begin >= 2) {
                    if (p.compareTo(temp[begin]) < 0) {
                        lines.add(new LineSegment(p, temp[end]));
                    }
                }
                begin = ++end;
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

