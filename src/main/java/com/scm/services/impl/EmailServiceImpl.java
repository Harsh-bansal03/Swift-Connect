package com.scm.services.impl;

import com.scm.entities.Email;
import com.scm.helpers.Helper;
import com.scm.repsitories.EmailRepository;
import com.scm.services.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender eMailSender;
    private final EmailRepository emailRepository;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    public EmailServiceImpl(JavaMailSender eMailSender, EmailRepository emailRepository) {
        this.eMailSender = eMailSender;
        this.emailRepository=emailRepository;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("verify_swiftconnect"+domainName);
        eMailSender.send(message);

    }

    @Override
    public void sendEmailMessage(String to, String subject, String body, List<MultipartFile> attachments, String user) throws Exception {
        MimeMessage message = eMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username = Helper.getEmailOfLoggedInUser(authentication);
        helper.setTo(to);
        helper.setSubject(subject);
        String disclaimer="This mail has been sent by **swiftconnect.scm@gmail.com** on behalf of user with email: " +username;
        String bodyWithDisclaimer=body+"<br><br><small><i>"+disclaimer+"."+"</i></small>";
        helper.setText(bodyWithDisclaimer,true);
        String userName = username.split("@")[0];
        String fromEmail = userName + domainName;
        log.info("here is the email: {}",fromEmail);
        helper.setFrom(fromEmail); // Set this dynamically based on logged-in user

        // Handle attachments
        if (attachments != null) {
            for (MultipartFile attachment : attachments) {
                if (!attachment.isEmpty()) {
                    if (attachment.getSize() > 5 * 1024 * 1024) { // Limit to 5 MB
                        throw new Exception("File size exceeds the limit of 5MB for " + attachment.getOriginalFilename());
                    }
                    helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
                }
            }
        }


        eMailSender.send(message);


        Email email = new Email();
        email.setRecipients(to);
        email.setSubject(subject);
        email.setContent(body);
        email.setSentDate(LocalDateTime.now());
        email.setUserId(user);

        emailRepository.save(email);

    }


    @Override
    public List<Email> getSentEmailsForLoggedInUser(String user) {
        // Retrieve emails for the logged-in user from the repository
        return emailRepository.findByUserId(user);
    }

}

//
//    @Override
//    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
//        MimeMessage message=eMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8")
//            helper.setTo(to);
//            helper.setSubject(subject);
////            helper.setFrom();
//            helper.setText(htmlContent,true);
//            eMailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void sendEmailWithAttachment(String to, String subject, String message, File file) {
//
//    }


