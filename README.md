# Log-Sanitizer

Sanitizes log files to replace IP addresses, UUIDs and any custom texts with placeholders.

## How to use
* Using the command line, run: `java -jar Log_Sanitizer.jar </path/to/logfile>`
* To sanitize custom texts, run: `java -jar Log_Sanitizer.jar <path/to/logfile> <path/to/custom_sanitizer>`
<br>_Refer to [How to use: Custom Texts](#custom-texts) to learn how to configure the `custom_sanitizer
` file._

Archived log file formats accepted: `.zip`, `.tar`, `.tgz`. This application accepts any other unarchived log
 files. 

The sanitized logs will be stored in `./Sanitized_Logs/<logfile>`.
A summary of what was sanitized will be stored in `./Sanitized_Logs/Summary - <logfile>`.

## How it works
### IP addresses
Recognizes the pattern of any string from 0.0.0.0 to 255.255.255.255 as an IP address. These strings will be replaced
 with `IP_ADDRESS_#`, where `#` is the index of the IP address within the log file.

### UUIDs
Recognizes the pattern of any string of 32 hexadecimal digits displayed in 5 groups separated by hyphens in the
 form of 8-4-4-4-12 hexadecimal as a UUID. These strings will be replaced with `UUID_#`, where `#` is the index of the UUID within the log file.

### Custom Texts
Refer to the file `custom_sanitizer`.
1. Replace the semicolon in line 2 with your desired separator. 
2. Add each custom text to be sanitized *on a new line*, in the format of: <text_to_be_replaced><separator><placeholder>
> ie. p@ssw0rd;PASSWORD

## Application information
* Uses Java 11.0.8


 

_Sample log files are generated using: https://github.com/mingrammer/flog_
