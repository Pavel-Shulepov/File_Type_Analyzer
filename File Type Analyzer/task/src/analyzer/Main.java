package analyzer;


import analyzer.strategy.KmpSearchStrategy;
import analyzer.strategy.SearchSubstring;
import analyzer.utils.FileUtil;
import analyzer.utils.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 2) throw new RuntimeException("Введите параметры!");
        String patternPath = args[1];
        String folderPath = args[0];

        List<Pattern> patterns = new LinkedList<>(preparePatterns(patternPath));

        List<File> fileList = new Vector<>();
        FileUtil.getFilesRecursive(new File[]{ new File(folderPath) }, fileList);

        SearchSubstring searchSubstring = new SearchSubstring(new KmpSearchStrategy());
        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

        Callable<String> searcher = () -> {
            while (fileList.size() > 0) {
                var f = fileList.remove(0);
                var content = FileUtil.readFile(f.getPath());
                boolean wanted = false;
                for(var p: patterns) {
                    if (searchSubstring.contains(content, p.getPattern())) {
                        System.out.println(f.getName() + ": " + p.getFileType());
                        wanted = true;
                        break;
                    }
                }
                if (!wanted) System.out.println(f.getName() + ": " + "Unknown file type");
            }
            return null;
        };

        List<Callable<String>> searches = new ArrayList<>();
        for (int i = 0; i < 10; i++) searches.add(searcher);
        WORKER_THREAD_POOL.invokeAll(searches);
        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
        WORKER_THREAD_POOL.shutdown();

    }

    private static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static List<Pattern> preparePatterns(String patternPath) throws IOException {
        List<Pattern> result = new LinkedList<>();
        FileUtil.readLines(patternPath).forEach(line -> {
            String[] params = line.split(";");
            result.add(new Pattern(Integer.parseInt(params[0]), params[1].replaceAll("\"", ""), params[2].replaceAll("\"", "")));
        });
        result.sort((p1, p2) -> p2.getPriority() - p1.getPriority());
        return result;
    }

}
