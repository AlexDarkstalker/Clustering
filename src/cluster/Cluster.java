package cluster;

import problem.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Cluster {
    private double minDist;

    private int minDistClustNum;

    private ArrayList<Point> points;

    private LinkedList <Double> distances;

    public void setDistances(double[] distances) {
        this.distances = new LinkedList<>();
        for(int i = 1;  i< distances.length; i++)
            this.distances.add(distances[i]);
        this.setMinDist();

    }

    public void addPoints(List<Point> points) {
        this.points.addAll(points);
    }

    public void setDistance(int numCluster, double distance) {
        this.distances.set(numCluster, distance);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = new ArrayList<>(Arrays.asList(points));
    }

    public Cluster(Point[] points) {
        this.points = new ArrayList<>(Arrays.asList(points));
    }

    public LinkedList<Double> getDistances() {
        return distances;
    }

    @Override
    public String toString() {
        return "points=" + points +
                ", distances=" + distances +
                '}' + '\n';
    }

    public double getMinDist() {
        return minDist;
    }

    public int getMinDistClustNum() {
        return minDistClustNum;
    }

    public void recalcDistances() {

    }

    private void setMinDist() {
        int i = 0;
        int numMinDist = -1;
        double minDist = Double.MAX_VALUE;
        while (i < this.distances.size()) {
            if(this.distances.get(i) < minDist) {
                minDist = this.distances.get(i);
                numMinDist = i;
            }
            i++;
        }
        this.minDistClustNum = numMinDist;
        this.minDist = minDist;
    }
}
