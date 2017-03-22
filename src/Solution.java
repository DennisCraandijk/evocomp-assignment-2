import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 * @noinspection Since15
 */
public class Solution implements Cloneable {


    public int fitnessValue;

    public List<Integer> bitArray;

    /**
     * Construct with random solution
     */
    public Solution(List bitArray, Graph graph) {
        this.bitArray = bitArray;
        this.fitnessValue = graph.scoreSolution(this);
    }

    /**
     * Swap vertexes on the n'the vertex with a zero and the Nth vertex with a one
     * @param nthZero
     * @param nthOne
     */
    public void vertexSwap(int nthZero, int nthOne) {

        // increment i and count the amount of 0's till the nth zero
        int i = 0;
        int zeroCount = 0;
        while(zeroCount != nthZero) {
            if(bitArray.get(i).intValue() == 0) {
                zeroCount++;
            }
            i++;
        }

        // and set this bit to one
        bitArray.set(i, 1);

        // vice versa for 0
        int j = 0;
        int oneCount = 0;
        while(oneCount != nthOne) {
            if(bitArray.get(j).intValue() == 0) {
                oneCount++;
            }
            j++;
        }

        bitArray.set(j, 0);



    }

    @Override public Solution clone() {
        try {
            final Solution result = (Solution) super.clone();
            // copy fields that need to be copied here!
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }

}
