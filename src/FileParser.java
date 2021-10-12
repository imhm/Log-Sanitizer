import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileParser {

    private final Charset charset = StandardCharsets.US_ASCII;
    private final String ipPlaceholder = "IP_ADDRESS_";
    private final String uuidPlaceholder = "UUID_";
    private final String destDirectoryPath = "./Sanitized_Logs/";
    private Path destPath;
    private Path sourcePath;
    private ArrayList<String> fileIpAddressesToReplace = new ArrayList<>();
    private ArrayList<String> fileUuidToReplace = new ArrayList<>();

    public FileParser(Path sourcePath) {
        this.sourcePath = sourcePath;
        this.destPath = Paths.get(destDirectoryPath, sourcePath.getFileName().toString());
    }

    public void fileParser() throws Exception{
        // Sanitize file line by line and write the sanitized line to the sanitized file
        createDestFile();
        try (BufferedReader reader = Files.newBufferedReader(sourcePath, charset)) {
            try (BufferedWriter writer = Files.newBufferedWriter(destPath, charset)) {
                String line;
                // Sanitize line by line.
                while ((line = reader.readLine()) != null) {
                    // Extract IP
                    ArrayList<String> lineIpAddressToReplace = IpExtractor.extractIp(line);
                    fileIpAddressesToReplace.addAll(lineIpAddressToReplace);

                    // Extract UUID
                    ArrayList<String> lineUuidToReplace = UuidExtractor.extractUuid(line);
                    fileUuidToReplace.addAll(lineUuidToReplace);

                    // Remove duplicates from sanitized items Arraylist
                    removeDuplicateFromArrayList(fileIpAddressesToReplace);
                    removeDuplicateFromArrayList(fileUuidToReplace);

                    // Sanitize line
                    String sanitizedLine = sanitizeLine(line, lineIpAddressToReplace, fileIpAddressesToReplace , ipPlaceholder);
                    sanitizedLine = sanitizeLine(sanitizedLine, lineUuidToReplace, fileUuidToReplace , uuidPlaceholder);

                    writer.append(sanitizedLine);
                    writer.newLine();
                }
            } catch (IOException x) {
                System.err.format("IOException in BufferedWriter: %s%n", x);
            }

        } catch (IOException x) {
            System.err.format("IOException in BufferedReader: %s%n", x);
        }
    }

    private void createDestFile() throws Exception {

        File directory = new File(destDirectoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            Files.createFile(destPath);
        } catch (Exception e) {
            throw new Exception("Error creating sanitized file: " + e);
        }
    }


    //Remove duplicate IP address
    private void removeDuplicateFromArrayList(ArrayList arrayList) {
        // Create a new LinkedHashSet
        // Add the elements to set
        Set<String> set = new LinkedHashSet<>(arrayList);

        // Clear the list
        arrayList.clear();

        // add the elements of set
        // with no duplicates to the list
        arrayList.addAll(set);
    }

    /** Sanitizes the line
     *
     * @param line String to be sanitized.
     * @param lineArrayList Arraylist of items to be sanitized in the line.
     * @param fileArrayList Arraylist of items sanitized from the file.
     * @param placeholder Placeholder text
     * @return
     */
    private String sanitizeLine(String line, ArrayList<String> lineArrayList, ArrayList fileArrayList, String placeholder) {
        String sanitizedLine = line;
        for (String item : lineArrayList) {
            sanitizedLine = sanitizedLine.replace(item,placeholder + fileArrayList.indexOf(item));
        }
        return sanitizedLine;
    }

}
