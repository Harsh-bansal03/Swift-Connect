package com.scm.repsitories;

import com.scm.entities.Contact;
import com.scm.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
//    @Query("SELECT c FROM Email c WHERE c.user.id = :userId")
    List<Email> findByUserId(String userId);
}