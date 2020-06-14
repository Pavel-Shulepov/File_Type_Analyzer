package analyzer.strategy;

public class NaiveSearchStrategy implements SearchStrategy {

    @Override
    public boolean searchSubstring(byte[] content, String pattern) {
        boolean result = false;
        byte[] patternBytes = pattern.getBytes();
        for (int i = 0; i < content.length; i++) {
            if (content[i] == patternBytes[0]) {
                result = true;
                for (int j = 1; j < patternBytes.length; j++) {
                    if (content[i + j] != patternBytes[j]) {
                        result = false;
                        break;
                    }
                }
                if (result) break;
            }
        }
        return result;
    }
}
