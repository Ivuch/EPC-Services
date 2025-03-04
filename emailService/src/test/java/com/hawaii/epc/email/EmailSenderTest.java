package com.hawaii.epc.email;

import com.hawaii.epc.email.entity.EmailSender;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailSenderTest {

    @InjectMocks
    private EmailSender emailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail() throws MessagingException {
        String toEmail = "mmatellan@epc-instore.com";
        String subject = "Test Subject";
        String messageBody = "Test Message Body";

        try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class);
             MockedStatic<Transport> mockedTransport = Mockito.mockStatic(Transport.class)) {

            Session mockSession = mock(Session.class);
            mockedSession.when(() -> Session.getInstance(any(Properties.class), any())).thenReturn(mockSession);

            try (MockedConstruction<MimeMessage> mockedMimeMessage = mockConstruction(MimeMessage.class, (mock, context) -> {
                when(mock.getSession()).thenReturn(mockSession);
            })) {
                mockedTransport.when(() -> Transport.send(any(Message.class))).thenAnswer(invocation -> null);

                emailSender.sendEmail(toEmail, subject, messageBody);

                MimeMessage mockMimeMessage = mockedMimeMessage.constructed().get(0);
                verify(mockMimeMessage).setFrom((Address) any());
                verify(mockMimeMessage).setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                verify(mockMimeMessage).setSubject(subject);
                verify(mockMimeMessage).setText(messageBody);

                mockedTransport.verify(() -> Transport.send(any(Message.class)));
            }
        }
    }

    @Test
    public void testSendEmailWithMessagingException() throws MessagingException {
        String toEmail = "mmatellan@epc-instore.com";
        String subject = "Test Subject";
        String messageBody = "Test Message Body";

        try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class);
             MockedStatic<Transport> mockedTransport = Mockito.mockStatic(Transport.class)) {

            Session mockSession = mock(Session.class);
            mockedSession.when(() -> Session.getInstance(any(Properties.class), any())).thenReturn(mockSession);

            try (MockedConstruction<MimeMessage> mockedMimeMessage = mockConstruction(MimeMessage.class, (mock, context) -> {
                when(mock.getSession()).thenReturn(mockSession);
            })) {
                mockedTransport.when(() -> Transport.send(any(Message.class))).thenThrow(new MessagingException("Failed to send email"));


                emailSender.sendEmail(toEmail, subject, messageBody);


                MimeMessage mockMimeMessage = mockedMimeMessage.constructed().get(0);
                verify(mockMimeMessage).setFrom((Address) any());
                verify(mockMimeMessage).setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                verify(mockMimeMessage).setSubject(subject);
                verify(mockMimeMessage).setText(messageBody);

                mockedTransport.verify(() -> Transport.send(any(Message.class)));
            }
        }
    }

    @Test
    public void testSendRealEmail() {
        EmailSender emailSender = new EmailSender();

        String toEmail = "mmatellan@epc-instore.com";
        String subject = "Prueba de Correo desde Test";
        String messageBody = "Este es un correo de prueba enviado desde una prueba de integración.";


        emailSender.sendEmail(toEmail, subject, messageBody);

        System.out.println("Correo enviado.");
    }
}