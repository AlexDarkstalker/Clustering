package cluster;

public class LWClusterDistance implements ClustersDistance{
    double alphaU;
    double alphaV;
    double beta;
    double gamma;

    @Override
    public double recalcClustersDistance(Cluster cluster1, Cluster cluster2, Cluster cluster3, int num1, int num2, int num3) {
        this.alphaU = (getNumPoints(cluster3) + getNumPoints(cluster1))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        this.alphaV = (getNumPoints(cluster3) + getNumPoints(cluster2))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        this.beta = -(getNumPoints(cluster3))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        this.gamma = 0;
        double distance = alphaU * getDistanceToCluster(cluster1, num3) + alphaV* getDistanceToCluster(cluster2, num3) + beta*getDistanceToCluster(cluster1, num2);
        return distance;
    }

    private int getNumPoints(Cluster cluster) {
        return cluster.getPoints().size();
    }

    private double getDistanceToCluster(Cluster cluster, int num) {
        return cluster.getDistances().get(num);
    }
}
