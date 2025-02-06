package com.scm.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private List<MultipartFile> attachments;

    // Getters and Setters
}