package solution;

import cluster.Cluster;
import iteration.Iteration;
import iteration.LWClusteringIteration;
import metric.Metric;
import org.w3c.dom.ls.LSOutput;
import problem.Point;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class LWSolution implements Solution{
    private double fitnessValue;
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
//        solve();
    }

    public double[][] getClusterDistances() {
        return clusterDistances;
    }


    @Override
    public void initializeStartSolution() {

    }

    public void solve() {
        final Date start = new Date();
        final Date curTime = new Date();
        this.clustersList = new LinkedList<>();
        initializeStartSolution(this.clustersList);
        this.iterationsList = new ArrayList<>();
        for(int i = 0; i < this.points.length - 20; i++)
        {
            for(int j = 0; j < this.clustersList.size(); j++)
                clustersList.get(j).setDistances(this.clusterDistances[j], j);
            this.currentIter = new LWClusteringIteration(this.clustersList);
            this.currentIter.runIter();
            this.iterationsList.add(this.currentIter);
            recalcDistances();
        }
        this.logToFile();
        curTime.setTime( new Date().getTime() - start.getTime());
        log.println( (curTime.getTime() / 1000));
        calcFitnessValue(metric);
        log.println("InClusterMeanDistance = " + fitnessValue);
        log.close();
    }

    public void calcFitnessValue(Metric metric) {
        fitnessValue = calcMeanInClusterDistance(metric);
    }

    private double calcMeanInClusterDistance(Metric metric) {
        double sumDistanceInCluster = 0, clusterResult = 0;
        int numCluster = 0;
        for (Cluster cluster: clustersList ) {
            sumDistanceInCluster = 0;
            if (cluster.getPoints().size() != 0) {
                for (int i = 0; i < cluster.getPoints().size(); ++i) {
                    for (int j = i + 1; j < cluster.getPoints().size(); ++j)
                        sumDistanceInCluster += metric.getDistance(cluster.getPoints().get(i), cluster.getPoints().get(j));
                }
//                log.println("Cluster # " + numCluster + " = " + getString(pointsInCluster));
//                log.println("MeanDist cluster # " + numCluster + " = " + (sumDistanceInCluster / pointsInCluster.size()) + " ");
                clusterResult += sumDistanceInCluster / cluster.getPoints().size();
            }
            numCluster++;
        }
        return  clusterResult;
    }

    private void recalcDistances() {
        this.clusterDistances = null;
        this.clusterDistances = new double[this.clustersList.size()][this.clustersList.size()];
        for(int i = 0; i < this.clustersList.size(); i++)
            for(int j = i; j < this.clustersList.size(); j++) {
                clusterDistances[i][j] = this.clustersList.get(i).getDistances().get(j);
                clusterDistances[j][i] = clusterDistances[i][j];
            }
    }

    private void logToFile() {
        for(int i = 0; i < this.iterationsList.size(); i++)
            logIter(this.iterationsList.get(i), i);

    }

    private void logIter(Iteration iter, int i) {
        log.print("[Iteration #" + i + "]{\n\t");
        for(int j = 0; j < iter.getClustersList().size(); j++)
            logCluster(iter.getClustersList().get(j), j);
        log.print("}");
    }

    private void logCluster(Cluster cluster, int j) {
        log.print("[Cluster#" + j + "]{\n\t");
        log.print(cluster.toString());
        log.print("}");
    }

    public void initializeStartSolution(LinkedList<Cluster> curIterClusters) {
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
            curIterClusters.get(i).setDistances(this.clusterDistances[i], i);
        System.out.println("Initialize start solution");
//        for(int i = 0; i < this.points.length; i++)
//            for(int j = i; j < this.points.length; j++) {
//                System.out.println(clusterDistances[i][j]);
//            }
//        System.out.println();

    }



//    private void runIteration(ArrayList<Cluster> curIterClusters, LWClusteringIteration currentIter) {
//        LWClusteringIteration curIter = new LWClusteringIteration(this.clustersList);
//        for(int i = 0; i < this.clustersList.size(); i++) {
//            curIter.getMinDistance(this.clustersList.get(i).getDistances().toArray(new double[] ));
//        }
//
//    }
}
