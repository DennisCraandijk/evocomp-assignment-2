/**
 * Created by Dennis on 22/03/2017.
 */
public class GLS extends BaseAlgorithm {

    public int populationSize;

    public GLS(Graph graph, int maxLocalOptima, int populationSize) {
        super(graph, maxLocalOptima);
        this.populationSize = populationSize;
    }

    public Solution partition() {
        Population population = new Population();

        for(int i = 0; i < populationSize; i++) {
            Solution solution = new Solution(generateRandomBitArray(graph.nodes.length));
            population.addSolution(solution);
        }

        return null;
    }
}
