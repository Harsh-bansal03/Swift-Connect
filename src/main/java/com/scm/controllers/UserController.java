package com.scm.controllers;

import com.scm.entities.Activity;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.UserForm;
import com.scm.helpers.Helper;
import com.scm.services.ActivityService;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.validators.ValidFile;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scm.services.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // user dashbaord page
    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model,Authentication authentication) {

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        model.addAttribute("totalContacts", contactService.getByUserId(user.getUserId()).size());
        List<Activity> recentActivities = activityService.getRecentActivities(user.getUserId());
        model.addAttribute("recentActivities", recentActivities);
        logger.info("total count = {}   {}",contactService.getByUserId(user.getUserId()).size(),recentActivities.toString());
        return "user/user_dashboard"; // This will be your dashboard template
    }

    // user profile page

    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {

        return "user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfilePic(@ValidFile @RequestParam("imageFile")MultipartFile imageFile, Model model, Authentication authentication){
        String fileName = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(imageFile, fileName);
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        user.setProfilePic(imageUrl);
        userService.updateUser(user);
        logger.info("updated user {}", user.getName());

        model.addAttribute("userprofile",imageUrl);
return "user/profile";

    }



}
