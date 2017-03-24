import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class Population {

    public int generation;

    public List<Solution> population;

    public Population(int populationSize) {
        this.generation = 0;
        this.population = new ArrayList<>(populationSize);
    }

    public void addSolution(Solution solution) {
        population.add(solution);
    }


}
