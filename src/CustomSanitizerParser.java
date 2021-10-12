import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CustomSanitizerParser {
    private final Charset charset = StandardCharsets.US_ASCII;

    private Path filePath;
    private ArrayList<ArrayList<String>> customData = new ArrayList<>();

    public CustomSanitizerParser (Path filePath) throws Exception{
        if (filePath != null) {
            this.filePath = filePath;
            retrieveCustomData();
        }
    }

    private void retrieveCustomData() throws Exception{
        try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
            try {
                // Get delimiter
                String[] temp_array = reader.readLine().split("'");
                String delimiter = temp_array[1];

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data_split_array = line.split(delimiter);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(data_split_array[0]); // Target string to be replaced
                    arrayList.add(data_split_array[1]); // Placeholder
                    customData.add(arrayList);
                }
            } catch (Exception e) {
                throw new Exception("Error reading custom sanitizer file: " + e);
            }
        } catch (IOException x) {
            System.err.format("IOException in BufferedReader: %s%n", x);
        }
    }

    public ArrayList<ArrayList<String>> getCustomData() {
        return customData;
    }
}
