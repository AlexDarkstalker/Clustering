package run;

import problem.GAClusteringProblem;
import problem.Problem;

import java.io.IOException;
import java.io.PrintStream;

public class RunClusteringGA {
    public static void main(String[] args) {
        try {
            GAClusteringProblem clusterProblem = new GAClusteringProblem("a1.txt", new PrintStream("log.txt"));

        } catch (IOException e) {
            e.getMessage();
        }
    }
}
