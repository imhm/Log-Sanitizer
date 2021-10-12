# Log-Sanitizer

Sanitizes a log file to replace IP addresses and UUIDs with placeholders.

## How to use
Using the command line, run: `java -jar Log_Sanitizer.jar </path/to/logfile>`
The sanitized log will be stored in `./Sanitized_Logs/<logfile>`.

## How it works
### IP addresses
Recognizes the pattern of any string from 0.0.0.0 to 255.255.255.255 as an IP address. These strings will be replaced
 with `IP_ADDRESS_#`, where `#` is the index of the IP address within the log file.

### UUIDs
Recognizes the pattern of any string of 32 hexadecimal digits displayed in 5 groups separated by hyphens in the
 form of 8-4-4-4-12 hexadecimal as a UUID. These strings will be replaced with `UUID_#`, where `#` is the index of the UUID within the log file.

## Application information
* Uses Java 11.0.8

 

_Sample log files are generated using: https://github.com/mingrammer/flog_