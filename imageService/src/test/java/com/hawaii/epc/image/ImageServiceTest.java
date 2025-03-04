package com.hawaii.epc.image;

import com.hawaii.epc.image.boundary.ImageService;
import com.hawaii.epc.image.entity.Image;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.model.StreamedContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private FacesContext facesContext;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testSaveImage() throws IOException {
        Image image = new Image();
        image.setTitle("test.jpg");
        byte[] fileContent = "test content".getBytes();

        imageService.saveImage(image, fileContent, "FL");

        Path expectedPath = Paths.get(ImageService.ROOT_DIR + ImageService.UPLOAD_DIR + "test.jpg");
        assertTrue(Files.exists(expectedPath), "El archivo debería haberse creado");
        Files.deleteIfExists(expectedPath);
    }

    @Test
    public void testGetAllImages() throws IOException {
        Path imagePath = Paths.get(ImageService.ROOT_DIR + ImageService.UPLOAD_DIR + "treasure.jpg");
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, "test content".getBytes());

        List<Image> images = imageService.getAllImages("FL");

        assertFalse(images.isEmpty(), "La lista de imágenes no debería estar vacía");
        assertEquals("treasure.jpg", images.get(0).getTitle(), "El título de la imagen debería ser 'treasure.jpg'");

    }

    @Test
    public void testDeleteImage() throws IOException {
        String imageName = "test.jpg";
        Path imagePath = Paths.get(ImageService.ROOT_DIR + ImageService.UPLOAD_DIR + imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, "test content".getBytes());

        imageService.deleteImage(imageName, "FL");

        assertFalse(Files.exists(imagePath), "El archivo debería haberse eliminado");
    }

    @Test
    public void testDownloadImage() throws IOException {
        String imageName = "test.jpg";
        Path imagePath = Paths.get(ImageService.ROOT_DIR + ImageService.UPLOAD_DIR + "FL" + imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, "test content".getBytes());

        try (var mockedStatic = mockStatic(FacesContext.class)) {
            FacesContext facesContext = mock(FacesContext.class);
            ExternalContext externalContext = mock(ExternalContext.class);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getRealPath("")).thenReturn(ImageService.ROOT_DIR);

            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            StreamedContent streamedContent = imageService.downloadImage(imageName, "FL");

            assertNotNull(streamedContent, "El StreamedContent no debería ser nulo");
            assertEquals(imageName, streamedContent.getName(), "El nombre del archivo debería coincidir");
        }

        // Clean up
        Files.deleteIfExists(imagePath);
    }
}