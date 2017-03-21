/**
 * Created by Dennis on 21/03/2017.
 */
public class GeneticAlgorithm {

    public void run () {

        Graph graph = new Graph("Graph500.txt");

        Solution[] population = initializePopulation(1, 10);

    }

    public Solution[] initializePopulation (int populationSize, int nodes){

        Solution[] population = new Solution[populationSize];

        for (int i = 0; i < population.length; i++) {
            population[i] = new Solution(nodes);
        }

        return population;
    }
}
