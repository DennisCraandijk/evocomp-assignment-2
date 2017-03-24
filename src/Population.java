import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 22/03/2017.
 */
class Population {

    int generation;

    List<Solution> population;

    Population(int populationSize) {
        this.generation = 0;
        this.population = new ArrayList<>(populationSize);
    }

    void addSolution(Solution solution) {
        population.add(solution);
    }


}
