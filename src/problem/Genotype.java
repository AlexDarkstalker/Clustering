package problem;

import metric.Metric;

import java.io.PrintStream;
import java.util.ArrayList;

public class Genotype {
    //Каждый элемент массива - номер кластера, которому принадлежит точка
    int [] pointsInClusters;
    //Номера точек в массиве i соответствуют всем точкам из кластера i
    ArrayList<ArrayList<Integer>> clusterPoints;
    //Значение функции приспособленности
    double fitnessValue;

    public Genotype(int[] pointsInClusters, int numClusters) {
        this.pointsInClusters = pointsInClusters;
        createClusterPoints(numClusters);
    }

    public Genotype(Point[] points, int numClusters) {
        createPointsInClusters(points, numClusters);
        createClusterPoints(numClusters);
    }

    private void createPointsInClusters(Point[] points, int numClusters) {
        pointsInClusters = new int[points.length];
        double randValue;
        for(int i = 0; i < points.length; ++i) {
            randValue = Math.random() * (numClusters);
            pointsInClusters[i] = (int) (randValue);
        }
    }

    private void createClusterPoints(int numClusters) {
        clusterPoints = new ArrayList<>(numClusters);
        for(int i = 0; i < numClusters; ++i)
            clusterPoints.add(new ArrayList<Integer>());
//        for(ArrayList<Integer> cluster: clusterPoints)
//            cluster = new ArrayList<>();
        for (int i = 0; i < pointsInClusters.length; ++i) {
            clusterPoints.get(pointsInClusters[i]).add(i);
        }
    }

    public void calcFitnessValue(Metric metric, Point[] points, final PrintStream log) {
        fitnessValue = calcMeanInClusterDistance(metric, points, log);
    }

    private double calcMeanInClusterDistance(Metric metric, Point[] points, final PrintStream log) {
        double sumDistanceInCluster = 0, clusterResult = 0;
        int numCluster = 0;
        for (ArrayList<Integer> pointsInCluster: clusterPoints ) {
            sumDistanceInCluster = 0;
            if (pointsInCluster.size() != 0) {
                for (int i = 0; i < pointsInCluster.size(); ++i) {
                    for (int j = i + 1; j < pointsInCluster.size(); ++j)
                        sumDistanceInCluster += metric.getDistance(points[pointsInCluster.get(i)], points[pointsInCluster.get(j)]);
                }
//                log.println("Cluster # " + numCluster + " = " + getString(pointsInCluster));
//                log.println("MeanDist cluster # " + numCluster + " = " + (sumDistanceInCluster / pointsInCluster.size()) + " ");
                clusterResult += sumDistanceInCluster / pointsInCluster.size();
            }
            numCluster++;
        }
        return  clusterResult;
    }

    private String getString(ArrayList<Integer> pointsInCluster) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < pointsInCluster.size(); ++i) {
            if(i != pointsInCluster.size() - 1)
                result.append(pointsInCluster.get(i) + ", ");
            else
                result.append(pointsInCluster.get(i) + "]");
        }
        return result.toString();
    }

    public int[] getPointsInClusters() {
        return pointsInClusters;
    }

    public void setPointsInClusters(int[] pointsInClusters) {
        this.pointsInClusters = pointsInClusters;
    }

    public ArrayList<ArrayList<Integer>> getClusterPoints() {
        return clusterPoints;
    }

    public void setClusterPoints(ArrayList<ArrayList<Integer>> clusterPoints) {
        this.clusterPoints = clusterPoints;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
}
