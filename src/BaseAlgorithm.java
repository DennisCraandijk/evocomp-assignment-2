import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dennis on 21/03/2017.
 */
class BaseAlgorithm {

    public Graph graph;

    public Solution bestSolution = null;

    //TODO make localOptima capacity variable
    public List<Solution> localOptima = new ArrayList<>(2500);

    public int functionEvaluations = 0;

    public BaseAlgorithm(Graph graph) {
        this.graph = graph;
    }

    public void updateFitness(Solution solution) {
        solution.fitness = evaluateSolution(solution);
    }

    public int evaluateSolution(Solution solution) {

        functionEvaluations++;

        int fitness = 0;
        // for all nodes
        for (int i = 0; i < this.graph.nodes.length; i++) {
            Integer color = solution.bitArray.get(i);

            // for all edges with equal color, increment fitness
            for (int j = 0; j < this.graph.nodes[i].length; j++) {
                if (color == solution.bitArray.get(this.graph.nodes[i][j])) {
                    fitness++;
                }
            }
        }

        return fitness;
    }

    public List<Integer> generateRandomBitArray(int nodes) {

        List<Integer> bitArray = new ArrayList<>(nodes);

        for (int i = 0; i < nodes; i++) {
            // alternate value between 0 and 1
            Integer color = ((i & 1) == 0) ? 0 : 1;
            bitArray.add(color);
        }

        Collections.shuffle(bitArray);

        return bitArray;
    }


    /**
     * Swaps a 0 and 1 on the string with incrementing index
     * Returns first improved solution
     * @param solution
     * @return
     */
    public Solution climbFirstImprovement(Solution solution) {
        int baseFitness = solution.fitness;

        for (int i = 0; i < (this.graph.nodes.length / 2); i++) {
            for (int j = 0; j < (this.graph.nodes.length / 2); j++) {


                //swap  0 and 1 colored vertexes in an incrementing way and check fitness
                solution.vertexSwap(i, j);
                updateFitness(solution);

                if (solution.fitness > baseFitness) {
                    return solution;
                }

                // swap back to original
                solution.vertexSwap(j, i);

            }
        }

        return null;
    }
}
