# Log-Sanitizer

Sanitizes a log file to remove IP addresses.

## How it works
Recognizes the pattern of any string from 0.0.0.0 to 255.255.255.255 as an IP address. These strings will be replaced
 with "IP_ADDRESS_#", where # is the index of the IP address within the log file.

_Sample log files are generated using: https://github.com/mingrammer/flog_