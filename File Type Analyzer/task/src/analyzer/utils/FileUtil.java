package analyzer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    public static byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public static void getFilesRecursive(File[] arr, List<File> fileList)
    {
        for (File f : arr)
        {
            if(f.isFile()) fileList.add(f);
            else if(f.isDirectory())
            {
                getFilesRecursive(Objects.requireNonNull(f.listFiles()), fileList);
            }
        }
    }

}
