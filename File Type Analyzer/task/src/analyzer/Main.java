package analyzer;


import analyzer.strategy.KmpSearchStrategy;
import analyzer.strategy.SearchSubstring;
import analyzer.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        if (args.length < 3) throw new RuntimeException("Введите параметры!");
        String folderPath = args[0];
        String pattern = args[1];
        String fileType = args[2];

        List<File> fileList = new Vector<>();
        FileUtil.getFilesRecursive(new File[]{ new File(folderPath) }, fileList);

        SearchSubstring searchSubstring = new SearchSubstring(new KmpSearchStrategy());
        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

        Callable<String> searcher = () -> {
            while (fileList.size() > 0) {
                var f = fileList.remove(0);
                try {
                    if (searchSubstring.contains(FileUtil.readFile(f.getPath()), pattern)) System.out.println(f.getName() + ": " + fileType);
                    else System.out.println(f.getName() + ": " + "Unknown file type");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        };

        List<Callable<String>> searches = new ArrayList<>();
        for (int i = 0; i < 10; i++) searches.add(searcher);
        WORKER_THREAD_POOL.invokeAll(searches);
        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
        WORKER_THREAD_POOL.shutdown();

    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
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
}
