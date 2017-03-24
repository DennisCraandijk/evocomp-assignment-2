import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Dennis on 21/03/2017.
 */
class Graph {

    int[][] nodes;

    Graph(String path) {

        initializeGraphFromFile(path);
    }

    private void initializeGraphFromFile(String path) {
        try {
            String[] lines = readFile(path).split("\\r?\\n");

            this.nodes = new int[lines.length][];

            for (String line : lines) {
                String[] data = line.split("\\s+");

                // use the data to build an nodes matrix
                // data[1] is the id of the vertex (starting from 1)
                // data[3] contains the amount of edges
                // everything from data[4] and higher contain edges

                int vertexId = Integer.parseInt(data[1]);
                int edgeCount = Integer.parseInt(data[3]);
                this.nodes[vertexId - 1] = new int[edgeCount];

                for (int j = 0; j < edgeCount; j++) {
                    // vertex ids start from 1, so -1 on all ids to build array
                    this.nodes[vertexId - 1][j] = Integer.parseInt(data[j + 4]) - 1;
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    private static String readFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }
}
