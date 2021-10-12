import java.util.ArrayList;
import java.util.List;

public class IpExtractor {
    private static final Integer MAX_CHAR_AWAY = 4;

    public static void main(String[] args) {
        extractIp("115.76.36.47 - - [06/Oct/2021:00:44:53 -0700] \"POST /empower/drive/ubiquitous/e-commerce "
                + "HTTP/2.0\" 401 26695 \"https://www.humancollaborative.name/robust/networks\" \"Mozilla/5.0 (X11; "
                + "Linux x86_64) AppleWebKit/5360 (KHTML, like Gecko) Chrome/36.0.884.0 Mobile Safari/5360\"");
    }

    /** Extract IP addresses to be replaced.
     *
     * @param text String to be parsed.
     * @return An Arraylist of IP addresses.
     */
    public static ArrayList<String> extractIp(String text) {
        char[] charArray = text.toCharArray();
        List<Integer> periodIndex = new ArrayList<>();

        // Contains the first and third(period) for each valid 3 consecutive periods
        ArrayList<ArrayList<Integer>> validPeriods = new ArrayList<>();

        ArrayList<String> ipAddressesToReplace = new ArrayList<>();
        extractPeriodIndex(charArray, periodIndex);
        searchForThreeConsecutivePeriods(periodIndex, validPeriods);
        extractIpAddressesToReplace(text, charArray, validPeriods, ipAddressesToReplace);
        validateIpAddress(ipAddressesToReplace);
        return ipAddressesToReplace;
    }

    // Checks the validity of the IP address (Ensure each number is between 0 and 255.
    private static void validateIpAddress(ArrayList<String> ipAddressesToReplace) {
        ArrayList<String> duplicate = new ArrayList<>(ipAddressesToReplace);
        for (String ip : duplicate) {
            String[] array = ip.split("\\.");
            for (String string : array) {
                try {
                    int number = Integer.parseInt(string);
                    if (number < 0 || number > 255) {
                        ipAddressesToReplace.remove(ip);
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error validating IP addresses: "  + e);
                }
            }
        }
    }

    //

    /**
     * Extracts the IP address from the text given and store into an arraylist
     * @param text Input string to extract IP address
     * @param charArray Array of characters from String text
     * @param validPeriods Arraylist containing the first and last indexes of valid 3 consecutive periods.
     * @param ipAddressesToReplace Arraylist to store IP address
     */
    private static void extractIpAddressesToReplace(String text, char[] charArray,
                                                    ArrayList<ArrayList<Integer>> validPeriods,
                                                    ArrayList<String> ipAddressesToReplace) {
        for (ArrayList<Integer> validPeriod : validPeriods) {
            // Find the numbers before the first period
            int i = 0;
            int beginIndex;
            do {
                i++; // Iterate to previous character
                beginIndex = validPeriod.get(0) - i;
            } while (beginIndex >= 0 && Character.isDigit(charArray[beginIndex]));
            if (i == 1 || i > 4) {
                //not valid
                continue;
            }
            beginIndex++;

            // Find the numbers after the last period
            int j = 0;
            int endIndex;
            int lastPeriodIndex = validPeriod.get(1);
            do {
                j++; // Iterate to previous character
                endIndex = lastPeriodIndex + j;
            } while (endIndex < charArray.length && Character.isDigit(charArray[endIndex]));
            if (j == 1 || j > 4) {
                //not valid, numbers out of bounds.
                continue;
            }

            ipAddressesToReplace.add(text.substring(beginIndex, endIndex));
        }
    }


    // Finds 3 consecutive periods in the periodIndex array.
    private static void searchForThreeConsecutivePeriods(List<Integer> periodIndex,
                                                         ArrayList<ArrayList<Integer>> validPeriods) {

        for (int i = 0; i < periodIndex.size() - 2; i++) {
            int currentPeriodIndex;
            int nextPeriodIndex;
            int j = 0;
            do {
                currentPeriodIndex = periodIndex.get(i+j);
                j++;
                if(i+j >= periodIndex.size()) { // Prevents index out of bounds
                    break;
                }
                nextPeriodIndex = periodIndex.get(i+j);
            }
            while (currentPeriodIndex + MAX_CHAR_AWAY >= nextPeriodIndex );
            int lastConsecutivePeriodIndex = i + j - 1;
            if (j == 3) {
                ArrayList<Integer> consecutivePeriods = new ArrayList<>();
                consecutivePeriods.add(periodIndex.get(i));  // Get first period index
                consecutivePeriods.add(periodIndex.get(lastConsecutivePeriodIndex));  // Get last period index
                validPeriods.add(consecutivePeriods);
            }
            i = lastConsecutivePeriodIndex;  // Update i to the last consecutive period element
        }
    }

    private static void extractPeriodIndex(char[] charArray, List<Integer> periodIndex) {
        int i = 0;
        for (char ch : charArray) {
            if (ch == '.') {
                periodIndex.add(i);
            }
            i++;
        }
    }
}
