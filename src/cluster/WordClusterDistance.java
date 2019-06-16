package cluster;

public class WordClusterDistance implements ClustersDistance{
    double alphaU;
    double alphaV;
    double beta;
    double gamma;

    @Override
    public double recalcClustersDistance(Cluster cluster1, Cluster cluster2, Cluster cluster3, int num1, int num2, int num3) {
        System.out.println("[WordClustersDistance]\n");
        this.alphaU = (getNumPoints(cluster3) + getNumPoints(cluster1))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        System.out.println("\tAlphaU = " + alphaU + '\n');
        this.alphaV = (getNumPoints(cluster3) + getNumPoints(cluster2))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        System.out.println("\tAlphaV = " + alphaV + '\n');
        this.beta = -(getNumPoints(cluster3))/(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        System.out.println("\tbeta = " + beta + '\n');
        this.gamma = 0;
        double distance = alphaU * getDistanceToCluster(cluster1, num3) + alphaV* getDistanceToCluster(cluster2, num3) + beta*getDistanceToCluster(cluster1, num2);
        System.out.println("\tdistance between clusters " + num1 + " and " + num3 + " = " + distance + '\n');
        return distance;
    }

    private int getNumPoints(Cluster cluster) {
        return cluster.getPoints().size();
    }

    private double getDistanceToCluster(Cluster cluster, int num) {
        return cluster.getDistances().get(num);
    }
}
