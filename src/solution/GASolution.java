package solution;

import cluster.Cluster;
import iteration.Iteration;
import metric.Metric;
import problem.Point;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GASolution implements Solution{
    private final PrintStream log;
    private Point[] points;
    private Cluster[] clusters;
    private int[] pointsInClusters;
    private Metric metric;
    private double[][] clusterDistances;
    private Iteration currentIter;
    private LinkedList<Cluster> clustersList;
    private ArrayList<Iteration> iterationsList;

    public GASolution(Point[] points, Metric metric, final PrintStream log) {
        this.points = points;
        this.metric = metric;
        this.log = log;
    }

    private List<Integer> getPointsFromCluster(int i) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int j = 0; j < pointsInClusters.length; j++) {
            if (pointsInClusters[j] == i)
                list.add(j);
        }
        return list;
    }

    @Override
    public void initializeStartSolution() {
        pointsInClusters = new int[points.length];
        for (int numOfCluster: pointsInClusters)
            numOfCluster = (int) (Math.random() * clusters.length);
    }

    @Override
    public void solve() {

    }
}
