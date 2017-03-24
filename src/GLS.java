import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 22/03/2017.
 */
public class GLS extends BaseAlgorithm {

    public int populationSize;

    public GLS(Graph graph, int maxLocalOptima, int populationSize) {
        super(graph, maxLocalOptima);
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
            population.addSolution(solution);
        }

        // continue till an amount of local optima or infinitely if set to 0
        while (this.localOptima.size() < this.maxLocalOptima || this.maxLocalOptima == 0) {

            for (int i = 0; i < population.population.size(); i++) {
                // climb till no improvement is found
                while (true) {
                    Solution climbedSolution = climbFirstImprovement(population.population.get(i));
                    if (climbedSolution == null) break;
                    population.population.set(i, climbedSolution);
                }

                saveNewOptimum(population.population.get(i));

            }

            // TODO crossover and selection

            //TODO exit if converged

        }

        return this.bestSolution;
    }

    /**
     * Uniform crossover on two parents
     * @param parentBitArray1
     * @param parentBitArray2
     * @return childBitArray
     */
    public List<Integer> crossover(List<Integer> parentBitArray1, List<Integer> parentBitArray2)
    {
        List<Integer> childBitArray = new ArrayList<>(parentBitArray1.size());

        // keep track of the bits where parents disagree
        List<Integer> disagreedBitIndexes = new ArrayList<>(parentBitArray1.size());


        // invert one parent if hamming distance it larger than l/2
        boolean invertOneParent = false;
        if(getHammingDistance(parentBitArray1, parentBitArray2) > (parentBitArray1.size() / 2)) {
            invertOneParent = true;
        }

        for(int i = 0; i < parentBitArray1.size(); i++) {
            int parentBit1 = parentBitArray1.get(i).intValue();
            int parentBit2 = parentBitArray2.get(i).intValue();

            // invert one parent if hamming distance it larger than l/2
            if(invertOneParent) parentBit1 = 1 - parentBit1;

            if(parentBit1 == parentBit2) {
                childBitArray.add(i, parentBit1);
            } else {
                disagreedBitIndexes.add(i);
            }
        }


        // sample from the evenly distributed random bit array for the disagreed bits
        List<Integer> randomBitArray = generateRandomBitArray(disagreedBitIndexes.size());

        for(int i = 0; i < disagreedBitIndexes.size(); i++) {
            childBitArray.add(disagreedBitIndexes.get(i), randomBitArray.get(i));
        }


        return childBitArray;
    }

    /**
     * Calculate hamming distance
     * @param bitArray1
     * @param bitArray2
     * @return hamming distance
     */
    public int getHammingDistance(List<Integer> bitArray1, List<Integer> bitArray2)
    {
        int distance = 0;
        for(int i = 0; i < bitArray1.size(); i++) {
            if(bitArray1.get(i) != bitArray2.get(i)) {
                distance++;
            }
        }

        return distance;
    }
}
