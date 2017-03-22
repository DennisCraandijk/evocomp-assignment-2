import com.sun.org.apache.bcel.internal.generic.POP;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 */
public class PartitioningAlgorithm {

    public void run(int populationSize, String algorithm) {

        Graph graph = new Graph("Graph500.txt");

        Population population = new Population(populationSize, graph);


        switch (algorithm) {
            case "GLS":
                break;
            case "ILS":
                break;
            case "MLS":
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
        }

        return;
    }
}
