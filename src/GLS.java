import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Dennis on 22/03/2017.
 */
public class GLS extends BaseAlgorithm {

    public int nIterationsConverged = 0;

    private int populationSize;

    GLS(Graph graph, int maxLocalOptima, int maxCPUTime, int populationSize) {
        super(graph, maxLocalOptima, maxCPUTime);
        this.populationSize = populationSize;
    }

    /**
     * Partition the graph
     *
     * @return
     */
    public Solution partition() {
        Population population = new Population(populationSize);

        // generate initial population
        while (population.population.size() < populationSize) {
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
            solution.fitness = evaluateSolution(solution);

            solution = hillClimb(solution);


            saveNewOptimum(solution);

            if (population.contains(solution)) {
                continue;
            }

            population.addSolution(solution);
        }

        Random rand = new Random();
        // continue till stopping criteria is met
        while (!shouldStop()) {
            // generate 2 distinc indices
            int index1 = rand.nextInt(population.population.size());
            int index2 = rand.nextInt(population.population.size() - 1);
            if (index2 >= index1) index2++;

            Solution child = uniformCrossover(population.population.get(index1), population.population.get(index2));
            child.fitness = evaluateSolution(child);

            child = hillClimb(child);

            saveNewOptimum(child);

            // if child already in population
            if (population.contains(child)) {
                continue;
            }

            sortPopulation(population);
            if (child.fitness > population.population.get(populationSize - 1).fitness) {
                continue;
            }

            // if child is unique and improvement of population, add it
            population.replaceSolution(child, populationSize - 1);

            population.iterations++;
            if (hasConverged(population)) {
                nIterationsConverged = population.iterations;
                break;
            }
        }

        return this.bestSolution;
    }

    /**
     * Uniform crossover on two parents
     *
     * @param parent1
     * @param parent1
     * @return childBitArray
     */
    public Solution uniformCrossover(Solution parent1, Solution parent2) {
        List<Integer> childBitArray = new ArrayList<>(parent1.bitArray.size());

        // keep track of the bits where parents disagree
        List<Integer> disagreedBitIndexes = new ArrayList<>(parent1.bitArray.size());


        // invert one parent if hamming distance it larger than l/2
        boolean invertOneParent = false;
        if (getHammingDistance(parent1.bitArray, parent2.bitArray) > (parent1.bitArray.size() / 2)) {
            invertOneParent = true;
        }

        for (int i = 0; i < parent1.bitArray.size(); i++) {
            int parentBit1 = parent1.bitArray.get(i);
            int parentBit2 = parent2.bitArray.get(i);

            // invert one parent if hamming distance it larger than l/2
            if (invertOneParent) parentBit1 = 1 - parentBit1;

            if (parentBit1 == parentBit2) {
                childBitArray.add(i, parentBit1);
            } else {
                // set stub bit, will be overwritten
                childBitArray.add(i, -1);
                disagreedBitIndexes.add(i);
            }
        }


        // sample from the evenly distributed random bit array for the disagreed bits
        List<Integer> randomBitArray = generateRandomBitArray(disagreedBitIndexes.size());

        for (int i = 0; i < disagreedBitIndexes.size(); i++) {
            childBitArray.set(disagreedBitIndexes.get(i), randomBitArray.get(i));
        }


        return new Solution(childBitArray);
    }

    /**
     * Calculate hamming distance
     *
     * @param bitArray1
     * @param bitArray2
     * @return hamming distance
     */
    private int getHammingDistance(List<Integer> bitArray1, List<Integer> bitArray2) {
        int distance = 0;
        for (int i = 0; i < bitArray1.size(); i++) {
            if (bitArray1.get(i).intValue() != bitArray2.get(i).intValue()) {
                distance++;
            }
        }

        return distance;
    }

    private void sortPopulation(Population pop) {
        Collections.sort(pop.population, (Solution a1, Solution a2) -> a1.fitness - a2.fitness);
    }

    // If HammingDistance is 0 on best and worst solution, we assume that it has converged 
    private boolean hasConverged(Population pop) {
        sortPopulation(pop);
        return getHammingDistance(pop.population.get(0).bitArray, pop.population.get(pop.population.size() - 1).bitArray) == 0;
    }
}
