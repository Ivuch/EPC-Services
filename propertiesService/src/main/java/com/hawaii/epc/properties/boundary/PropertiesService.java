package com.hawaii.epc.properties.boundary;



import com.hawaii.epc.properties.entity.PropertiesReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class PropertiesService {

    @Inject
    private PropertiesReader propertiesReader;

    String location = "";
    String appVersion = "";
    String appDev = "";

    public void loadAndPrintProperties() {

        location = propertiesReader.getProperty("location.origin");
        appVersion = propertiesReader.getProperty("app.version");
        appDev = propertiesReader.getProperty("app.dev");

        System.out.println("App Location: " + location.toUpperCase());
        System.out.println("App Version: " + appVersion);
        System.out.println("App Author: " + appDev);
    }
};
