package solution;

import cluster.Cluster;
import iteration.Iteration;
import iteration.LWClusteringIteration;
import metric.Metric;
import problem.Point;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class LWSolution {
    private final PrintStream log;
    private Point[] points;
    private Cluster[] clusters;
    private Metric metric;
    private double[][] clusterDistances;
    private Iteration currentIter;
    private LinkedList<Cluster> clustersList;
    private ArrayList<Iteration> iterationsList;


    public Cluster[] getClusters() {
        return clusters;
    }

    public LWSolution(Point[] points, Metric metric, final PrintStream log) {
        this.points = points;
        this.metric = metric;
        this.log = log;
        solve();
    }

    public double[][] getClusterDistances() {
        return clusterDistances;
    }

    private void solve() {
        this.clustersList = new LinkedList<>();
        initializeStartSolution(this.clustersList);
        this.iterationsList = new ArrayList<>();
        for(int i = 0; i < this.points.length; i++)
        {
            this.currentIter = new LWClusteringIteration(this.clustersList);
            this.currentIter.runIter();
            this.iterationsList.add(this.currentIter);

        }
    }

    private void initializeStartSolution(LinkedList<Cluster> curIterClusters) {
        for(Point p: this.points) {
            Point[] initialPoints = new Point[1];
            initialPoints[0] = p;
            curIterClusters.add(new Cluster(initialPoints));
        }
        this.clusterDistances = new double[this.points.length][this.points.length];
        for(int i = 0; i < this.points.length; i++)
            for(int j = i; j < this.points.length; j++) {
                clusterDistances[i][j] = this.metric.getDistance(this.points[i], this.points[j]);
                clusterDistances[j][i] = clusterDistances[i][j];
            }
        for(int i = 0; i < this.clustersList.size(); i++)
            curIterClusters.get(i).setDistances(this.clusterDistances[i]);


    }



//    private void runIteration(ArrayList<Cluster> curIterClusters, LWClusteringIteration currentIter) {
//        LWClusteringIteration curIter = new LWClusteringIteration(this.clustersList);
//        for(int i = 0; i < this.clustersList.size(); i++) {
//            curIter.getMinDistance(this.clustersList.get(i).getDistances().toArray(new double[] ));
//        }
//
//    }
}
