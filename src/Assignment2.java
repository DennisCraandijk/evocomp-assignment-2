/**
 * Created by Dennis on 21/03/2017.
 */
public class Assignment2 {

    public static void main(String[] args) {
        BaseAlgorithm GA = new BaseAlgorithm();


        run("MLS");
    }

    public static void run(String algorithmName) {

        Graph graph = new Graph("Graph500.txt");


        switch (algorithmName) {
            case "GLS":
                GLS gls = new GLS(1000);
                gls.partition(graph);
                break;
            case "ILS":
                break;
            case "MLS":
                MLS mls = new MLS();
                mls.partition(graph);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algorithmName);
        }
    }
}
