import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 * @noinspection Since15
 */
public class Solution {


    public int fitnessValue;

    public int[] bitString;

    /**
     * Construct with random solution
     * @param nodes
     */
    public Solution(int nodes) {
        this.bitString = generateRandomSolution(nodes);
        this.fitnessValue = calculateFitness();
    }

    private int[] generateRandomSolution(int nodes) {

        bitString = new int[nodes];

        for (int i = 0; i < nodes; i++) {
            // alternate value between 0 and 1
            bitString[i] = ( (i & 1) == 0 ) ? 0 : 1;
        }

        // Implementing Fisher Yates shuffle
        // https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
        Random rnd = ThreadLocalRandom.current();
        for (int i = bitString.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = bitString[index];
            bitString[index] = bitString[i];
            bitString[i] = a;
        }

        return bitString;
    }

    private int calculateFitness() {
        //TODO calculate fitness
        return 1;
    }

}
