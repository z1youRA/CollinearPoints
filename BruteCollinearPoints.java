import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] linesResult;
    private final Point[] points;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
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

    private LineSegment[] findLines() {
        ArrayList<LineSegment> lines = new ArrayList<>();
        for (int i = 0; i < this.points.length; i++) {
            for (int j = i + 1; j < this.points.length; j++) {
                double slope1 = this.points[j]
                        .slopeTo(this.points[i]);   // record the slope from i to j
                for (int k = j + 1; k < this.points.length; k++) {
                    if (this.points[k].slopeTo(this.points[j])
                            == slope1) {    // k is collinear to i and j
                        for (int m = k + 1; m < this.points.length; m++) {
                            if (this.points[m].slopeTo(this.points[k]) == slope1) {
                                int little, large;
                                int ijMin, kmMin;
                                // find the start and end of the line.
                                ijMin = this.points[i].compareTo(this.points[j]) < 0 ? i : j;
                                kmMin = this.points[k].compareTo(this.points[m]) < 0 ? k : m;
                                little = this.points[ijMin].compareTo(this.points[kmMin]) < 0 ?
                                         ijMin : kmMin;
                                large = this.points[i + j - ijMin]
                                                .compareTo(this.points[k + m - kmMin]) > 0 ?
                                        i + j - ijMin :
                                        k + m - kmMin;
                                lines.add(new LineSegment(this.points[little],
                                                          this.points[large]));

                            }
                        }
                    }

                }
            }
        }
        LineSegment[] segments = new LineSegment[lines.size()];
        return lines.toArray(segments);
    }

    public int numberOfSegments() {     // the number of line segments
        return linesResult.length;
    }

    public LineSegment[] segments() {     // the line segments
        return Arrays.copyOf(linesResult, linesResult.length);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
