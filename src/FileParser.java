import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileParser {

    private ArrayList<String> fileIpAddressesToReplace = new ArrayList<>();
    private final Charset charset = StandardCharsets.US_ASCII;
    private Path destPath;
    private Path sourcePath;

    public FileParser(Path sourcePath, Path destPath) {
        this.sourcePath = sourcePath;
        this.destPath = destPath;
    }

    public void fileParser() {
        // Sanitize file line by line and write the sanitized line to the sanitized file
        if (!createDestFile()) return;
        try (BufferedReader reader = Files.newBufferedReader(sourcePath, charset)) {
            try (BufferedWriter writer = Files.newBufferedWriter(destPath, charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ArrayList<String> lineIpAddressToReplace = StringSanitizer.stringSanitizer(line);
                    fileIpAddressesToReplace.addAll(lineIpAddressToReplace);
                    String sanitizedLine = sanitizeLine(line, lineIpAddressToReplace);
                    writer.append(sanitizedLine);
                    writer.newLine();
                    removeDuplicateIp();
                }
            } catch (IOException x) {
                System.err.format("IOException in BufferedWriter: %s%n", x);
            }

        } catch (IOException x) {
            System.err.format("IOException in BufferedReader: %s%n", x);
        }
    }

    private boolean createDestFile() {
        try {
            Files.createFile(destPath);
        } catch (Exception e) {
            System.out.println("Error creating sanitized file: " + e);
            return false;
        }
        return true;
    }


    //Remove duplicate IP address
    private void removeDuplicateIp() {
        // Create a new LinkedHashSet
        // Add the elements to set
        Set<String> set = new LinkedHashSet<>(fileIpAddressesToReplace);

        // Clear the list
        fileIpAddressesToReplace.clear();

        // add the elements of set
        // with no duplicates to the list
        fileIpAddressesToReplace.addAll(set);
    }

    private String sanitizeLine(String line, ArrayList<String> lineIpAddressToReplace) {
        String sanitizedLine = line;
        for (String ip : lineIpAddressToReplace) {
            sanitizedLine = sanitizedLine.replace(ip,"IP_ADDRESS_" + fileIpAddressesToReplace.indexOf(ip));
        }
        return sanitizedLine;
    }

}
