import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineArgumentParser {

    private Path sourcePath;
    private Path customSanitizerPath;

    public CommandLineArgumentParser(String[] args) throws Exception{
        if(args.length == 0) {
            throw new Exception("Run the program with the filepath of the logfile as the argument.");
        }

        // Get source path
        sourcePath = Paths.get(args[0]);

        // Get custom sanitizer file path
        if (args.length > 1) {
            customSanitizerPath = Paths.get(args[1]);
        }
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getCustomSanitizerPath() {
        return customSanitizerPath;
    }
}
