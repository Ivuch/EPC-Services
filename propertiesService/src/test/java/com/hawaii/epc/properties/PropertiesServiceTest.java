package com.hawaii.epc.properties;

import com.hawaii.epc.properties.entity.PropertiesReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PropertiesServiceTest {

    @InjectMocks
    private PropertiesReader propertiesReader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadProperties() {
        // Arrange
        String propertiesContent = "location.origin=Kauai\napp.version=v0.1\napp.dev=Maximiliano Ivan Matellan";
        InputStream inputStream = new ByteArrayInputStream(propertiesContent.getBytes());

        // Act
        propertiesReader = new PropertiesReader() {
            protected InputStream getPropertiesInputStream(String propertiesFileName) {
                return inputStream;
            }
        };
        propertiesReader.init();

        // Assert
        assertEquals("Kauai", propertiesReader.getProperty("location.origin"));
        assertEquals("v0.1", propertiesReader.getProperty("app.version"));
        assertEquals("Maximiliano Ivan Matellan", propertiesReader.getProperty("app.dev"));
    }

}