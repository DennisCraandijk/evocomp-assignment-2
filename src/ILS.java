import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class ILS extends BaseAlgorithm {

    private int mutationSize;

    ILS(Graph graph, String localSearchType, boolean skipConflictlessNodes, int maxLocalOptima, int maxCPUTime, int mutationSize) {
        super(graph, localSearchType, skipConflictlessNodes, maxLocalOptima, maxCPUTime);
        this.mutationSize = mutationSize;
    }

    public Solution partition() {

        // start with random solution
        Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
        solution.fitness = evaluateSolution(solution);

        // continue till stopping criteria is met
        while (!shouldStop()) {

            solution = hillClimb(solution);

            // check if this local optimum is different from the last local optimum
            // if this is the same as the last local optimum, it hasn't left base of attraction
            if (this.localOptima.size() == 0 || !solution.bitArray.equals(this.localOptima.get(this.localOptima.size() - 1).bitArray)) {

                saveNewOptimum(solution);

            } else {
                System.out.print("Same local optimum found \n");
            }

            // continue from a new mutation from the best know solution
            solution = bestSolution.clone();
            solution = mutate(solution, this.mutationSize);

            int evaluation = evaluateSolution(solution);
            if (solution.fitness != evaluation) {
                System.out.print("\n\n Error \n\n");
            }
            //solution.fitness = evaluateSolution(solution);
        }

        return this.bestSolution;
    }

    /**
     * Do X amount random vertex swaps
     * @param solution
     * @return
     */
    private Solution mutate(Solution solution, int times) {
        for (int i = 0; i < times; i++) {
            int j = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));
            int k = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));

            Integer nodeId1 = solution.colorIndex.get(0).get(j);
            Integer nodeId2 = solution.colorIndex.get(1).get(k);

            // check if this swap would lower the fitness
            int swapPotential = evaluateSwapPotential(solution, nodeId1, nodeId2);

            // and update the fitness and all the affected nodes their color conflict count
            solution.vertexSwap(j, k);
            solution = updateConflictCountAfterSwap(solution, nodeId1, nodeId2);
            solution.fitness = solution.fitness + swapPotential;
        }

        return solution;
    }
}
