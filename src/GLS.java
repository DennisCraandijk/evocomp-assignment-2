/**
 * Created by Dennis on 22/03/2017.
 */
public class GLS extends BaseAlgorithm {

    public int populationSize;

    public GLS(int populationSize) {
        this.populationSize = populationSize;
    }

    public Solution partition(Graph graph) {
        Population population = new Population();

        for(int i = 0; i < populationSize; i++) {
            Solution solution = new Solution(generateRandomSolution(graph.nodes.length), graph);
            population.addSolution(solution);
        }

        return null;
    }
}
