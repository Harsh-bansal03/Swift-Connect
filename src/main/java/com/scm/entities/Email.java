package com.scm.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        private String subject;
        private String recipients; // Comma-separated for display
        private LocalDateTime sentDate;
        private String content;


    private String userId;
        // Getters and setters
    }
