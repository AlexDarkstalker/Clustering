package problem;

import metric.EuclidMetric;
import metric.Metric;
import solution.GASolution;
import solution.LWSolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class GAClusteringProblem {

    private Point[] points;
    private Metric metric;

    public GAClusteringProblem(final String pointsFile, final PrintStream log) throws IOException {
        final BufferedReader br = new BufferedReader(new FileReader(pointsFile));
        points = readPoints(pointsFile, log, br);
        this.metric = new EuclidMetric();
        solveProblem(new GASolution(this.points, this.metric, log), this.metric, log);
    }

    private Point[] readPoints(String pointsFile, PrintStream log, BufferedReader br) throws IOException {
        Point[] points = new Point[0];
        final ArrayList<Point> list = new ArrayList<>();
        String line;
        String[] coord;
        int i = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            coord = line.split("   ");
            list.add(new Point(Double.valueOf(coord[0]), Double.valueOf(coord[1])));
//            for(String s: coord)
//                System.out.println(s + "!!");
            i++;
        }
        //log.print(i);
        points = list.toArray(points);
        //log.print(points.length);
//        for(Point p: points)
//            System.out.println(p.getX() + "! !" + p.getY());
        this.points = points;
        return points;
    }

    private void solveProblem(GASolution sol, Metric metric, PrintStream log) {
        sol.initializeStartSolution();
        sol.solve();
    }

}
