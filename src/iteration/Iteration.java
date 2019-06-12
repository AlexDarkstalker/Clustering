package iteration;

import cluster.Cluster;

import java.util.LinkedList;

public interface Iteration {
    void runIter();
    LinkedList<Cluster> getClustersList();
}
