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
        if(args.length != 1) {
            System.out.println("Run the program with the filepath of the logfile as the argument.");
            return;
        }

        System.out.println("Sanitizing log file...");
        try {
            CommandLineArgumentParser parser = new CommandLineArgumentParser(args);
            FileParser fp = new FileParser(parser.getSourcePath());
            fp.fileParser();
            System.out.println("Log file sanitized.");
        } catch (Exception e) {
            System.out.println("Unable to sanitize log file: ");
            System.out.println(e.getMessage());
        }
    }
}
