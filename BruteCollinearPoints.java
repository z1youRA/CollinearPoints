public class BruteCollinearPoints {
    private LineSegment[] lines = new LineSegment[100];
    private int linesNum = 0;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
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
}
