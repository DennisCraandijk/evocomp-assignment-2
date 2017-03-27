import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 22/03/2017.
 */
public class GLS extends BaseAlgorithm {

    private int populationSize;

    GLS(Graph graph, int maxLocalOptima, int maxCPUTime, int populationSize) {
        super(graph, maxLocalOptima, maxCPUTime);
        this.populationSize = populationSize;
    }

    /**
     * Partition the graph
     * @return
     */
    public Solution partition() {
        Population population = new Population(populationSize);

        // generate initial population
        for(int i = 0; i < populationSize; i++) {
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
            solution.fitness = evaluateSolution(solution);

            population.addSolution(solution);
        }

        // continue till stopping criteria is met
        while (!shouldStop()) {

            for (int i = 0; i < population.population.size(); i++) {

                // break if population iteration exceeds stopping criteria
                if(shouldStop()) break;

                // climb till no improvement is found
                while (true) {
                    Solution climbedSolution = climbFirstImprovement(population.population.get(i));
                    if (climbedSolution == null) break;
                    population.population.set(i, climbedSolution);
                }

                saveNewOptimum(population.population.get(i));

            }

            Solution child = uniformCrossover(population.population.get(0), population.population.get(1));

            // TODO crossover and selection

            //TODO exit if converged

        }

        return this.bestSolution;
    }

    /**
     * Uniform crossover on two parents
     * @param parent1
     * @param parent1
     * @return childBitArray
     */
    public Solution uniformCrossover(Solution parent1, Solution parent2)
    {
        List<Integer> childBitArray = new ArrayList<>(parent1.bitArray.size());

        // keep track of the bits where parents disagree
        List<Integer> disagreedBitIndexes = new ArrayList<>(parent1.bitArray.size());


        // invert one parent if hamming distance it larger than l/2
        boolean invertOneParent = false;
        if(getHammingDistance(parent1.bitArray, parent2.bitArray) > (parent1.bitArray.size() / 2)) {
            invertOneParent = true;
        }

        for(int i = 0; i < parent1.bitArray.size(); i++) {
            int parentBit1 = parent1.bitArray.get(i);
            int parentBit2 = parent2.bitArray.get(i);

            // invert one parent if hamming distance it larger than l/2
            if(invertOneParent) parentBit1 = 1 - parentBit1;

            if(parentBit1 == parentBit2) {
                childBitArray.add(i, parentBit1);
            } else {
                // set stub bit, will be overwritten
                childBitArray.add(i, -1);
                disagreedBitIndexes.add(i);
            }
        }


        // sample from the evenly distributed random bit array for the disagreed bits
        List<Integer> randomBitArray = generateRandomBitArray(disagreedBitIndexes.size());

        for(int i = 0; i < disagreedBitIndexes.size(); i++) {
            childBitArray.set(disagreedBitIndexes.get(i), randomBitArray.get(i));
        }


        return new Solution(childBitArray);
    }

    /**
     * Calculate hamming distance
     * @param bitArray1
     * @param bitArray2
     * @return hamming distance
     */
    private int getHammingDistance(List<Integer> bitArray1, List<Integer> bitArray2)
    {
        int distance = 0;
        for(int i = 0; i < bitArray1.size(); i++) {
            if(bitArray1.get(i).intValue() != bitArray2.get(i).intValue()){
                distance++;
            }
        }

        return distance;
    }
}
