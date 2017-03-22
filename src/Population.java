import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 22/03/2017.
 */
public class Population {

    public int generation;

    public List population;

    public Population() {
        this.generation = 0;
    }

    public void addSolution(Solution solution) {
        population.add(solution);
    }


}
