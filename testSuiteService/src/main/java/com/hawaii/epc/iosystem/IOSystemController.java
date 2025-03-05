package com.hawaii.epc.iosystem;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.util.List;

@Named("ioSystemController")
@RequestScoped
public class IOSystemController {

    @Inject
    private IOSystemService ioSystemService;

    private String filePath = "/media/ubuntu/25683426-685a-4ff9-a404-ed0d164f9736/home/Max/work/IOSystemService/io.txt";
    private String fileContent;
    private String directoryPath = "/media/ubuntu/25683426-685a-4ff9-a404-ed0d164f9736/home/Max/work";
    private String statusMessage;

    // Getters and Setters

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    // Methods to call IOSystemService

    public void readFile() {
        try {
            this.fileContent = ioSystemService.readFile(filePath);
            this.statusMessage = "File read successfully!";
        } catch (IOException e) {
            this.statusMessage = "Error reading file: " + e.getMessage();
        }
    }

    public void writeFile() {
        try {
            ioSystemService.writeFile(filePath, fileContent);
            this.statusMessage = "File written successfully!";
        } catch (IOException e) {
            this.statusMessage = "Error writing file: " + e.getMessage();
        }
    }

    public void deleteFile() {
        try {
            boolean deleted = ioSystemService.deleteFile(filePath);
            this.statusMessage = deleted ? "File deleted successfully!" : "File not found!";
        } catch (IOException e) {
            this.statusMessage = "Error deleting file: " + e.getMessage();
        }
    }

    public List<String> listFiles() {
        return ioSystemService.listFilesInDirectory(directoryPath);
    }
}
