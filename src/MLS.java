/**
 * Created by Dennis on 22/03/2017.
 */
public class MLS extends BaseAlgorithm {

    public MLS(Graph graph) {
        super(graph);
    }

    public Solution partition(int maxLocalOptima) {

        // continue till an amount of local optima
        while(localOptima.size() < maxLocalOptima || maxLocalOptima == 0) {


            // start with random solution
            Solution solution = new Solution(generateRandomSolution(graph.nodes.length));
            updateFitness(solution);

            // climb till no improvement is found
            while (true) {
                Solution climbedSolution = climbFirstImprovement(solution);
                if (climbedSolution == null) {
                    break;
                }
                solution = climbedSolution;
            }

            //add to local optima and best
            System.out.print("Local optimum " + localOptima.size() + " found: " + solution.fitnessValue + "\n");
            localOptima.add(solution);

            if (bestSolution == null || solution.fitnessValue > bestSolution.fitnessValue) {
                bestSolution = solution;
            }
        }

        return bestSolution;
    }

    /**
     * Swaps a 0 and 1 on the string with incrementing index
     * Returns first improved solution
     * @param baseSolution
     * @return
     */
    private Solution climbFirstImprovement(Solution baseSolution) {
        for (int i = 0; i < (graph.nodes.length / 2); i++) {
            for (int j = 0; j < (graph.nodes.length / 2); j++) {
                Solution solution = baseSolution.clone();

                //swap incrementing 0 and 1 vertexes and check fitness
                solution.vertexSwap(i + 1, j + 1);
                updateFitness(solution);

                if (solution.fitnessValue > baseSolution.fitnessValue) {
                    return solution;
                }

            }
        }

        return null;
    }

}
