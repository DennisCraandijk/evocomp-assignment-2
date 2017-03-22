import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 * @noinspection Since15
 */
public class Solution {


    public int fitnessValue;

    public int[] bitArray;



    /**
     * Construct with random solution
     */
    public Solution(int[] bitArray, Graph graph) {
        this.bitArray = bitArray;
        this.fitnessValue = graph.scoreSolution(this);
    }

}
