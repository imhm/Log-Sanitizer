import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineArgumentParser {

    private Path sourcePath;

    public CommandLineArgumentParser(String[] args) {
        sourcePath = Paths.get(args[0]);
    }

    public Path getSourcePath() {
        return sourcePath;
    }

}
