/**
 * Created by Dennis on 21/03/2017.
 */
public class Assignment2 {

    public static void main(String[] args) {

//        String algorithmName = "MLS";
        String algorithmName = "ILS";
//        String algorithmName = "GLS";

        // set to 0 for infinite
        int maxLocalOptima = 100;

        run(algorithmName, maxLocalOptima);
    }

    public static void run(String algorithmName, int maxLocalOptima) {

        Graph graph = new Graph("Graph500.txt");

        switch (algorithmName) {
            case "GLS":
                GLS gls = new GLS(graph, 1000);
                gls.partition();
                break;
            case "ILS":
                ILS ils = new ILS(graph, 5);
                ils.partition(maxLocalOptima);
                break;
            case "MLS":
                MLS mls = new MLS(graph);
                mls.partition(maxLocalOptima);

                System.out.print("Best fitness found:" + mls.bestSolution.fitness + "\n");
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algorithmName);
        }
    }
}
