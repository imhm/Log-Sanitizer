/**
 * Entry point of the Main application.
 * Initializes the application and starts the interaction with the user.
 */
public class Main {
    /*
    Inputs:
        Logs files (.zip/.tar/.tgz) [<1GB]  - TAR Archive file .tgz = .tar.gz file
        whitelist  (what should be replaced)

    Output:
        Sanitized logs
        Summary of replacements
     */
    public static void main(String[] args) {
        System.out.println("Sanitizing log file...");
        try {
            CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);
            CustomSanitizerParser customSanitizerParser = new CustomSanitizerParser(commandLineArgumentParser.getCustomSanitizerPath());
            FileParser fp = new FileParser(commandLineArgumentParser.getSourcePath(), customSanitizerParser);
            fp.fileParser();
            System.out.println("Log file sanitized.");
        } catch (Exception e) {
            System.out.println("Unable to sanitize log file: ");
            e.printStackTrace();
        }
    }
}
