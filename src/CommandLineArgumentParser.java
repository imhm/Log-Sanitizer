import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineArgumentParser {
    public enum FILE_EXTENSION {
        DEFAULT,    // Single file
        ZIP,
        TAR,
        TGZ
    }
    private FILE_EXTENSION fileExtension;
    private Path sourcePath;
    private Path customSanitizerPath;


    public CommandLineArgumentParser(String[] args) throws Exception{
        if(args.length == 0) {
            throw new Exception("Run the program with the filepath of the logfile as the argument.");
        }

        // Get source path
        sourcePath = Paths.get(args[0]);


        // Get file extension
        setFileExtension();

        // Get custom sanitizer file path
        if (args.length > 1) {
            customSanitizerPath = Paths.get(args[1]);
        }
    }

    private void setFileExtension() {
        String extension;

        int i = sourcePath.toString().lastIndexOf('.');
        if (i > 0) {
            extension = sourcePath.toString().substring(i+1).toLowerCase();
            switch (extension) {
            case "gz":
            case "tgz":
                fileExtension = FILE_EXTENSION.TGZ;
                System.out.println("File extension: TGZ");
                break;
            case "tar": fileExtension = FILE_EXTENSION.TAR;
                System.out.println("File extension: TAR");
                break;
            case "zip": fileExtension = FILE_EXTENSION.ZIP;
                System.out.println("File extension: ZIP");
                break;
            default: fileExtension = FILE_EXTENSION.DEFAULT;
            }
        }
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getCustomSanitizerPath() {
        return customSanitizerPath;
    }

    public FILE_EXTENSION getFileExtension() {
        return fileExtension;
    }
}
