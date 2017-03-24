import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dennis on 21/03/2017.
 */
class BaseAlgorithm {

    Graph graph;

    int functionEvaluations = 0;

    Solution bestSolution = null;

    List<Solution> localOptima;
    private int maxLocalOptima;
    private int maxCPUTime;

    private long startTime;

    /**
     * Construct baseAlgorithm
     *
     * @param graph
     * @param maxLocalOptima
     */
    BaseAlgorithm(Graph graph, int maxLocalOptima, int maxCPUTime) {
        this.startTime = System.currentTimeMillis();
        this.maxCPUTime = maxCPUTime;

        this.graph = graph;

        this.maxLocalOptima = maxLocalOptima;
        this.localOptima = new ArrayList<>(maxLocalOptima);
    }

    /**
     * Partition the graph
     *
     * @return
     */
    public Solution partition() {
        return null;
    }

    /**
     * Amount of edges with color-conflict
     *
     * @param solution
     * @return fitnessValue
     */
    int evaluateSolution(Solution solution) {

        functionEvaluations++;

        int colorConflicts = 0;
        // for all nodes
        for (int i = 0; i < this.graph.nodes.length; i++) {
            colorConflicts = colorConflicts + getColorConflicts(solution, i);
        }

        // only return the edges, not the nodes
        return colorConflicts / 2;
    }

    // overloaded
    private int getColorConflicts(Solution solution, int nodeId) {
        return getColorConflicts(solution, nodeId, -1);
    }

    /**
     * get amount edges with color-conflict for a specific node
     *
     * @param solution
     * @param nodeId
     * @param excludeNodeId
     * @return
     */
    private int getColorConflicts(Solution solution, int nodeId, int excludeNodeId) {

        int colorConflicts = 0;

        int color = solution.bitArray.get(nodeId);

        // for all edges with unequal color, increment fitness
        for (int i = 0; i < this.graph.nodes[nodeId].length; i++) {
            // if node  has different color
            if (color != solution.bitArray.get(this.graph.nodes[nodeId][i])) {
                if (excludeNodeId == -1 || excludeNodeId != this.graph.nodes[nodeId][i]) colorConflicts++;
            }
        }

        return colorConflicts;
    }

    /**
     * how much the fitnessvalue will change if a swap is done
     *
     * @param solution
     * @param iZero
     * @param iOne
     * @return
     */
    private int evaluateSwapPotential(Solution solution, int iZero, int iOne) {

        // TODO hacky workaround, otherwise the score will divert after some runs
        solution = solution.clone();

        int currentColorConflicts = getColorConflicts(solution, solution.colorIndex.get(0).get(iZero)) + getColorConflicts(solution, solution.colorIndex.get(1).get(iOne), solution.colorIndex.get(0).get(iZero));

        solution.vertexSwap(iZero, iOne);

        int newColorConflicts = getColorConflicts(solution, solution.colorIndex.get(0).get(iZero)) + getColorConflicts(solution, solution.colorIndex.get(1).get(iOne), solution.colorIndex.get(0).get(iZero));

        // the new amount of conflicts minus the old amount of conflicts
        return newColorConflicts - currentColorConflicts;
    }


    /**
     * Generate bitarray as initial solultion
     *
     * @param nodes
     * @return
     */
    List<Integer> generateRandomBitArray(int nodes) {

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
     *
     * @param solution
     * @return
     */
    Solution climbFirstImprovement(Solution solution) {

        for (int i = 0; i < (this.graph.nodes.length / 2); i++) {
            for (int j = 0; j < (this.graph.nodes.length / 2); j++) {


                // check the change in fitness if a swap would happen
                int swapPotential = evaluateSwapPotential(solution, i, j);

                // if the fitness can improve, do a swap
                if (swapPotential < 0) {

                    //solution.fitness = solution.fitness + swapPotential;
                    solution.vertexSwap(i, j);
                    solution.fitness = solution.fitness + swapPotential;

                    // check if the new fitness value is correct with this code
                    /*int evaluation = evaluateSolution(solution);
                    if (solution.fitness != evaluation) {
                        System.out.print("\n\n Error \n\n");
                    }*/

                    return solution;
                }

            }
        }

        return null;
    }

    void saveNewOptimum(Solution solution) {
        //add to local optima and best
        Solution localOptimum = solution.clone();
        this.localOptima.add(localOptimum);
        System.out.print("Local optimum " + this.localOptima.size() + " found: " + solution.fitness + "\n");

        if (this.bestSolution == null || solution.fitness < this.bestSolution.fitness) {
            this.bestSolution = solution.clone();
        }
    }

    int getCPUTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    boolean shouldStop() {
        boolean stopCPU = (this.maxCPUTime != 0 && getCPUTime() > this.maxCPUTime);

        boolean stopLocalOptima = (this.maxLocalOptima != 0 && this.localOptima.size() >= this.maxLocalOptima);

        return (stopCPU || stopLocalOptima);
    }
}
