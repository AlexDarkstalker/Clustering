package iteration;

import cluster.Cluster;
import cluster.ClustersDistance;
import cluster.WordClusterDistance;

import java.util.ArrayList;
import java.util.LinkedList;

public class LWClusteringIteration implements Iteration{
    private ClustersDistance clustersDistance;
    private LinkedList<Cluster> clustersList;
    private double[] minDistances;
    private double minDistance;
    private int num1;
    private int num2;

    public LWClusteringIteration(LinkedList<Cluster> clustersList) {
        this.clustersDistance = new WordClusterDistance();
        this.clustersList = clustersList;
        Double[] dists = new Double[0];
        ArrayList<Double> minDists = new ArrayList<>();
        for(int i = 0; i < clustersList.size(); i++)
            minDists.add(clustersList.get(i).getMinDist());
        dists = minDists.toArray(dists);
        this.minDistances = new double[dists.length];
        for(int i = 0; i < dists.length; i++)
            this.minDistances[i] = dists[i];
        System.out.println("Iteration created\n");
        this.findMin();
        System.out.println("FoundMinimaDistance between " + num1 + " and " + num2 + '\n');
    }

    public void runIter() {
//        this.findMin();
//        System.out.println("FoundMinimaDistance between " + num1 + " and " + num2 + '\n');
        System.out.println("Start running iter\n");
//        this.recalcClusterDistance();
        this.clustersList.get(num1).addPoints(this.clustersList.get(num2).getPoints());
        this.recalcClusterDistance();
        this.recalcDistanceToCluster();
        this.kickElem(num2);
        this.nullDistanceToCluster(num2);
    }

    private void nullDistanceToCluster(int num2) {
//        for(Cluster cluster: this.clustersList)
//            cluster.setDistance(num2, 0.);
        for(Cluster cluster: clustersList) {
            cluster.getDistances().remove(num2);
        }
    }

    private void recalcDistanceToCluster() {
        for(int i = 0; i < this.clustersList.size(); i++)
            this.getClustersList().get(i).getDistances().set(num1, this.clustersList.get(num1).getDistances().get(i));
    }

    private void recalcClusterDistance() {
//        System.out.println("Recalc distances to all clusters for cluster " + num1 + '\n');
        for(int i = 0; i < this.clustersList.size(); i++) {
            if((i == num1)||(i == num2))
                continue;
//            else if (i == num2)
//                this.clustersList.get(num1).getDistances().re
            this.clustersList.get(num1).getDistances().set(i, this.clustersDistance.recalcClustersDistance(this.clustersList.get(num1), this.clustersList.get(num2), this.clustersList.get(i), num1, num2, i));

        }
    }

    private void kickElem(int numElem) {
        this.clustersList.remove(numElem);
    }



    public LinkedList<Cluster> getClustersList() {
        return clustersList;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    private double findMin (double [][] distances) {
        double minDist;
        minDist = Double.MAX_VALUE;
        for(int i = 0; i < distances.length; i++)
            for (int j = i+1; j < distances[i].length; j++)
                if(distances[i][j] < minDist)
                    minDist = distances[i][j];
                return minDist;
    }

    private void findMin () {
        double minDist;
        int numMin = -1;
        minDist = Double.MAX_VALUE;
        for(int i = 0; i < this.minDistances.length; i++) {
//            System.out.println("[findMin] cluster" + i +":\n");
            if (this.minDistances[i] < minDist) {
                minDist = this.minDistances[i];
                numMin = i;
//                System.out.println("\t[findMin] current minDist= " + minDist + " to cluster " + numMin + "\n\tnew min dist = " + this.minDistances[i] + '\n');
            }
        }
        this.minDistance = minDist;
        this.num1 = numMin;
        this.num2 = this.clustersList.get(numMin).getMinDistClustNum();
    }

}
