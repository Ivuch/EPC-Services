package com.hawaii.epc.email.boundary;

import com.hawaii.epc.email.entity.EmailSender;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

@Named
@Slf4j
@SessionScoped
public class EmailService implements Serializable {

    @Inject
    private EmailSender emailSender;

    private String toEmail = "mmatellan@epc-insotre.com";
    private String subject = "Test Email from HawaiiGmail";
    private String body = "Jakarta Mail and Gmail SMTP.";

    public void sendEmail() {
        try {
            emailSender.sendEmail(toEmail, subject, body);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email sent successfully!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to send email."));
        }
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
