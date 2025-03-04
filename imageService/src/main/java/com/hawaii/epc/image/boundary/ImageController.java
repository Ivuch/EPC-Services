package com.hawaii.epc.image.boundary;


import com.hawaii.epc.image.entity.Image;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ImageController implements java.io.Serializable {

    @Inject
    private ImageService imageService;

    private Image image = new Image();
    private UploadedFile uploadedFile;

    public void upload(String chain) {
        if (uploadedFile != null) {
            try {
                imageService.saveImage(image, uploadedFile.getContent(), chain);
                getAllImages(chain);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Image> getAllImages(String chain) {

        List<Image> images = imageService.getAllImages(chain);
        for (Image image : images) {
            String uniqueParam = "?v=" + System.currentTimeMillis(); // Añade un timestamp único
            image.setThumbnailImageSrc(image.getThumbnailImageSrc() + uniqueParam);
            image.setItemImageSrc(image.getItemImageSrc() + uniqueParam);
        }
        return images;

        //return imageService.getAllImages();
    }

    public void deleteImage(String name, String chain) {
        try {
            imageService.deleteImage(name, chain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StreamedContent downloadImage(String imageName, String chain) {
        try {
            String DOWNLOAD_DIR = "/Users/max/Downloads/";
           String UPLOAD_DIR = "/resources/images/";
           String ROOT_DIR ="/Users/max/work/dev/imageService/src/main/webapp";

            File sourceFile = new File(ROOT_DIR + UPLOAD_DIR + chain + File.separator + imageName);
            File destinationFile = new File(DOWNLOAD_DIR + imageName);

            // Verificar si el archivo fuente existe
            if (!sourceFile.exists()) {
                throw new FileNotFoundException("Image not found in source directory: " + sourceFile.getAbsolutePath());
            }

            // Si el archivo no existe en el destino, copiarlo
            if (!destinationFile.exists()) {
                Files.createDirectories(destinationFile.getParentFile().toPath());
                Files.copy(sourceFile.toPath(), destinationFile.toPath());
            }

            // Crear un InputStream para el archivo de destino
            InputStream stream = new FileInputStream(destinationFile);

            // Retornar el archivo como contenido descargable
            return DefaultStreamedContent.builder()
                    .name(imageName)
                    .contentType(Files.probeContentType(destinationFile.toPath()))
                    .stream(() -> stream)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<ResponsiveOption> responsiveOptions;

    public ImageController() {
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 5));
        responsiveOptions.add(new ResponsiveOption("768px", 3));
        responsiveOptions.add(new ResponsiveOption("560px", 1));
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    // Getters y Setters
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}