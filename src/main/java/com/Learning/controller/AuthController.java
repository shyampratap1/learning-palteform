package com.Learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.Learning.Entity.User;
import com.Learning.userRepo.UserRepo;

@Controller
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/index")
    public String index() {
        return "index";
    }
	
	@GetMapping("/login")
	public String login() { 
		return "login";
	}
	@GetMapping("/homeExam")
	public String home() { 
		return "Examhome";
	}
	
	  @GetMapping("/profile")
	    public String profile(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); // Get the logged-in username
	        User user = userRepository.findByUsername(username);
	        
	        model.addAttribute("student", user); // Pass the user object to the view
	        return "profile"; // Return the view name
	    }
	
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("USER"); // Assign a default role
        userRepository.save(user);
        return "redirect:/login";
    }
}