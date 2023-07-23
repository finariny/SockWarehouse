package com.example.sockwarehouse.service;

import java.nio.file.Path;

public interface FileService {
    void saveToFile(Path path, String json);

    String readFromFile(Path path);

    void cleanFile(Path path);
}
