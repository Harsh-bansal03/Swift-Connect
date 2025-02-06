package com.scm.controllers;

import com.scm.entities.Contact;
import com.scm.entities.Email;
import com.scm.entities.EmailRequest;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.EmailService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationNotSupportedException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showEmailForm(Model model, Authentication authentication) {
        model.addAttribute("emailRequest", new EmailRequest());
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication)).getUserId();
        List<Contact> contacts = contactService.getByUserId(user);
        model.addAttribute("contacts", contacts);

        List<Email> sentEmails = emailService.getSentEmailsForLoggedInUser(user);
        model.addAttribute("sentEmails", sentEmails);

        return "user/emailForm"; // Thymeleaf template name
    }


    @PostMapping("/send")
    public String sendEmail(@ModelAttribute EmailRequest emailRequest, Model model, HttpSession session,Authentication authentication) {
        try {

            String recipients = emailRequest.getTo();
            var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication)).getUserId();
            for (String recipient : recipients.split(",")){

                log.info(recipient);
                emailService.sendEmailMessage(recipient, emailRequest.getSubject(), emailRequest.getBody(), emailRequest.getAttachments(),user);


            }


            model.addAttribute("message", "Email sent successfully!");

           session.setAttribute("message",
                    Message.builder()
                            .content("Email sent successfully!")
                            .type(MessageType.green)
                            .build());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());

            session.setAttribute("message",
                    Message.builder()
                            .content(e.getMessage())
                            .type(MessageType.red)
                            .build());
        }
        return "redirect:/email"; // Redirect back to the form
    }
}
