package analyzer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static boolean checkFile(String filePath, String pattern) throws IOException {
        boolean result = false;
        byte[] buff = Files.readAllBytes(Paths.get(filePath));
        byte[] patternBytes = pattern.getBytes();
        for (int i = 0; i < patternBytes.length; i++) {
            if (buff[i] == patternBytes[0]) {
                result = true;
                for (int j = 1; j < patternBytes.length; j++) {
                    if (buff[i + j] != patternBytes[j]) {
                        result = false;
                        break;
                    }
                }
                if (result) break;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 3) throw new RuntimeException("Введите параметры!");
        String filePath = args[0];
        String pattern = args[1];
        String fileType = args[2];
        if (checkFile(filePath, pattern)) System.out.println(fileType);
        else System.out.println("Unknown file type");

    }

}
