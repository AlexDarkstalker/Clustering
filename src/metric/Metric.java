package metric;

import problem.Point;

public interface Metric {
    double getDistance(Point x, Point y);
}
