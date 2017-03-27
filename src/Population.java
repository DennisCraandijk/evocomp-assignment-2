import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 22/03/2017.
 */
class Population {

    int iterations;

    List<Solution> population;

    Population(int populationSize) {
        this.iterations = 0;
        this.population = new ArrayList<>(populationSize);
    }

    void addSolution(Solution solution) {
        population.add(solution);
    }

    boolean contains(Solution solution){
    	for (int i = 0; i < population.size(); i++) {
			if(population.get(i).bitArray.equals(solution.bitArray)){
				return true;
			}
		}
    	return false;
    }
    
    
    void replaceSolution(Solution solution, int index){
    	population.set(index, solution);
    }
}
