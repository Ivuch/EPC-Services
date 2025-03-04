package com.hawaii.epc.iosystem;

import com.hawaii.epc.iosystem.boundary.IOSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IOSystemServiceTest {

    @InjectMocks
    private IOSystemService ioSystemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadFile() throws IOException {
        String filePath = "/path/to/file.txt";
        String expectedContent = "Hello, World!";

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readString(Paths.get(filePath))).thenReturn(expectedContent);

            String content = ioSystemService.readFile(filePath);

            assertEquals(expectedContent, content, "El contenido del archivo debería coincidir");
        }
    }

    @Test
    public void testWriteFile() throws IOException {
        String filePath = "/path/to/file.txt";
        String content = "Hello, World!";

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            ioSystemService.writeFile(filePath, content);

            mockedFiles.verify(() -> Files.writeString(
                    Paths.get(filePath),
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            ), times(1));
        }
    }

    @Test
    public void testDeleteFile() throws IOException {
        String filePath = "/path/to/file.txt";

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(Paths.get(filePath))).thenReturn(true);

            boolean deleted = ioSystemService.deleteFile(filePath);

            assertTrue(deleted, "El archivo debería haberse eliminado");
        }
    }

    @Test
    public void testListFilesInDirectory() throws IOException {
        String directoryPath = "/path/to/directory";

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class);
             MockedStatic<Paths> mockedPaths = Mockito.mockStatic(Paths.class)) {

            Path mockPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get(directoryPath)).thenReturn(mockPath);

            DirectoryStream<Path> mockDirectoryStream = mock(DirectoryStream.class);
            mockedFiles.when(() -> Files.newDirectoryStream(mockPath)).thenReturn(mockDirectoryStream);

            Path mockFile1 = mock(Path.class);
            when(mockFile1.getFileName()).thenReturn(mock(Path.class)); // Mock the filename
            when(mockFile1.getFileName().toString()).thenReturn("file1.txt"); // Return the filename as a string

            Path mockFile2 = mock(Path.class);
            when(mockFile2.getFileName()).thenReturn(mock(Path.class)); // Mock the filename
            when(mockFile2.getFileName().toString()).thenReturn("file2.txt"); // Return the filename as a string
            when(mockDirectoryStream.iterator()).thenReturn(List.of(mockFile1, mockFile2).iterator());

            List<String> files = ioSystemService.listFilesInDirectory(directoryPath);

            assertEquals(2, files.size(), "Debería haber 2 archivos en el directorio");
            assertTrue(files.contains("file1.txt"), "El archivo 'file1.txt' debería estar en la lista");
            assertTrue(files.contains("file2.txt"), "El archivo 'file2.txt' debería estar en la lista");
        }
    }
}