package com.hawaii.epc.iosystem;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class IOSystemService {

    // Method to read a file's content and return it as a String
    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }

    // Method to write content to a file (overwrites if the file already exists)
    public void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Method to append content to an existing file
    public void appendToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.writeString(path, content, StandardOpenOption.APPEND);
    }

    // Method to delete a file
    public boolean deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.deleteIfExists(path);
    }

    // Method to list all files in a directory
    public List<String> listFilesInDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty");
        }

        List<String> fileAndDirectoryList = new ArrayList<>();
        Path path = Paths.get(directoryPath);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path entry : directoryStream) {
                fileAndDirectoryList.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally log the error or throw a custom exception
        }

        return fileAndDirectoryList;
    }


    // Method to create a directory
    public void createDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    // Method to delete a directory (must be empty)
    public boolean deleteDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        return Files.deleteIfExists(path);
    }

    // Method to copy a file
    public void copyFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    // Method to move a file
    public void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}

