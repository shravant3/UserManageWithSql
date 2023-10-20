package com.week6.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.week6.login.model.UserDtls;
import com.week6.login.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String start(){
        return "start";
    }

    @GetMapping("register")
    public String register(){
        return "register";
    }

    @GetMapping("/login")
    public String login()
    {
       Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
         if(authentication==null|| authentication instanceof AnonymousAuthenticationToken)
         {
            return "login";
         }

         if(authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN"))){

            return "redirect:/admin/";
         }
         else{
                return "redirect:/user/";
         }
         
        
    }
    

    
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute UserDtls user,HttpSession session)
    {
        boolean f=userService.checkUsername(user.getUsername());
        if(f==true){
            session.setAttribute("message","Username already Exists");
             Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            if(authentication==null|| authentication instanceof AnonymousAuthenticationToken)
            {
                return "redirect:/register";
            }
            else
            {
                return "redirect:/admin/register";
            }
            
        }
        else{
            UserDtls userDetails=userService.createUser(user);
            if(userDetails!=null)
            {
                session.setAttribute("message","Registered Successfully");
                Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
                if(authentication==null|| authentication instanceof AnonymousAuthenticationToken)
                {
                    return "redirect:/register";
                }
                else
                {
                    return "redirect:/admin/register";
                }
            }
            else
            {
                session.setAttribute("message","Some Error in Server");
                Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
                if(authentication==null|| authentication instanceof AnonymousAuthenticationToken)
                {
                    return "redirect:/register";
                }
                else
                {
                    return "redirect:/admin/register";
                }
            }
        } 
        
    }
 

}
