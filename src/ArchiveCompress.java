import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ArchiveCompress {
    public static void archiveFileParser(Path sourcePath, CustomSanitizerParser customSanitizerParser,
                                         CommandLineArgumentParser.FILE_EXTENSION fileExtension) throws Exception {
        File targetDir = new File("./");

        switch (fileExtension) {
        case ZIP:
            // Extract .zip
            try (InputStream fi = Files.newInputStream(sourcePath);
                 InputStream bi = new BufferedInputStream(fi);
                 ArchiveInputStream i = new ZipArchiveInputStream(bi);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(i))) {
                extractArchiveEntry(targetDir, i, reader, customSanitizerParser);
            } catch (Exception e) {
                throw new Exception("Unable to extract .zip" + e);
            }
            break;
        case TAR:
            // Extract .tar
            try (InputStream fi = Files.newInputStream(sourcePath);
                 InputStream bi = new BufferedInputStream(fi);
                 ArchiveInputStream i = new TarArchiveInputStream(bi);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(i))) {
                extractArchiveEntry(targetDir, i, reader, customSanitizerParser);
            } catch (Exception e) {
                throw new Exception("Unable to extract .tar" + e);
            }
            break;
        case TGZ:
            // Extract .tar.gz
            try (InputStream fi = Files.newInputStream(sourcePath);
                 InputStream bi = new BufferedInputStream(fi);
                 InputStream gzi = new GzipCompressorInputStream(bi);
                 ArchiveInputStream i = new TarArchiveInputStream(gzi);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(i))) {
                extractArchiveEntry(targetDir, i, reader, customSanitizerParser);
            } catch (Exception e) {
                throw new Exception("Unable to extract .tgz" + e);
            }
            break;
        }
    }

    private static void extractArchiveEntry(File targetDir, ArchiveInputStream i, BufferedReader reader,
                                            CustomSanitizerParser customSanitizerParser) throws Exception {
        ArchiveEntry entry = null; // Each file within the archive is an entry
        while ((entry = i.getNextEntry()) != null) {
            if (!i.canReadEntryData(entry)) {
                // log something?
                continue;
            }
            String name = targetDir + "\\" + entry.getName(); // create new file

            File f = new File(name);
            if (!entry.isDirectory()) {
                Path sourcePath = Paths.get(entry.getName());

                FileParser fp = new FileParser(sourcePath, customSanitizerParser, reader);
                fp.fileParser();
            }
        }
    }
}
