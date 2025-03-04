package com.hawaii.epc.image.boundary;


import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class DropdownBean implements Serializable {

    private String selectedCountry;
    private String route;
    private String flagUrl;

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
        updateFlagUrl();
    }

    public String getRoute() {
        return route;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void submit() {
        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            this.route = "/images/" + selectedCountry + "/more/path";
        } else {
            this.route = "No country selected";
        }
    }

    private void updateFlagUrl() {
        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            this.flagUrl = "/resources/images/flags/" + selectedCountry + ".png";
        } else {
            this.flagUrl = null;
        }
    }
}