package analyzer.strategy;

import java.util.ArrayList;
import java.util.List;

public class KmpSearchStrategy implements SearchStrategy {

    private int[] prefixFunction(String str) {
        int[] prefixFunc = new int[str.length()];
        for (int i = 1; i < str.length(); i++) {
            int j = prefixFunc[i - 1];
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }
            prefixFunc[i] = j;
        }
        return prefixFunc;
    }

    public List<Integer> KMPSearch(byte[] text, String pattern) {
        int[] prefixFunc = prefixFunction(pattern);
        ArrayList<Integer> occurrences = new ArrayList<>();
        byte[] patternBytes = pattern.getBytes();
        int j = 0;
        for (int i = 0; i < text.length; i++) {
            while (j > 0 && text[i] != patternBytes[j]) {
                j = prefixFunc[j - 1];
            }
            if (text[i] == patternBytes[j]) {
                j += 1;
            }
            if (j == pattern.length()) {
                occurrences.add(i - j + 1);
                j = prefixFunc[j - 1];
            }
        }
        return occurrences;
    }

    @Override
    public boolean searchSubstring(byte[] content, String pattern) {
        return KMPSearch(content, pattern).size() > 0;
    }
}
