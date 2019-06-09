package metric;

import problem.Point;

public class EuclidMetric implements Metric {
    public double getDistance(Point a, Point b) {
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY() - b.getY())*(a.getY()-b.getY()));
    }
}
