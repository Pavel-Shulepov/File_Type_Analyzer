package analyzer;


import analyzer.strategy.KmpSearchStrategy;
import analyzer.strategy.NaiveSearchStrategy;
import analyzer.strategy.SearchSubstring;
import analyzer.utils.FileUtil;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 4) throw new RuntimeException("Введите параметры!");
        String algorithm = args[0];
        String filePath = args[1];
        String pattern = args[2];
        String fileType = args[3];

        SearchSubstring searchSubstring;
        if ("--naive".equals(algorithm)) {
            searchSubstring = new SearchSubstring(new NaiveSearchStrategy());
        } else if ("--KMP".equals(algorithm)) {
            searchSubstring = new SearchSubstring(new KmpSearchStrategy());
        } else throw new RuntimeException("Неверная стратегия поиска!");


        long start = System.nanoTime();
        if (searchSubstring.contains(FileUtil.readFile(filePath), pattern)) System.out.println(fileType);
        else System.out.println("Unknown file type");
        long elapsedNanos = System.nanoTime() - start;
        System.out.println("It took " + elapsedNanos / 1000000000 + " seconds");

    }

}
