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
    private double crossProbability;
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

    public GASolution(Point[] points, Metric metric, final PrintStream log, int populationSize, int numClusters, int s, double crossProbability, double mutProbability, int runningTime) {
        this.points = points;
        this.metric = metric;
        this.log = log;
        this.populationSize = populationSize;
        this.numClusters = numClusters;
        this.s = s;
        this.crossProbability = crossProbability;
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
        double bestValue = 0;
        ArrayList<Double> bestValues = new ArrayList<>();
        bestValues.add(Double.MAX_VALUE);
        do {
            Genotype ksi = selectTournamentGenotypes(population);
            Genotype etta = selectTournamentGenotypes(population);
            Genotype child = crossover(ksi, etta);
            mutate(child);
            log.println("ksi fitness = " + ksi.getFitnessValue() + " etta fitness = " + etta.getFitnessValue() + " Child fitnessVal = " + child.getFitnessValue());
            replaceGenotype(child);
            bestValue = getBestValue(population);
            if (bestValue < bestValues.get(bestValues.size() - 1)) {
                bestValues.add(bestValue);
                System.out.println(bestValue);
            }
            if (curBestValue > bestValue) {
                curBestValue = bestValue;
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
//        logBestValues(bestValues, log);
        logBestSolNumPoints(bestSolution);
        logBestValue(bestValues, log);
        log.close();
    }

    private void logBestValue(ArrayList<Double> bestValues, PrintStream log) {
//        double bestValue = Double.MAX_VALUE;
//        for (Double value: bestValues) {
//            if (bestValue > value)
//                bestValue = value;
//        }
        log.println("Best value = " + bestValues.get(bestValues.size() - 1));
    }

    private void logBestValues(ArrayList<Double> bestValues, PrintStream log) {
        log.println("Best values : [" );
        for(Double value: bestValues)
            log.println(value);
        log.println("]");
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
        population.add(child);
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
            if(Math.random() < mutProbability) {
                int numClust = 0;
                do {
                    numClust = (int) (Math.random() * (numClusters));

                } while (numClust == child.getPointsInClusters()[i]);
                child.getClusterPoints().get(child.getPointsInClusters()[i]).remove((Integer) i);
                child.getClusterPoints().get(numClust).add(i);
                child.getPointsInClusters()[i] = numClust;
            }
        }
        child.calcFitnessValue(metric, points, log);
    }

    private Genotype crossover(Genotype ksi, Genotype etta) {
        double chooseRand = Math.random();
//        System.out.println(chooseRand);
        if(chooseRand < this.crossProbability) {
//            changeClusterNums(ksi, etta);
            int[] pointsInClusters = new int[ksi.getPointsInClusters().length];
            for (int i = 0; i < ksi.getPointsInClusters().length; ++i) {
                if (Math.random() > 0.5)
                    pointsInClusters[i] = ksi.getPointsInClusters()[i];
                else
                    pointsInClusters[i] = etta.getPointsInClusters()[i];

            }
            Genotype child = new Genotype(pointsInClusters, numClusters);
            child.calcFitnessValue(metric, points, log);
            return child;
        }
        return ksi;//new Genotype(ksi.getPointsInClusters(), numClusters);
    }

    private void changeClusterNums(Genotype ksi, Genotype etta) {
        int countSamePoints = 0, bestPoints = Integer.MAX_VALUE, numBestEtta = 0;
//        System.out.println("Ksi = " + printArray(ksi.getPointsInClusters()));
//        System.out.println("Etta = " + printArray(etta.getPointsInClusters()));
        ArrayList<Integer> changed;
        for (int i = 0; i < ksi.getClusterPoints().size(); i++) {
            for (int j = i; j < etta.getClusterPoints().size(); ++j) {
                countSamePoints = getSameNums(ksi.getClusterPoints().get(i), etta.getClusterPoints().get(j));
                if (countSamePoints < bestPoints) {
                    bestPoints = countSamePoints;
                    numBestEtta = j;
                }
            }
            changed = etta.getClusterPoints().get(numBestEtta);
            etta.getClusterPoints().remove(numBestEtta);
            etta.getClusterPoints().add(i, changed);
            for (int j = 0; j < etta.getPointsInClusters().length; j++) {
                if ((i <= etta.getPointsInClusters()[j])&&(etta.getPointsInClusters()[j]) < numBestEtta)
                    etta.getPointsInClusters()[j]++;
                else if (etta.getPointsInClusters()[j] == numBestEtta)
                    etta.getPointsInClusters()[j] = i;
            }
        }
//        System.out.println("Etta = " +  printArray(etta.getPointsInClusters()));
    }

    private String printArray(int[] pointsInClusters) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < pointsInClusters.length; i++)
            result.append(pointsInClusters[i]).append(" ,");
        result.append(" ]");
        return result.toString();
    }

    private int getSameNums(ArrayList<Integer> integers, ArrayList<Integer> integers1) {
        int count = 0;
        for (int i = 0; i < integers.size(); i++) {
            for(int j = 0; j < integers1.size(); j++) {
                if (integers.get(i).equals(integers1.get(j)))
                    count++;
            }
        }
        return count;
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
