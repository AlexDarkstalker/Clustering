package run;

import problem.Problem;

import java.io.IOException;
import java.io.PrintStream;

public class RunClusteringGA {
    public static void main(String[] args) {
        try {
            Problem clusterProblem = new Problem("a0.txt", new PrintStream("log.txt"));

        } catch (IOException e) {
            e.getMessage();
        }
    }
}
