package com.scm.services;

import com.scm.entities.Email;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmailService {


    void sendEmail(String to, String subject, String body);


    void sendEmailMessage(String to, String subject, String body, List<MultipartFile> attachments, String user) throws Exception;

List<Email> getSentEmailsForLoggedInUser(String user);


    //    void sendEmailWithHtml(String to, String subject, String htmlContent);
//
//    //
//    void sendEmailWithAttachment(String to,String subject,String message,File file);

}
