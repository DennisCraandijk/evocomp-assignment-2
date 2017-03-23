import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class ILS extends BaseAlgorithm {

    public int mutationSize;

    public ILS(Graph graph, int mutationSize) {
        super(graph);
        this.mutationSize = mutationSize;
    }

    public Solution partition(int maxlocalOptima) {

        // start with random solution
        Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
        updateFitness(solution);

        // continue till an amount of local optima or infinitely if set to 0
        while (this.localOptima.size() < maxlocalOptima || maxlocalOptima == 0) {

            // climb till no improvement is found
            while (true) {
                Solution climbedSolution = climbFirstImprovement(solution);
                if (climbedSolution == null) {
                    break;
                }
                solution = climbedSolution;
            }

            // check if this local optimum is different from the last local optimum
            // if this is the same as the last local optimum, it hasn't left base of attraction
            if (this.localOptima.size() == 0 || solution.bitArray.equals(this.localOptima.get(this.localOptima.size() - 1).bitArray) == false) {
                //add to local optima and best

                Solution localOptimum = solution.clone();
                this.localOptima.add(localOptimum);
                System.out.print("Local optimum " + this.localOptima.size() + " found: " + solution.fitness + "\n");

                if (this.bestSolution == null || solution.fitness > this.bestSolution.fitness) {
                    this.bestSolution = solution;
                }
            } else {
                System.out.print("Same local optimum found \n");
            }

            // mutate to find a new local optima
            mutate(solution);
        }

        return null;
    }

    /**
     * @param solution
     * @param size     the size of mutations to be made
     * @return
     */
    public Solution mutate(Solution solution) {
        for (int i = 0; i < this.mutationSize; i++) {
            int j = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));
            int k = ThreadLocalRandom.current().nextInt(0, (solution.bitArray.size() / 2));

            solution.vertexSwap(j, k);
        }

        return solution;
    }
}
