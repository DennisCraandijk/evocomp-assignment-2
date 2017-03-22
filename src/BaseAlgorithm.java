import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dennis on 21/03/2017.
 */
public class BaseAlgorithm {

    public Graph graph;

    public Solution bestSolution = null;

    //TODO make localOptima capacity variable
    public List<Solution> localOptima = new ArrayList<>(2500);

    public BaseAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public List generateRandomSolution(int nodes) {

        ArrayList<Integer> bitArray = new ArrayList<>(nodes);

        for (int i = 0; i < nodes; i++) {
            // alternate value between 0 and 1
            Integer color = ( (i & 1) == 0 ) ? 0 : 1;
            bitArray.add(color);
        }

        Collections.shuffle(bitArray);

        return bitArray;
    }

    public void updateFitness(Solution solution) {
        solution.fitnessValue = graph.scoreSolution(solution);
    }


}
