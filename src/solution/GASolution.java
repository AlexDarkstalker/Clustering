package solution;

import cluster.Cluster;
import iteration.Iteration;
import metric.Metric;
import problem.Genotype;
import problem.Point;

import java.io.PrintStream;
import java.util.*;

public class GASolution implements Solution{
    private int numClusters;
    private int populationSize;
    private int s;
    private int runningTime;
    private double mutProbability;
    private ArrayList<Genotype> population;
//    private ArrayList<Genotype> tournamentGenotypes;
    private final PrintStream log;
    private Point[] points;
    private Cluster[] clusters;
    private int[] pointsInClusters;
    private Metric metric;
    private double[][] clusterDistances;
    private Iteration currentIter;
    private LinkedList<Cluster> clustersList;
    private ArrayList<Iteration> iterationsList;

    public GASolution(Point[] points, Metric metric, final PrintStream log, int populationSize, int numClusters, int s, double mutProbability, int runningTime) {
        this.points = points;
        this.metric = metric;
        this.log = log;
        this.populationSize = populationSize;
        this.numClusters = numClusters;
        this.s = s;
        this.mutProbability = mutProbability;
        this.runningTime = runningTime;
    }

    private List<Integer> getPointsFromCluster(int i) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int j = 0; j < pointsInClusters.length; j++) {
            if (pointsInClusters[j] == i)
                list.add(j);
        }
        return list;
    }

    @Override
    public void initializeStartSolution() {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; ++i) {
            Genotype gen = new Genotype(points, numClusters);
            gen.calcFitnessValue(metric, points, log);
            population.add(gen);
        }
    }

    @Override
    public void solve() {
        final Date start = new Date();
        final Date curTime = new Date();
        int curIt = 0;
        int numNotBetter = 0;
        double curBestValue = Double.MAX_VALUE;
        do {
            Genotype ksi = selectTournamentGenotypes(population);
            Genotype etta = selectTournamentGenotypes(population);
            Genotype child = crossover(ksi, etta);
            mutate(child);
            replaceGenotype(child);
            if (curBestValue > getBestValue(population)) {
                curBestValue = getBestValue(population);
                numNotBetter = 0;
            }
            else
                numNotBetter++;
            curTime.setTime( new Date().getTime() - start.getTime());
            log.println( (curTime.getTime() / 1000));
            log.println("curIt -" + curIt + ", CurBestValue = " + getBestValue(population) + "numNotBetter = " + numNotBetter );
//            System.out.println("curTime - " + (curTime.getTime() / 1000) + " curIt -" + curIt + ", CurBestValue = " + getBestValue(population));
            curIt++;
        } while ( (curTime.getTime() < runningTime * 1000 ));
        Genotype bestSolution = getBestSolution(population);
        logBestSolNumPoints(bestSolution);
        log.close();
    }

    private void logBestSolNumPoints(Genotype bestSolution) {
        int i = 0;
        for(ArrayList<Integer> arr: bestSolution.getClusterPoints()) {
            log.println("Num in cluster # " + i + " = " + arr.size());
            i++;
        }
    }

    private Genotype getBestSolution(ArrayList<Genotype> population) {
        double bestValue = Double.MAX_VALUE;
        Genotype bestSol = population.get(0);
        for (Genotype gen: population) {
            if (gen.getFitnessValue() < bestValue) {
                bestValue = gen.getFitnessValue();
                bestSol = gen;
            }
        }
        return bestSol;
    }

    private double getBestValue(ArrayList<Genotype> population) {
        double bestValue = Double.MAX_VALUE;
        for (Genotype gen: population) {
            if (gen.getFitnessValue() < bestValue)
                bestValue = gen.getFitnessValue();
        }
        return bestValue;
    }

    private void replaceGenotype(Genotype child) {
        int worstNum = findNumOfWorst(population);
        population.remove(worstNum);
        population.add(worstNum, child);
    }

    private int findNumOfWorst(ArrayList<Genotype> population) {
        int numOfWorst = 0;
        double worstValue = 0;
        for (int i = 0; i < population.size(); ++i) {
            if (population.get(i).getFitnessValue() > worstValue) {
                worstValue = population.get(i).getFitnessValue();
                numOfWorst = i;
            }
        }
        return numOfWorst;
    }

    private void mutate(Genotype child) {
        for(int i = 0; i < child.getPointsInClusters().length; ++i) {
            if(Math.random() < mutProbability)
                child.getPointsInClusters()[i] = (int)(Math.random() * (numClusters));
        }
    }

    private Genotype crossover(Genotype ksi, Genotype etta) {
        double chooseRand;
        int[] pointsInClusters = new int[ksi.getPointsInClusters().length];
        for(int i = 0; i < ksi.getPointsInClusters().length; ++i) {
            if(Math.random() > 0.5)
                pointsInClusters[i] = ksi.getPointsInClusters()[i];
            else 
                pointsInClusters[i] = etta.getPointsInClusters()[i];

        }
        Genotype child = new Genotype(pointsInClusters, numClusters);
        child.calcFitnessValue(metric, points, log);
        return child;
    }

    private Genotype selectTournamentGenotypes(ArrayList<Genotype> population) {
        ArrayList<Genotype> tournamentGenotypes = new ArrayList<>();
        int num = 0, minNum = 0;
        double minFitnessValue = Double.MAX_VALUE;
        for(int i = 0; i < s; ++i) {
            num = (int) (Math.random() * (populationSize));
            Genotype gen = population.get(num);
            tournamentGenotypes.add(gen);
            if(minFitnessValue > gen.getFitnessValue()) {
                minFitnessValue = gen.getFitnessValue();
                minNum = i;
            }
        }
        return tournamentGenotypes.get(minNum);
    }

}
