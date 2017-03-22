import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dennis on 21/03/2017.
 */
public class BaseAlgorithm {

    public Solution best;

    public Solution[] localOptima;

    public List generateRandomSolution(int nodes) {

        ArrayList<Integer> bitArray = new ArrayList<>(nodes);

        for (int i = 0; i < nodes; i++) {
            // alternate value between 0 and 1
            Integer color = ( (i & 1) == 0 ) ? 0 : 1;
            bitArray.add(color);
        }

        Collections.shuffle(bitArray);

        return bitArray;
    }
}
