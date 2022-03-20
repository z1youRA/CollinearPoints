import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] lines = new LineSegment[100];
    private int linesNum = 0;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Input Invalid: null points are given");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("Input Invalid: Repeated points are given");
                }
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slope1 = points[j].slopeTo(points[i]);   // record the slope from i to j
                for (int k = j + 1; k < points.length; k++) {
                    if (points[k].slopeTo(points[i]) == slope1) {    // k is collinear to i and j
                        for (int m = k + 1; m < points.length; m++) {
                            if (points[m].slopeTo(points[i]) == slope1) {
                                lines[linesNum++] = new LineSegment(points[i], points[m]);
                                if (linesNum >= 100) {
                                    throw new UnsupportedOperationException(
                                            "Lines number exceeded");
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public int numberOfSegments() {     // the number of line segments
        return linesNum;
    }

    public LineSegment[] segments() {     // the line segments
        return lines;
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
