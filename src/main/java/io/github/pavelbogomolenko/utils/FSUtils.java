package io.github.pavelbogomolenko.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FSUtils {
    public static String readFileContent(String filePath) {
        try {
            if(!System.getenv("DOCKER_RELATIVE_PATH_BEGIN").equals("")) {
                filePath = "/" + filePath;
            }
            Path fileName = Path.of(filePath);
            return Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
