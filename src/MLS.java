/**
 * Created by Dennis on 22/03/2017.
 */
public class MLS extends BaseAlgorithm {

    /**
     * Constructor
     * @param graph
     * @param maxLocalOptima
     */
    public MLS(Graph graph, int maxLocalOptima) {
        super(graph, maxLocalOptima);
    }

    public Solution partition() {

        // continue till an amount of local optima or infinitely if set to 0
        while(localOptima.size() < this.maxLocalOptima || this.maxLocalOptima == 0) {


            // start with random solution
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
            solution.fitness = evaluateSolution(solution);

            // climb till no improvement is found
            while (true) {
                Solution climbedSolution = climbFirstImprovement(solution);
                if (climbedSolution == null) {
                    break;
                }
                solution = climbedSolution;
            }

            saveNewOptimum(solution);


        }

        return this.bestSolution;
    }

}
