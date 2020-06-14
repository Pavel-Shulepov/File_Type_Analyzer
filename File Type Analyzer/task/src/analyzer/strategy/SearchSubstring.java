package analyzer.strategy;

public class SearchSubstring {

    SearchStrategy searchStrategy;

    public SearchSubstring(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public boolean contains(byte[] content, String pattern) {
        return searchStrategy.searchSubstring(content, pattern);
    }
}
