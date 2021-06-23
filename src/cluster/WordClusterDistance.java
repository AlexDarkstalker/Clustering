package cluster;

public class WordClusterDistance implements ClustersDistance{
    double alphaU;
    double alphaV;
    double beta;
    double gamma;

    @Override
    public double recalcClustersDistance(Cluster cluster1, Cluster cluster2, Cluster cluster3, int num1, int num2, int num3) {
        double numerator = 0, delimeter = 0;
//        System.out.println("[WordClustersDistance]\n");
        numerator = (double)(getNumPoints(cluster3) + getNumPoints(cluster1));
        delimeter = (double)((getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3)));
        this.alphaU = numerator / delimeter;
//        System.out.println("\tAlphaU = " + alphaU + '\n');
        numerator = (double)(getNumPoints(cluster3) + getNumPoints(cluster2));
        delimeter = (double)(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        this.alphaV = numerator / delimeter;
//        System.out.println("\tAlphaV = " + alphaV + '\n');
        numerator = (double)(-(getNumPoints(cluster3)));
        delimeter = (double)(getNumPoints(cluster1) + getNumPoints(cluster2) + getNumPoints(cluster3));
        this.beta = numerator / delimeter;
//        System.out.println("\tbeta = " + beta + '\n');
        this.gamma = 0;
        double distance = alphaU * getDistanceToCluster(cluster1, num3) + alphaV* getDistanceToCluster(cluster2, num3) + beta*getDistanceToCluster(cluster1, num2);
//        System.out.println("\tdistance between clusters " + num1 + " and " + num3 + " = " + distance + '\n');
        return distance;
    }

    private int getNumPoints(Cluster cluster) {
        return cluster.getPoints().size();
    }

    private double getDistanceToCluster(Cluster cluster, int num) {
        return cluster.getDistances().get(num);
    }
}
