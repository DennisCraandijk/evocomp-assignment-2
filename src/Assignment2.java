/**
 * Created by Dennis on 21/03/2017.
 */
public class Assignment2 {

    public static void main(String[] args) {

//        String algorithmName = "MLS";
//        String algorithmName = "ILS";
        String algorithmName = "GLS";

        // set to 0 for infinite
        int maxLocalOptima = 10;

        // set to 0 for infinite
        int maxCPUTime = 0;

        run(algorithmName, maxLocalOptima, maxCPUTime);
    }

    private static void run(String algorithmName, int maxLocalOptima, int maxCPUTime) {

        Graph graph = new Graph("Graph500.txt");

        BaseAlgorithm algorithm;

        switch (algorithmName) {
            case "GLS":
                algorithm = new GLS(graph, maxLocalOptima, maxCPUTime, 2);
                break;
            case "ILS":
                algorithm = new ILS(graph, maxLocalOptima, maxCPUTime, 6);
                break;
            case "MLS":
                algorithm = new MLS(graph, maxLocalOptima, maxCPUTime);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algorithmName);
        }

        Solution best = algorithm.partition();
        int time = algorithm.getCPUTime();
        int functionEvaluations = algorithm.fullFunctionEvaluations;

        System.out.print("Best fitness found:" + algorithm.bestSolution.fitness + "\n");
    }
}
