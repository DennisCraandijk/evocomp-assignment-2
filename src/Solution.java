import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 21/03/2017.
 *
 * @noinspection Since15
 */
public class Solution implements Cloneable {


    int fitness;


    List<Integer> bitArray;
    List<Integer> bitColorConflictCount;
    List<Integer> bitTabooUntil;

    List<List<Integer>> colorIndex;


    /**
     * Construct with random solution
 * @param bitArray
     */
    Solution(List<Integer> bitArray) {
        this(bitArray, 0);
    }

    /**
     * Construct with random solution
     */
    Solution(List<Integer> bitArray, int fitness) {
        this.bitArray = bitArray;
        this.bitColorConflictCount = new ArrayList<>(bitArray.size());
        this.bitTabooUntil = new ArrayList<>(bitArray.size());
        this.fitness = fitness;
        this.colorIndex = constructColorIndex(bitArray);

        for(int i = 0; i < bitArray.size(); i++) {
            this.bitTabooUntil.add(i, 0);
        }
    }

    private List<List<Integer>> constructColorIndex (List<Integer> bitArray){

        List<List<Integer>> colorIndex = new ArrayList<>(2);

        List<Integer> verticesColored0 = new ArrayList<>(bitArray.size() / 2);
        List<Integer> verticesColored1 = new ArrayList<>(bitArray.size() / 2);

        for(int i = 0; i < bitArray.size(); i++) {
            if(bitArray.get(i) == 0) {
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
    void vertexSwap(int iZero, int iOne) {

        // swap vertices in the colorIndex
        int nodeId0 = colorIndex.get(0).get(iZero);
        int nodeId1 = colorIndex.get(1).get(iOne);

        colorIndex.get(0).set(iZero, nodeId1);
        colorIndex.get(1).set(iOne, nodeId0);

        // swap vertices in the bitArray
        bitArray.set(nodeId0, 1);
        bitArray.set(nodeId1, 0);

    }

    @Override
    public Solution clone() {
        try {
            final Solution result = (Solution) super.clone();
            // copy fields that need to be copied here!
            result.bitArray = new ArrayList<>(bitArray);
            result.bitColorConflictCount = new ArrayList<>(bitColorConflictCount);
            result.bitTabooUntil = new ArrayList<>(bitTabooUntil);
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
