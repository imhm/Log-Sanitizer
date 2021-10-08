import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineArgumentParser {

    private Path sourcePath;
    private Path destPath;

    public CommandLineArgumentParser(String[] args) {
        sourcePath = Paths.get(args[0]);
        destPath = Paths.get("./Sanitized_log");
//        destPath = Paths.get(args[1]);
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getDestPath() {
        return destPath;
    }
}
