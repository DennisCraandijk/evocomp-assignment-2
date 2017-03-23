import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 *
 * @noinspection Since15
 */
public class Solution implements Cloneable {


    public int fitness;


    public List<Integer> bitArray;

    public List<List<Integer>> colorIndex;


    /**
     * Construct with random solution
 * @param bitArray
     */
    public Solution(List<Integer> bitArray) {
        this(bitArray, 0);
    }

    /**
     * Construct with random solution
     */
    public Solution(List<Integer> bitArray, int fitness) {
        this.bitArray = bitArray;
        this.fitness = fitness;
        this.colorIndex = constructColorIndex(bitArray);
    }

    private List<List<Integer>> constructColorIndex (List<Integer> bitArray){

        List<List<Integer>> colorIndex = new ArrayList<>(2);

        List<Integer> verticesColored0 = new ArrayList<>(bitArray.size() / 2);
        List<Integer> verticesColored1 = new ArrayList<>(bitArray.size() / 2);

        for(int i = 0; i < bitArray.size(); i++) {
            if(bitArray.get(i).intValue() == 0) {
                verticesColored0.add(i);
            } else {
                verticesColored1.add(i);
            }
        }

        colorIndex.add(0, verticesColored0);
        colorIndex.add(1, verticesColored1);

        return colorIndex;
    }

    /**
     * Swap vertexes on the n'the vertex with a zero and the Nth vertex with a one
     * @param iZero
     * @param iOne
     */
    public void vertexSwap(int iZero, int iOne) {

        // swap vertices in the colorIndex
        int vertexId0 = colorIndex.get(0).get(iZero);
        int vertexId1 = colorIndex.get(1).get(iOne);

        colorIndex.get(0).set(iZero, vertexId1);
        colorIndex.get(1).set(iOne, vertexId0);

        // swap vertices in the bitArray
        bitArray.set(vertexId0, 1);
        bitArray.set(vertexId1, 0);

        return;

    }


    /*public void vertexSwap(int nthZero, int nthOne) {

        //TODO change to using 2 dimensional color array
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

    }*/

    @Override
    public Solution clone() {
        try {
            final Solution result = (Solution) super.clone();
            // copy fields that need to be copied here!
            result.bitArray = new ArrayList<>(bitArray);
            result.fitness = fitness;
            result.colorIndex = new ArrayList<>(2);
            result.colorIndex.add(0, new ArrayList<>(colorIndex.get(0)));
            result.colorIndex.add(1, new ArrayList<>(colorIndex.get(1)));
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
