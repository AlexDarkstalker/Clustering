package run;

import problem.Problem;

import java.io.IOException;
import java.io.PrintStream;

public class RunClusteringLW {
    public static void main(String[] args) {
        try {
            Problem clusterProblem = new Problem("a1.txt", new PrintStream("log.txt"));

        } catch (IOException e) {
            e.getMessage();
        }
    }
}
