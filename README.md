# Log-Sanitizer

Sanitizes a log file to remove IP addresses.

Recognizes the pattern of any string from 0.0.0.0 to 255.255.255.255 will be replaced with "IP_ADDRESS_#" where # is the index of the IP address within the log file.
