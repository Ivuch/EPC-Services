package com.hawaii.epc.image.boundary;

import com.hawaii.epc.image.entity.Image;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ImageService implements Serializable {

    public static final String DOWNLOAD_DIR = "/Users/max/Downloads/";
    public static final String UPLOAD_DIR = "/resources/images/";
    public static final String ROOT_DIR ="/Users/max/work/dev/imageService/src/main/webapp";


    public StreamedContent downloadImage(String imageName, String chain) {
        try {
            String UPLOAD_DIR = "/resources/images/";
            String ROOT_DIR = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");

            Path imagePath = Paths.get(ROOT_DIR + UPLOAD_DIR + chain + File.separator + imageName);

            if (!Files.exists(imagePath)) {
                throw new FileNotFoundException("Image not found: " + imagePath.toString());
            }

            InputStream stream = new FileInputStream(imagePath.toFile());

            return DefaultStreamedContent.builder()
                    .name(imageName)
                    .contentType(Files.probeContentType(imagePath))
                    .stream(() -> stream)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo descargar la imagen."));
            return null;
        }
    }

    public void saveImage(Image image, byte[] fileContent, String chain) throws IOException {
        Path uploadDir = Paths.get(ROOT_DIR + UPLOAD_DIR + chain + File.separator);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(image.getTitle()); //was .getName() before on imageService2
        Files.write(filePath, fileContent);
        image.setItemImageSrc(filePath.toString());  //was .setPath() before on imageService2
    }

    public List<Image> getAllImages(String chain) {
        List<Image> images = new ArrayList<>();
        Path uploadDir = Paths.get(ROOT_DIR + UPLOAD_DIR+chain);

        if (Files.exists(uploadDir)) {
            try (var directoryStream = Files.newDirectoryStream(uploadDir)) {
                for (Path filePath : directoryStream) {
                    if (isValidImageExtension(filePath)) {
                        Image image = new Image();
                        image.setItemImageSrc(UPLOAD_DIR + chain + File.separator + filePath.getFileName().toString());
                        image.setThumbnailImageSrc(UPLOAD_DIR + chain + File.separator + filePath.getFileName().toString());
                        image.setAlt(filePath.getFileName().toString());
                        image.setTitle(filePath.getFileName().toString());
                        images.add(image);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return images;
    }
   /* public List<Image> getAllImages2() {
        List<Image> images = new ArrayList<>();
        Path uploadDir = Paths.get(UPLOAD_DIR);

        if (Files.exists(uploadDir)) {
            try (var directoryStream = Files.newDirectoryStream(uploadDir)) {
                for (Path filePath : directoryStream) {
                    // Verificar si el archivo tiene una extensión válida
                    if (isValidImageExtension(filePath)) {
                        Image image = new Image();
                        image.setName(filePath.getFileName().toString());
                        image.setPath(filePath.toAbsolutePath().toString());
                        images.add(image);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return images;
    }*/

    /**
     * Verifica si la extensión del archivo es .jpg o .jpeg.
     *
     * @param filePath Ruta del archivo.
     * @return true si la extensión es válida, false en caso contrario.
     */
    private boolean isValidImageExtension(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();

        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".webp");
    }

    public void deleteImage(String name, String chain) throws IOException {
        Path filePath = Paths.get(ROOT_DIR + UPLOAD_DIR + chain + File.separator + name);
        Files.deleteIfExists(filePath);
    }

    public void updateImage(String oldName, Image newImage, byte[] fileContent, String chain) throws IOException {
        deleteImage(oldName, chain);
        saveImage(newImage, fileContent, chain);
    }
}