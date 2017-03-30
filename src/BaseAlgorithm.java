import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Dennis on 21/03/2017.
 */
class BaseAlgorithm {

    Graph graph;

    String localSearchType;
    boolean skipConflictlessNodes;

    int fullFunctionEvaluations = 0;
    int partialFunctionEvaluations = 0;
    int climbedVertexSwaps = 0;

    //In the paper, on the end of page 388, it is argued these are the best settings for the tabuTenure
    double tabuTenureAlfa = 0.6;
    int tabuTenureA = 10;

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
    BaseAlgorithm(Graph graph, String localSearchType, boolean skipConflictlessNodes, int maxLocalOptima, int maxCPUTime) {
        this.startTime = System.currentTimeMillis();
        this.maxCPUTime = maxCPUTime;

        this.graph = graph;
        this.localSearchType = localSearchType;
        this.skipConflictlessNodes = skipConflictlessNodes;

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

        fullFunctionEvaluations++;

        int totalColorConflicts = 0;
        // for all nodes
        for (int i = 0; i < this.graph.nodes.length; i++) {

            // get amount of color conflicts for this node
            int colorConflicts = getColorConflicts(solution, i);

            // remember this
            try {
                solution.bitColorConflictCount.set(i, colorConflicts);
            } catch (IndexOutOfBoundsException e) {
                solution.bitColorConflictCount.add(i, colorConflicts);
            }

            // increase total
            totalColorConflicts = totalColorConflicts + colorConflicts;
        }

        // only return the edges, not the nodes
        return totalColorConflicts / 2;
    }

    /**
     * get amount edges with color-conflict for a specific node
     *
     * @param solution
     * @param nodeId
     * @return
     */
    private int getColorConflicts(Solution solution, int nodeId) {

        int colorConflicts = 0;

        int color = solution.bitArray.get(nodeId);

        // for all edges with unequal color, increment fitness
        for (int i = 0; i < this.graph.nodes[nodeId].length; i++) {
            // if node  has different color
            if (color != solution.bitArray.get(this.graph.nodes[nodeId][i])) {
                colorConflicts++;
            }
        }

        return colorConflicts;
    }

    /**
     * Hill climb the current solution, till no better solution is found
     *
     * @param solution
     * @return
     */
    Solution hillClimb(Solution solution) {
        // climb till no improvement is found
        while (true) {

            Solution climbedSolution = null;

            if(this.localSearchType=="VSN") {
                climbedSolution = vertexSwapNeighbourhoodSearch(solution);
            } else if (this.localSearchType=="TS") {
                climbedSolution = tabooSearch(solution);
            }
            if (climbedSolution == null) return solution;
            climbedVertexSwaps++;
            solution = climbedSolution;

            /**
             * BEGIN Double checks, can be commented out if everything works ok
             */
            /*int evaluation = evaluateSolution(solution);
            if (solution.fitness != evaluation) {
                System.out.print("\n\n Error \n\n");
            }

            for(int i = 0; i < solution.bitColorConflictCount.size(); i++) {
                conflictCheck(solution, i);
            }*/
            /**
             * END Double checks, can be commented out if everything works ok
             */

        }
    }


    /**
     * Swaps a 0 and 1 on the string with incrementing index
     * Returns first improved solution
     *
     * @param solution
     * @return
     */
    Solution vertexSwapNeighbourhoodSearch(Solution solution) {

        // iterate on all nodes with color 0 and for all these nodes iterate all nodes with color 1
        for (int i = 0; i < (this.graph.nodes.length / 2); i++) {
            for (int j = 0; j < (this.graph.nodes.length / 2); j++) {

                Integer nodeId1 = solution.colorIndex.get(0).get(i);
                Integer nodeId2 = solution.colorIndex.get(1).get(j);

                // heuristic from article, skip if current node has no conflicts
                if (skipConflictlessNodes && (solution.bitColorConflictCount.get(nodeId1) == 0 || solution.bitColorConflictCount.get(nodeId2) == 0)) continue;

                // check if this swap would lower the fitness
                int swapPotential = evaluateSwapPotential(solution, nodeId1, nodeId2);

                // if not so, continue to next nodes
                if (swapPotential >= 0) {
                    continue;
                }

                // if so, perform the swap
                // and update the fitness and all the affected nodes their color conflict count
                solution.vertexSwap(i, j);
                solution = updateConflictCountAfterSwap(solution, nodeId1, nodeId2);
                solution.fitness = solution.fitness + swapPotential;

                return solution;

            }
        }

        return null;
    }

    Solution tabooSearch(Solution solution) {

        int bestSwapPotential = 9999; //start with very high number, so the first hit will always be an improvement
        int bestSwapNodeId1 = 0;
        int bestSwapNodeI = 0;
        int bestSwapNodeId2 = 0;
        int bestSwapNodeJ = 0;

        // iterate on all nodes with color 0 and for all these nodes iterate all nodes with color 1
        for (int i = 0; i < (this.graph.nodes.length / 2); i++) {
            for (int j = 0; j < (this.graph.nodes.length / 2); j++) {
                Integer nodeId1 = solution.colorIndex.get(0).get(i);
                Integer nodeId2 = solution.colorIndex.get(1).get(j);

                // heuristic from article, skip if current node has no conflicts
                if (skipConflictlessNodes && (solution.bitColorConflictCount.get(nodeId1) == 0 || solution.bitColorConflictCount.get(nodeId2) == 0)) continue;

                // check if this swap would lower the fitness
                int swapPotential = evaluateSwapPotential(solution, nodeId1, nodeId2);

                // do not do a taboo move, unless it is an improvement over the best solution so far
                if ((isTabooMove(solution, nodeId1) || isTabooMove(solution, nodeId2)) && swapPotential >= 0) {
                    continue;
                }

                // if swap potential is better or equal (see article) we keep it
                if(swapPotential <= bestSwapPotential) {
                    bestSwapPotential = swapPotential;
                    bestSwapNodeId1 = nodeId1;
                    bestSwapNodeI = i;
                    bestSwapNodeId2 = nodeId2;
                    bestSwapNodeJ = j;
                }
            }
        }

        // if no improvement is found
        if(bestSwapPotential==9999) return null;

        //otherwise swap and update tabu list
        makeTabu(solution, bestSwapNodeId1);
        makeTabu(solution, bestSwapNodeId2);

        // if so, perform the swap
        // and update the fitness and all the affected nodes their color conflict count
        solution.vertexSwap(bestSwapNodeI, bestSwapNodeJ);
        solution = updateConflictCountAfterSwap(solution, bestSwapNodeId1, bestSwapNodeId2);
        solution.fitness = solution.fitness + bestSwapPotential;

        return solution;
    }

    boolean isTabooMove(Solution solution, int nodeId) {
        return solution.bitTabooUntil.get(nodeId) > localOptima.size();
    }

    private void makeTabu(Solution solution, int nodeId) {
        Random rand = new Random();
        int tabuTenure = this.localOptima.size () + rand.nextInt(tabuTenureA) + (int) (tabuTenureAlfa * solution.fitness);

        solution.bitTabooUntil.set(nodeId, tabuTenure);
    }

    /**
     * How much would the fitness increase if these nodes swap
     *
     * @param solution
     * @param nodeId1
     * @param nodeId2
     * @return
     */
    private int evaluateSwapPotential(Solution solution, int nodeId1, int nodeId2) {

        this.partialFunctionEvaluations++;

        int currentColorConflictsNode1 = solution.bitColorConflictCount.get(nodeId1);
        int currentColorConflictsNode2 = solution.bitColorConflictCount.get(nodeId2);

        // if this node changes color, the new amount of conflicts is the total amount of connected nodes minus the current conflicting nodes
        int newColorConflictsNode1 = this.graph.nodes[nodeId1].length - currentColorConflictsNode1;
        int newColorConflictsNode2 = this.graph.nodes[nodeId2].length - currentColorConflictsNode2;

        // if the swapped node is among these connected nodes, the color conflict obviously stays the same for this name, so increment by 1
        if (areConnectedNodes(nodeId1, nodeId2)) {
            newColorConflictsNode1++;
            newColorConflictsNode2++;
        }

        return (newColorConflictsNode1 - currentColorConflictsNode1) + (newColorConflictsNode2 - currentColorConflictsNode2);
    }

    /**
     * updates the color conflict count of the swapped nodes and all their connected nodes
     *
     * @param solution
     * @param nodeId1
     * @param nodeId2
     * @return
     */
    private Solution updateConflictCountAfterSwap(Solution solution, Integer nodeId1, Integer nodeId2) {
        int[] swappedNodeIds = new int[2];
        swappedNodeIds[0] = nodeId1;
        swappedNodeIds[1] = nodeId2;

        // iterate over both swapped nodes
        for (int i = 0; i < swappedNodeIds.length; i++) {

            //take the node id and the swapped node id
            int primaryNodeId = swappedNodeIds[i];
            int swappedNodeId = swappedNodeIds[1 - i];

            int primaryNodeColor = solution.bitArray.get(primaryNodeId);

            // reset the color conflict count for this node
            solution.bitColorConflictCount.set(primaryNodeId, 0);

            // for all connected nodes with unequal color, increment conflict count
            for (int j = 0; j < this.graph.nodes[primaryNodeId].length; j++) {

                int connectedNode = this.graph.nodes[primaryNodeId][j];

                // if the iterated connected node is actually the swapped node, only increase the color conflicts and continue
                if (connectedNode == swappedNodeId) {
                    solution.bitColorConflictCount.set(primaryNodeId, (solution.bitColorConflictCount.get(primaryNodeId) + 1));
                    continue;
                }

                // otherwise, adjust the color conflicts for the primary node and the connected node
                if (primaryNodeColor != solution.bitArray.get(connectedNode)) {
                    solution.bitColorConflictCount.set(primaryNodeId, (solution.bitColorConflictCount.get(primaryNodeId) + 1));
                    solution.bitColorConflictCount.set(connectedNode, (solution.bitColorConflictCount.get(connectedNode) + 1));
                } else {
                    solution.bitColorConflictCount.set(connectedNode, (solution.bitColorConflictCount.get(connectedNode) - 1));
                }

            }
        }

        return solution;
    }

    /**
     * returns if these nodes are connected
     *
     * @param nodeId1
     * @param nodeId2
     * @return
     */
    private boolean areConnectedNodes(int nodeId1, int nodeId2) {

        // for all edges with unequal color, increment fitness
        for (int i = 0; i < this.graph.nodes[nodeId1].length; i++) {
            // if node  has different color
            if (this.graph.nodes[nodeId1][i] == nodeId2) return true;
        }

        return false;
    }


    /**
     * Generate bitArray as initial solution
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

    public boolean conflictCheck(Solution solution, int nodeId) {
        int colorConflicts = solution.bitColorConflictCount.get(nodeId);
        int realColorConflicts = getColorConflicts(solution, nodeId);

        if (realColorConflicts == colorConflicts) {
            return true;
        } else {
            System.out.print("\n\n Error \n\n");
            return false;
        }
    }
}
