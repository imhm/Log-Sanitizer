import org.apache.commons.io.FilenameUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileParser {

    private final Charset charset = StandardCharsets.US_ASCII;
    private final String ipPlaceholder = "IP_ADDRESS_";
    private final String uuidPlaceholder = "UUID_";
    private final String destDirectoryPath = "./Sanitized_Logs/";
    private ArrayList<ArrayList<String>> customData;
    private Path sourcePath;
    private Path destPath;
    private Path summaryPath;
    private BufferedReader sourceReader;
    private ArrayList<String> fileIpAddressesToReplace = new ArrayList<>();
    private ArrayList<String> fileUuidToReplace = new ArrayList<>();

    public FileParser(Path sourcePath, CustomSanitizerParser customSanitizerParser) {
        this.sourcePath = sourcePath;
        File file = new File(sourcePath.toString());
        String fileName = file.getName();
        String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
        this.destPath = Paths.get(destDirectoryPath, fileNameWithOutExt);
        this.summaryPath = Paths.get(destDirectoryPath, "Summary - " + fileNameWithOutExt);
        customData = customSanitizerParser.getCustomData();
    }

    public FileParser(Path sourcePath, CustomSanitizerParser customSanitizerParser, BufferedReader sourceReader) {
        this.sourceReader = sourceReader;
        File file = new File(sourcePath.toString());
        String fileName = file.getName();
        String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
        this.destPath = Paths.get(destDirectoryPath, fileNameWithOutExt);
        this.summaryPath = Paths.get(destDirectoryPath, "Summary - " + fileNameWithOutExt);
//        this.summaryPath = Paths.get(destDirectoryPath, "Summary - " + sourcePath.getFileName().toString());
//        this.destPath = Paths.get(destDirectoryPath, sourcePath.getFileName().toString());
        customData = customSanitizerParser.getCustomData();
    }

    public void fileParser() throws Exception{
        // Sanitize file line by line and write the sanitized line to the sanitized file
        createDestFile();
        // Write sanitized file
        sanitizeFile();
        // Write change summary file
        writeChangeSummary(fileIpAddressesToReplace, ipPlaceholder);
        writeChangeSummary(fileUuidToReplace, uuidPlaceholder);
        writeCustomChangeSummary();
    }

    private void writeCustomChangeSummary() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(summaryPath, charset, StandardOpenOption.APPEND)) {
            for (ArrayList<String> arrayList : customData) {
                String output = arrayList.get(1) + " : " + arrayList.get(0);
                writer.append(output);
                writer.newLine();
            }
        }
    }

    private void sanitizeFile() {
        try {
            BufferedReader reader = sourceReader;

            if (sourceReader == null) {
                reader = Files.newBufferedReader(sourcePath, charset);
            }

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

                    // Sanitize line - IP and UUIDs
                    String sanitizedLine = sanitizeLine(line, lineIpAddressToReplace, fileIpAddressesToReplace , ipPlaceholder);
                    sanitizedLine = sanitizeLine(sanitizedLine, lineUuidToReplace, fileUuidToReplace , uuidPlaceholder);

                    // Sanitize line - custom sanitization
                    for (ArrayList<String> arrayList : customData) {
                        sanitizedLine = sanitizedLine.replace(arrayList.get(0), arrayList.get(1));
                    }

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

    private void writeChangeSummary(ArrayList<String> fileArrayList, String placeholder) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(summaryPath, charset, StandardOpenOption.APPEND)) {
            for (String item : fileArrayList) {
                String output = placeholder + fileArrayList.indexOf(item) + " : " + item;
                writer.append(output);
                writer.newLine();
            }
        }
    }

    private void createDestFile() throws Exception {

        try {
            File destFile = new File(destPath.toString());
            File parent = destFile.getParentFile();
            if (!parent.isDirectory()
                    && !parent.mkdirs()) {    // if parent file is not a dir & unable ot make parent dir
                throw new IOException("Failed to create directory " + parent);
            }
            Files.createFile(destPath);
            Files.createFile(summaryPath);
        } catch (Exception e) {
            throw new Exception("Error creating new files: " + e);
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
     * @param placeholder Placeholder text.
     * @return Sanitized line.
     */
    private String sanitizeLine(String line, ArrayList<String> lineArrayList, ArrayList fileArrayList, String placeholder) {
        String sanitizedLine = line;
        for (String item : lineArrayList) {
            sanitizedLine = sanitizedLine.replace(item,placeholder + fileArrayList.indexOf(item));
        }
        return sanitizedLine;
    }

}
