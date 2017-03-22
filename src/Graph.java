import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Dennis on 21/03/2017.
 */
public class Graph {

    public int[][] graph;

    public Graph(String path) {

        initializeGraphFromFile(path);
    }

    private void initializeGraphFromFile(String path) {
        try {
            String[] lines = readFile(path).split("\\r?\\n");

            this.graph = new int[lines.length][];

            for (int i = 0; i < lines.length; i++) {
                String[] data = lines[i].split("\\s+");

                // use the data to build an graph matrix
                // data[1] is the id of the vertex (starting from 1)
                // data[3] contains the amount of edges
                // everything from data[4] and higher contain edges

                int vertexId = Integer.parseInt(data[1]);
                int edgeCount = Integer.parseInt(data[3]);
                this.graph[vertexId - 1] = new int[edgeCount];

                for (int j = 0; j < edgeCount; j++) {
                    // vertex ids start from 1, so -1 on all ids to build array
                    this.graph[vertexId - 1][j] = Integer.parseInt(data[j + 4]) - 1;
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
    static String readFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        }
    }
}
