package com.week6.login.controller;


import java.security.Principal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.week6.login.model.UserDtls;
import com.week6.login.repository.UserRepository;
import com.week6.login.service.message;
import com.week6.login.service.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String username = p.getName();
		UserDtls user = userRepo.findByUsername(username);

		m.addAttribute("user", user);

	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/change")
    public String loadpass(){
        return "editpass";
    }

	@GetMapping("/editprof")
    public String updateprofile()
    {
        return "editprof";
    }

	@PostMapping("/editprofile")
    public String updatedprofile(Principal p,@RequestParam(name="phone",required = false) Long phone,@RequestParam(name="age",required = false) Integer age,@RequestParam(name="name",required = false) String name,@RequestParam(name="email",required = false) String email,@RequestParam(name="address",required = false) String address)
    {
        
        UserDtls userdtl=userService.getUserByUsername(p.getName());
        userdtl.setEmail(email);
        userdtl.setAddress(address);
        userdtl.setName(name);
		userdtl.setAge(age);
		userdtl.setPhone(phone);
        userRepo.save(userdtl);

        
        return "redirect:/user/editprof";
        
    }



	@PostMapping("/updatepassword")
    public String changepass(Principal p, @RequestParam("old") String oldpass, @RequestParam("newpass") String newpass,HttpSession session)
    {
        String username=p.getName();

        UserDtls loginuser=userRepo.findByUsername(username);
           Boolean succ= passwordEncoder.matches(oldpass, loginuser.getPassword());

           if(succ)
           {
              loginuser.setPassword(passwordEncoder.encode(newpass));
              UserDtls login=userRepo.save(loginuser);
                if(login!=null){
                    session.setAttribute("msgp", new message("Password Updated Successfully","alert-success"));
                }
                else{
                        session.setAttribute("msgp", new message("Something Went Wrong", "alert-danger"));
                }
           }
           else
           {
            session.setAttribute("msgp", new message("Invalid Old Password", "alert-danger"));
           }



        return"redirect:/user/change";
    }

}