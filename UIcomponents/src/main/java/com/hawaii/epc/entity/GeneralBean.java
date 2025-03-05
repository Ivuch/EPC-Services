package com.hawaii.epc.entity;


import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;

import java.io.Serializable;
import java.util.Date;

@Named
@Slf4j
@SessionScoped
public class GeneralBean implements Serializable {
    private String text;
    private String selectedOption;
    private Date date;
    private int number;
    private boolean checked;
    private String radioValue;
    private int sliderValue;
    private int totalRecords;
    private int progress;

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }


    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }



    public void submit() {
        // Handle submit action
    }

    public void handleFileUpload(FileUploadEvent event) {
        // Handle file upload
    }

    public GeneralBean() {

    }
}
