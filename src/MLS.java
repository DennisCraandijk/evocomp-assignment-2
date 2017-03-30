/**
 * Created by Dennis on 22/03/2017.
 */
public class MLS extends BaseAlgorithm {

    /**
     * Constructor
     * @param graph
     * @param maxLocalOptima
     */
    MLS(Graph graph, String localSearchType, boolean skipConflictlessNodes, int maxLocalOptima, int maxCPUTime) {
        super(graph, localSearchType, skipConflictlessNodes, maxLocalOptima, maxCPUTime);
    }

    public Solution partition() {


        // continue till stopping criteria is met
        while (!shouldStop()) {

            // start with random solution
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
            solution.fitness = evaluateSolution(solution);

            solution = hillClimb(solution);

            saveNewOptimum(solution);


        }

        return this.bestSolution;
    }

}
