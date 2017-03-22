import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 *
 * @noinspection Since15
 */
public class Solution implements Cloneable {


    public int fitnessValue = 0;


    public List<Integer> bitArray;

    /**
     * Construct with random solution
     */
    public Solution(List bitArray) {
        this.bitArray = bitArray;
    }

    /**
     * Swap vertexes on the n'the vertex with a zero and the Nth vertex with a one
     *
     * @param nthZero
     * @param nthOne
     */
    public void vertexSwap(int nthZero, int nthOne) {

        int zeroCount = 0;
        int iZero = 0;

        int oneCount = 0;
        int iOne = 0;

        for(int i = 0; i < bitArray.size(); i++) {
            // find the Nth zero
            if (zeroCount != nthZero && bitArray.get(i).intValue() == 0) {
                zeroCount++;
                iZero = i;
            }
            // find the Nth one
            else if (oneCount != nthOne && bitArray.get(i).intValue() == 1){
                oneCount++;
                iOne = i;
            }

            // Swap if both are found, and break out of the loop
            if(zeroCount == nthZero && oneCount == nthOne) {

                bitArray.set(iZero, 1);
                bitArray.set(iOne, 0);

                break;
            }
        }

    }

    @Override
    public Solution clone() {
        try {
            final Solution result = (Solution) super.clone();
            // copy fields that need to be copied here!
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
