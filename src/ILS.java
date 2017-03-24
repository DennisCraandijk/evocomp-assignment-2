import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class ILS extends BaseAlgorithm {

    public int mutationSize;

    public ILS(Graph graph, int maxLocalOptima, int maxCPUTime, int mutationSize) {
        super(graph, maxLocalOptima, maxCPUTime);
        this.mutationSize = mutationSize;
    }

    public Solution partition() {

        // start with random solution
        Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
        solution.fitness = evaluateSolution(solution);

        // continue till stopping criteria is met
        while (!shouldStop()) {

            // climb till no improvement is found
            while (true) {
                Solution climbedSolution = climbFirstImprovement(solution);
                if (climbedSolution == null) break;
                solution = climbedSolution;
            }

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
            solution.fitness = evaluateSolution(solution);
        }

        return this.bestSolution;
    }

    /**
     * Do X amount random vertex swaps
     * @param solution
     * @return
     */
    public Solution mutate(Solution solution, int times) {
        for (int i = 0; i < times; i++) {
            int j = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));
            int k = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));

            solution.vertexSwap(j, k);
        }

        return solution;
    }
}
