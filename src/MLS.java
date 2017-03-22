/**
 * Created by Dennis on 22/03/2017.
 */
public class MLS extends BaseAlgorithm {

    public Solution partition (Graph graph) {

        Solution solution = new Solution(generateRandomSolution(graph.nodes.length), graph);

        best = solution.clone();

        while(solution.fitnessValue <= best.fitnessValue) {
            //TODO swap all vertexes in order to find a new best solution
            solution.vertexSwap();
        }

        return null;
    }

}
