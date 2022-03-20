import java.util.Comparator;

public class FastCollinearPoints {
    private Point[] aux, temp;
    private LineSegment[] lines = new LineSegment[100];
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
        aux = new Point[points.length];
        temp = new Point[points.length];
        for (int i = 0; i < temp.length; i++) {
            sort(temp[i].slopeOrder(), points, aux, 0, points.length - 1);
            for (int j = 0; i < points.length; i++) {
                double tempSlop = points[j].slopeTo(temp[i]);
                int count = 1;
                while (points[j + count].slopeTo(temp[i])
                        == tempSlop) { // examine whether four dots followed are on the same line
                    count++;
                }
                if (count >= 4) {
                    lines[lineCount++] = new LineSegment(points[j], points[j + count - 1]);
                }
            }
        }
    }

    private static void merge(Comparator comparator, Point[] a, Point[] aux, int low, int mid,
                              int high) {
        for (int i = low; i < high; i++) {
            aux[i] = a[i];
        }
        int i = low;
        int j = mid + 1;
        for (int k = low; k < high; k++) {
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
        return lines;
    }
}
