import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class Population {

    public int generation;

    public Solution[] population;

    public Population(int populationSize, Graph graph) {
        this.generation = 0;
        this.population = initializePopulation(populationSize, graph);
    }

    public Solution[] initializePopulation (int populationSize, Graph graph){

        Solution[] population = new Solution[populationSize];

        for (int i = 0; i < population.length; i++) {
            population[i] = new Solution(generateRandomSolution(graph.nodes.length), graph);
        }

        return population;
    }

    private int[] generateRandomSolution(int nodes) {

        int[] bitArray = new int[nodes];

        for (int i = 0; i < nodes; i++) {
            // alternate value between 0 and 1
            bitArray[i] = ( (i & 1) == 0 ) ? 0 : 1;
        }

        // Implementing Fisher Yates shuffle
        // https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
        Random rnd = ThreadLocalRandom.current();
        for (int i = bitArray.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = bitArray[index];
            bitArray[index] = bitArray[i];
            bitArray[i] = a;
        }

        return bitArray;
    }


}
