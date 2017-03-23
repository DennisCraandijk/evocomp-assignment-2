/**
 * Created by Dennis on 22/03/2017.
 */
public class MLS extends BaseAlgorithm {

    public MLS(Graph graph) {
        super(graph);
    }

    public Solution partition(int maxLocalOptima) {

        // continue till an amount of local optima or infinitely if set to 0
        while(localOptima.size() < maxLocalOptima || maxLocalOptima == 0) {


            // start with random solution
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
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
            this.localOptima.add(solution);
            System.out.print("Local optimum " + this.localOptima.size() + " found: " + solution.fitness + "\n");

            if (this.bestSolution == null || solution.fitness > this.bestSolution.fitness) {
                this.bestSolution = solution;
            }
        }

        return this.bestSolution;
    }

}
