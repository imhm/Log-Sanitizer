import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidExtractor {
    private static final int NUM_CHAR_BETWEEN_HYPHEN_PAIR = 5;
    private static final String uuidRegex = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    public static void main(String[] args) {
        extractUuid("172.163.248.72 - - [06/Oct/2021:00:44:53 -0700] \"HEAD /next-generation/channels HTTP/2.0\" 304 "
                + "58211 \"https://www.principalout-of-the-box.name/next-generation\" \"Mozilla/5.0 (Macintosh; Intel"
                + " Mac OS X 10_7_1 rv:7.0) Gecko/1940-31-08 Firefox/35.0\"");
    }

    public static ArrayList<String> extractUuid(String text) {

        ArrayList<ArrayList<Integer>> validHyphens = new ArrayList<>();
        ArrayList<Integer> hyphenIndex = new ArrayList<Integer>();
        char[] charArray = text.toCharArray();
        ArrayList<String> uuids = new ArrayList<>();


        // find the hyphens
        extractHyphenIndex(hyphenIndex, charArray);

        // search for consecutive hyphens
        searchForConsecutiveHyphens(hyphenIndex, validHyphens);

        // extract full uuid strings
        extractUuids(uuids, text, validHyphens);

        // validate uuids
        validateUuids(uuids);
        return uuids;
    }

    private static void validateUuids(ArrayList<String> uuids) {
        ArrayList<String> duplicate = new ArrayList<>(uuids);
        for (String uuid: duplicate) {
            Matcher m = Pattern.compile(uuidRegex)
                    .matcher(uuid);
            if (!m.find()) {
                uuids.remove(uuid);
            }
        }
    }

    private static void extractUuids(ArrayList<String> uuids, String text, ArrayList<ArrayList<Integer>> validHyphens) {
        for (ArrayList<Integer> validHyphen : validHyphens) {

            int beginIndex = validHyphen.get(0) - 8;
            int endIndex = validHyphen.get(1) + 13; // +12 +1 to account for endIndex exclusive in substring.
            try {
                uuids.add(text.substring(beginIndex, endIndex));
            } catch (Exception e) {
                System.out.println(validHyphens);
                System.out.println("Error: Unable to extractUuids on line:" );
                System.out.println(text);
            }
        }
    }

    private static void searchForConsecutiveHyphens(ArrayList<Integer> hyphenIndex, ArrayList<ArrayList<Integer>> validHyphens) {
        for (int i = 0; i < hyphenIndex.size() - 3; i++) {
            int currentHyphenIndex;
            int nextHyphenIndex;
            int j = 0;
            do {
                currentHyphenIndex = hyphenIndex.get(i+j);
                j++;
                if(i+j >= hyphenIndex.size()) { // Prevents index out of bounds
                    break;
                }
                nextHyphenIndex = hyphenIndex.get(i+j);
            }
            while (currentHyphenIndex + NUM_CHAR_BETWEEN_HYPHEN_PAIR >= nextHyphenIndex );
            int lastConsecutiveHyphenIndex = i + j - 1;
            if (j == 4) {
                ArrayList<Integer> consecutiveHyphens = new ArrayList<>();
                consecutiveHyphens.add(hyphenIndex.get(i));  // Get first hyphen index
                consecutiveHyphens.add(hyphenIndex.get(lastConsecutiveHyphenIndex));  // Get last hyphen index
                validHyphens.add(consecutiveHyphens);
            }
            i = lastConsecutiveHyphenIndex;  // Update i to the last consecutive hyphen element
        }
    }

    private static void extractHyphenIndex(ArrayList<Integer> hyphenIndex, char[] charArray) {
        int i = 0;
        for (char ch : charArray) {
            if (ch == '-') {
                hyphenIndex.add(i);
            }
            i++;
        }
    }

}
