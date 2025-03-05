package com.hawaii.epc.video;

import com.hawaii.epc.video.boundary.VideoController;
import com.hawaii.epc.video.entity.Video;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.model.file.UploadedFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoControllerTest {


    @Mock
    private UploadedFile uploadedFile;

    @InjectMocks
    private VideoController videoController;

    private Video video1;
    private Video video2;

    @BeforeEach
    public void setUp() {
        video1 = new Video("video1.mp4", VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video1.mp4");
        video2 = new Video("video2.mp4", VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video2.mp4");
    }

    @Test
    public void testLoadVideos() throws IOException {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path videoDir = Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY);
            DirectoryStream<Path> directoryStream = mock(DirectoryStream.class);
            when(directoryStream.iterator()).thenReturn(List.of(
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video1.mp4"),
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video2.mp4")
            ).iterator());
            mockedFiles.when(() -> Files.newDirectoryStream(videoDir, "*.{mp4,mov,avi,mkv}")).thenReturn(directoryStream);

            videoController.loadVideos();

            List<Video> videos = videoController.getVideos();
            assertEquals(2, videos.size(), "La lista de videos debe contener 2 elementos");
            assertEquals("video1.mp4", videos.get(0).getName(), "El nombre del primer video debe ser 'video1.mp4'");
            assertEquals("video2.mp4", videos.get(1).getName(), "El nombre del segundo video debe ser 'video2.mp4'");
        }
    }

    @Test
    public void testUploadVideo() throws IOException {
        when(uploadedFile.getContent()).thenReturn(new byte[1024]);
        lenient().when(uploadedFile.getFileName()).thenReturn("video1.mp4");

        videoController.setVideo(new Video("video1.mp4", null));

        // Simular el método estático Files.write y el DirectoryStream
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path videoDir = Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY);
            DirectoryStream<Path> directoryStream = mock(DirectoryStream.class);
            when(directoryStream.iterator()).thenReturn(List.of(
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video1.mp4"),
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video2.mp4")
            ).iterator());

            //lenient porque no es parte directa del metodo a testear
            lenient().when(Files.newDirectoryStream(videoDir, "*.{mp4,mov,avi,mkv}")).thenReturn(directoryStream);


            mockedFiles.when(() -> Files.exists(videoDir)).thenReturn(false);
            mockedFiles.when(() -> Files.createDirectories(videoDir)).thenReturn(videoDir);


            videoController.setUploadedFile(uploadedFile);
            videoController.uploadVideo();


            List<Video> videos = videoController.getVideos();
            assertFalse(videos.isEmpty(), "La lista de videos no debe estar vacía después de subir un video");

            // Verificar que se llamó a Files.write
            mockedFiles.verify(() -> Files.write(any(Path.class), any(byte[].class)), times(1));
        }
    }
/*
    @Test
    public void testDownloadVideo() throws IOException {
        File file = new File(video1.getPath());
        when(externalContext.getResponseOutputStream()).thenReturn(mock(OutputStream.class));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.probeContentType(file.toPath())).thenReturn("video/mp4");

            videoController.downloadVideo(video1);

            verify(externalContext, times(1)).setResponseContentType("video/mp4");
            verify(externalContext, times(1)).setResponseHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
        }
    }
*/
    @Test
    public void testDeleteVideo() throws IOException {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path videoDir = Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY);
            DirectoryStream<Path> directoryStream = mock(DirectoryStream.class);
            when(directoryStream.iterator()).thenReturn(List.of(
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video1.mp4"),
                    Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + "/video2.mp4")
            ).iterator());
            mockedFiles.when(() -> Files.newDirectoryStream(videoDir, "*.{mp4,mov,avi,mkv}")).thenReturn(directoryStream);

            // Simula la eliminación del archivo
            Path videoPath = Paths.get(VideoController.ROOT_DIRECTORY + VideoController.VIDEO_DIRECTORY + File.separator + video1.getName());
            mockedFiles.when(() -> Files.deleteIfExists(videoPath)).thenReturn(true);

            videoController.deleteVideo(video1);

            mockedFiles.verify(() -> Files.deleteIfExists(videoPath), times(1));
        }
    }

    @Test
    public void testGetVideoPath() {
        String videoPath = videoController.getVideoPath(video1);
        assertEquals(VideoController.VIDEO_DIRECTORY + "/video1.mp4", videoPath, "La ruta del video debe ser '/resources/videos/video1.mp4'");
    }
}