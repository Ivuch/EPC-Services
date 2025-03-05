package com.hawaii.epc.video.boundary;



import com.hawaii.epc.video.entity.Video;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.file.UploadedFile;

import java.io.*;
import java.io.Serializable;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Named
@Slf4j
@SessionScoped
public class VideoController implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ROOT_DIRECTORY = "/Users/max/work/dev/videoService/src/main/webapp";
    public static final String VIDEO_DIRECTORY = "/resources/videos";

    private UploadedFile uploadedFile;
    private List<Video> videos = new ArrayList<>();


    private Video video = new Video();

    public VideoController() {
        loadVideos();
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<Video> getVideos() {
        return videos;
    }

    // Cargar la lista de videos desde el sistema de archivos
    public void loadVideos() {
        videos.clear();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(ROOT_DIRECTORY + VIDEO_DIRECTORY), "*.{mp4,mov,avi,mkv}")) {
            for (Path entry : stream) {
                videos.add(new Video(entry.getFileName().toString(), VIDEO_DIRECTORY + File.separator + entry.getFileName().toString()));
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar videos", e.getMessage()));
        }
    }



    // Subir un video
    public void uploadVideo() throws IOException {
        if (uploadedFile != null) {
            Path uploadDir = Paths.get(ROOT_DIRECTORY + VIDEO_DIRECTORY);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(video.getName());
            byte[] fileContent = uploadedFile.getContent();
            Files.write(filePath, fileContent);
            video.setPath(filePath.toString());
            loadVideos();
            /*
            try (InputStream input = uploadedFile.getInputStream()) {
                Path filePath = Paths.get(ROOT_DIRECTORY + VIDEO_DIRECTORY, uploadedFile.getFileName());
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Video subido con éxito", uploadedFile.getFileName()));
                loadVideos(); // Actualizar la lista de videos
            } catch (IOException e) {
                log.info("Error al subir el video "+ e.getMessage());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se seleccionó ningún archivo", ""));
        }*/
        }
    }

    // Descargar un video
    public void downloadVideo(Video video) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            File file = new File(video.getPath());
            if (!file.exists()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Archivo no encontrado", ""));
                return;
            }

            externalContext.responseReset();
            externalContext.setResponseContentType(Files.probeContentType(file.toPath()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
            try (OutputStream output = externalContext.getResponseOutputStream();
                 FileInputStream input = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) > 0) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            facesContext.responseComplete();
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al descargar el video", e.getMessage()));
        }
    }

    // Eliminar un video
    public void deleteVideo(Video video) throws IOException {
        Path filePath = Paths.get(ROOT_DIRECTORY + VIDEO_DIRECTORY + File.separator + video.getName());
        Files.deleteIfExists(filePath);
        loadVideos();
    }

    // Obtener la ruta del video para reproducirlo
    public String getVideoPath(Video video) {
        // Ajusta esta lógica según cómo expongas los archivos en tu servidor
        return VIDEO_DIRECTORY +"/"+ video.getName();
    }


    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}


