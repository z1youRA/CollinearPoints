import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] lines = new LineSegment[100];
    private LineSegment[] linesResult;
    private int linesNum = 0;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        Point starts[] = new Point[100];
        Point ends[] = new Point[100];
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
                    if (points[k].slopeTo(points[j]) == slope1) {    // k is collinear to i and j
                        for (int m = k + 1; m < points.length; m++) {
                            if (points[m].slopeTo(points[k]) == slope1) {
                                int little, large, flag = 0;
                                int ijMin, kmMin;
                                ijMin = points[i].compareTo(points[j]) < 0 ? i : j;
                                kmMin = points[k].compareTo(points[m]) < 0 ? k : m;
                                little = points[ijMin].compareTo(points[kmMin]) < 0 ? ijMin : kmMin;
                                large = points[i + j - ijMin].compareTo(points[k + m - kmMin]) > 0 ?
                                        i + j - ijMin :
                                        k + m - kmMin;
                                for (int index = 0; index < linesNum;
                                     index++) {    // check repetition of line little to large;
                                    if (starts[index] == points[little]
                                            && ends[index] == points[large]) {
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    starts[linesNum] = points[little];
                                    ends[linesNum] = points[large];
                                    lines[linesNum++] = new LineSegment(points[little],
                                                                        points[large]);
                                }
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
        linesResult = new LineSegment[linesNum];
        for (int i = 0; i < linesNum; i++) {
            linesResult[i] = lines[i];
        }
    }

    public int numberOfSegments() {     // the number of line segments
        return linesNum;
    }

    public LineSegment[] segments() {     // the line segments
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
